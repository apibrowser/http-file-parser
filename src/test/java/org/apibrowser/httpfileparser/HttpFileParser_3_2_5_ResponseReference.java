package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_2_5_ResponseReference {

    @Test
    public void requestWithResponseHandlerInline() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "GET http://example.com\n" +
                "\n" +
                "<> previous-response.200.json\n" +
                "\n",
                HttpFileParser::request
        );

        Assertions.assertEquals("previous-response.200.json",
                parsed.bodyAndOrResponseHandlers().responseRef().filePath().getText().trim());
    }

}
