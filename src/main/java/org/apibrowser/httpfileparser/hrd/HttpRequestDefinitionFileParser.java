package org.apibrowser.httpfileparser.hrd;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apibrowser.httpfileparser.HttpFileParser;
import org.apibrowser.httpfileparser.HttpFileParserFactory;
import org.apibrowser.httpfileparser.hrd.model.HttpRequestDefinition;
import org.apibrowser.httpfileparser.hrd.model.HttpRequestDefinitionCollection;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
        result.add(toHrd(
            raw.RequestSeparator(), // might be empty
            raw.request() // not empty
        ));

        for (HttpFileParser.RequestWithSeparatorContext request : raw.requestWithSeparator()) {
            result.add(toHrd(request.RequestSeparator(), request.request()));
        }

        return result;
    }

    private HttpRequestDefinition toHrd(List<TerminalNode> requestSeparator, HttpFileParser.RequestContext request) {
        return new HttpRequestDefinition(); // TODO
    }
}
