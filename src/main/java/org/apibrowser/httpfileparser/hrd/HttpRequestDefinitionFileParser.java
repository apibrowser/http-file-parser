package org.apibrowser.httpfileparser.hrd;

import org.apibrowser.httpfileparser.HttpFileParser;
import org.apibrowser.httpfileparser.HttpFileParserFactory;
import org.apibrowser.httpfileparser.hrd.model.HttpRequestDefinition;
import org.apibrowser.httpfileparser.hrd.model.HttpRequestDefinitionCollection;

import java.io.IOException;
import java.io.InputStream;

/**
 * Facade around the "raw" HttpFileParser to provide a more convenient API and an easier to use
 * domain model.
 */
public class HttpRequestDefinitionFileParser {
    public HttpRequestDefinitionCollection parse(InputStream file) {
        try {
            HttpFileParser parsed = HttpFileParserFactory.parse(file);
            return map(parsed.requestsFile());
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    private HttpRequestDefinitionCollection map(HttpFileParser.RequestsFileContext raw) {
        /*
         * RequestsFileContext contains RequestContexts in 2 places:
         * .request() contains the first Request, the others are in
         * .requestWithSeparator() (which might be empty)
         *
         * The first request might have a Request Separator Line / Block on top, containing. But it can also be skipped,
         * while all following requests must have a separator above them.
         */
        HttpRequestDefinitionCollection result = new HttpRequestDefinitionCollection();

        // Skip empty files:
        if (raw.request() == null) {
            return result;
        }

        // Map First request:
        HttpFileParser.FileHeaderContext fileHeader = raw.fileHeader();
        HttpFileParser.CommentBlockContext commentBlock = fileHeader != null ? fileHeader.commentBlock() : null;

        result.add(toHrd(
                commentBlock != null && commentBlock.requestSeparator().size() > 0
                        ? commentBlock.requestSeparator(0)
                        : null,
                commentBlock, // might be empty
                raw.request() // guaranteed not empty
        ));

        for (HttpFileParser.RequestWithSeparatorContext requestWithSep : raw.requestWithSeparator()) {
            result.add(toHrd(
                    requestWithSep.requestSeparator(), requestWithSep.commentBlock(), requestWithSep.request()
            ));
        }

        return result;
    }

    /**
     * @param requestSeparatorContext
     * @param commentBlock            might be null
     */
    private HttpRequestDefinition toHrd(HttpFileParser.RequestSeparatorContext requestSeparatorContext,
                                        HttpFileParser.CommentBlockContext commentBlock,
                                        HttpFileParser.RequestContext request) {
        return new HttpRequestDefinition(); // TODO
    }
}
