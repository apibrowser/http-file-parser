package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_2_CommentTest {

    // TODO doc

    @Test
    public void requestFileWithMultipleRequests() {
        HttpFileParser.RequestsFileContext parsed = ParserTestUtil.test(
                "#\n" +
                "GET http://example.com\n" +
                "\n" +
                "###\n" +
                "#\n" +
                "\n" +
                "#\n" +
                "###\n" +
                "#\n" +
                "\n" +
                "PUT http://example.com\n" +
                "\n" +
                "###\n" +
                "POST http://example.com\n\n" +
                "###\n" +
                "",
                HttpFileParser::requestsFile
        );

        Assertions.assertNotNull(parsed.request());
        Assertions.assertEquals(2, parsed.requestWithSeparator().size());
    }
}
