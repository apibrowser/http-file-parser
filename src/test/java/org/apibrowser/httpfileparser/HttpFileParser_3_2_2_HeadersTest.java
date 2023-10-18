package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_2_2_HeadersTest {

    @Test
    public void requestWithHeaders() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "GET http://example.com/api/get?id=15\n" +
                "From: user@example.com\n" +
                "\n",
                HttpFileParser::request
        );

        Assertions.assertEquals("From: user@example.com\n", parsed.headers().getText());
    }

    // TODO add more tests

}
