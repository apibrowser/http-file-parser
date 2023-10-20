package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_2_3_MessageBody {

    @Test
    public void requestWithMessageBody() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "POST http://example.com/api/add\n" +
                "Content-Type: application/json\n" +
                "\n" +
                "{ \n" +
                "   \"key\": \"value\", \n" +
                "   \"another\": \"field\" \n" +
                "}\n" +
                "\n",
                HttpFileParser::request
        );

        Assertions.assertEquals("{ \"key\": \"value\", \"another\": \"field\" }",
                parsed.bodyAndOrResponseHandlers().messageBody().getText()
                        .replaceAll("\n", "")
                        .replaceAll(" +", " ")
                        .trim());
    }

    @Test
    public void messageBody() {
        HttpFileParser.MessageBodyContext parsed = ParserTestUtil.test(
                "{ \n" + // 1
                "   \"key\": \"value\", \n" + //2
                "}\n" + // 3
                "\n",
                HttpFileParser::messageBody
        );

        Assertions.assertEquals(3, parsed.messages().messageLine().size());
        Assertions.assertEquals("}", parsed.messages().messageLine(2).getText());
    }

    @Test
    public void messageBodyWithInputFileRef() {
        HttpFileParser.MessageBodyContext parsed = ParserTestUtil.test(
                "< content.json" +
                "\n",
                HttpFileParser::messageBody
        );

        Assertions.assertEquals("content.json", parsed.messages().messageLine(0).inputFileRef().filePath().getText());
    }

    @Test
    public void messageBodyWithInputFileRefMixed() {
        HttpFileParser.MessageBodyContext parsed = ParserTestUtil.test(
                "  hell, oh  \n" +
                "< content.json\n" +
                "\tworld\n  " +
                "\n",
                HttpFileParser::messageBody
        );

        Assertions.assertEquals("hell, oh", parsed.messages().messageLine(0).getText().trim());
        Assertions.assertEquals("content.json", parsed.messages().messageLine(1).inputFileRef().filePath().getText());
        Assertions.assertEquals("world", parsed.messages().messageLine(2).getText().trim());
    }

}
