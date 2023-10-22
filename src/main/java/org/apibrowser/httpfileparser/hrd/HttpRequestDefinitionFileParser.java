package org.apibrowser.httpfileparser.hrd;

import org.apibrowser.httpfileparser.HttpFileParser;
import org.apibrowser.httpfileparser.HttpFileParserFactory;
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
        HttpRequestDefinitionCollection httpRequestDefinitions = new HttpRequestDefinitionCollection();
        return httpRequestDefinitions; // TODO
    }
}
