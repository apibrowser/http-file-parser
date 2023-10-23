package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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
                "{ \n" + // 0
                "   \"key\": \"value\", \n" + // 1
                "}\n" + // 2
                "",
                HttpFileParser::messageBody
        );

        Assertions.assertEquals(3, parsed.messages().messageLine().size());
        Assertions.assertEquals("}", parsed.messages().messageLine(2).getText().trim());
    }

    @Test
    public void messageBodyWithEmptyLines() {
        HttpFileParser.MessageBodyContext parsed = ParserTestUtil.test(
                "POST http://example.com\n" +
                "\n" +
                "a\n" +
                "b\n" +
                "c\n" +
                "\n" + // empty
                "\n" + // empty
                "d\n" +
                "e\n" +
                "f\n",
                HttpFileParser::messageBody
        );

        Assertions.assertTrue(parsed.messages().getText().contains("f"));
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

    /**
     * The special treatment of Multipart-FormData as in the official spec was skipped, but
     * we must assert that this Content-Type is still parsed as regular message body.
     */
    @Test
    public void messageBodyIncludesMultipartFormData() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "POST http://example.com/api/upload\n" +
                "Content-Type: multipart/form-data; boundary=abcd\n" +
                "\n" +
                "--abcd\n" + // message line 0
                "Content-Disposition: form-data; name=\"text\"\n" + // message line 1
                "\n" + // message line 2
                "Text\n" + // message line 3
                "--abcd\n" + // message line 4
                "Content-Disposition: form-data; name=\"file_to_send\"; filename=\"input.txt\"\n" + // message line 5
                "\n" + // message line 6
                "< ./input.txt\n" + // message line 7
                "--abcd--\n", // message line 8
                HttpFileParser::request
        );

        List<HttpFileParser.MessageLineContext> bodyLines =
                parsed.bodyAndOrResponseHandlers().messageBody().messages().messageLine();
        Assertions.assertEquals("--abcd", bodyLines.get(0).getText().trim());
        Assertions.assertEquals("--abcd--", bodyLines.get(bodyLines.size() - 1).getText().trim());
        Assertions.assertEquals("< ./input.txt", bodyLines.get(bodyLines.size() - 2).getText().trim());
    }

}
