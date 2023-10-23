package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HttpFileParser_3_2_4_ResponseHandler {

    @Test
    public void requestWithResponseHandlerInline() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "GET http://example.com/auth\n" +
                "\n" +
                "> {% client.global.set(\"auth\", response.body.token);%}\n" +
                "\n",
                HttpFileParser::request
        );

        Assertions.assertEquals("client.global.set(\"auth\", response.body.token);",
                parsed.bodyAndOrResponseHandlers().responseHandler().handlerScript().getText().trim());
    }

    @Test
    public void requestWithResponseHandlerFileRef() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "GET http://example.com/auth\n" +
                "\n" +
                "> set-auth.script\n" +
                "\n",
                HttpFileParser::request
        );

        Assertions.assertEquals("set-auth.script",
                parsed.bodyAndOrResponseHandlers().responseHandler().filePath().getText().trim());
    }

}
