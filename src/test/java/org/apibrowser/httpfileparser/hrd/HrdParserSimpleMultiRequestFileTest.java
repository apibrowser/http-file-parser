package org.apibrowser.httpfileparser.hrd;

import org.apibrowser.httpfileparser.hrd.model.HttpMethod;
import org.apibrowser.httpfileparser.hrd.model.HttpRequestDefinitionCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * test for /examples/simple-multi-request.http
 */
public class HrdParserSimpleMultiRequestFileTest {

    public static final String TEST_FILE_PATH = "/examples/simple-multi-request.http";

    @Test
    public void testShouldDetectAllRequests() {
        InputStream file = getClass().getResourceAsStream(TEST_FILE_PATH);
        HttpRequestDefinitionCollection requests = new HttpRequestDefinitionFileParser().parse(file);
        Assertions.assertEquals(4, requests.size());
    }
}
