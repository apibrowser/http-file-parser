package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_2_2_HeadersTest {

    @Test
    public void requestWithHeaders() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "GET http://example.com/api/get?id=15\n" +
                "Content-Type: application/json  \n" +
                "From: user@example.com\t\n" +
                "\n",
                HttpFileParser::request
        );

        Assertions.assertEquals("Content-Type: application/json  \nFrom: user@example.com\t\n", parsed.headers().getText());
    }

    @Test
    public void headers() {
        HttpFileParser.HeadersContext parsed = ParserTestUtil.test(
                "Content-Type: application/json\n" +
                "From: user@example.com  \n" +
                "Accept: application/json\t\n" +
                "",
                HttpFileParser::headers
        );

        Assertions.assertEquals(3, parsed.headerField().size());
    }

    @Test
    public void headerField() {
        HttpFileParser.HeaderFieldContext parsed = ParserTestUtil.test(
                "Content-Type: application/json\n" +
                "",
                HttpFileParser::headerField
        );

        Assertions.assertEquals("Content-Type", parsed.fieldName().getText());
        Assertions.assertEquals("application/json", parsed.fieldValue().getText());
    }

}
