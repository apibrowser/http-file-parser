package org.apibrowser.httpfileparser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.apibrowser.httpfileparser.util.DebugHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_1_RequestsFileTest {

    // TODO doc

    @Test
    public void requestWithNakedSeparator() {
        HttpFileParser.RequestWithSeparatorContext parsed = ParserTestUtil.test(
                "###\n" +
                "GET http://example.com\n",
                HttpFileParser::requestWithSeparator
        );

        Assertions.assertNotNull(parsed.requestSeparator().RequestSeparator());
        Assertions.assertNotNull(parsed.request());
    }

    @Test
    public void requestWithCommentedSeparator() {
        HttpFileParser.RequestWithSeparatorContext parsed = ParserTestUtil.test(
                "### This is a demo request! $=o)))\n" +
                "GET http://example.com\n",
                HttpFileParser::requestWithSeparator
        );

        Assertions.assertNotNull(parsed.requestSeparator().RequestSeparator());
        Assertions.assertNotNull(parsed.request());
    }

    @Test
    public void requestWithMultipleSeparator() {
        HttpFileParser.RequestWithSeparatorContext parsed = ParserTestUtil.test(
                "### \n" + // whitespace before linebreak is arbitrary!
                "### This is a demo request! $=o)))\n" +
                "###\n" +
                "GET http://example.com\n",
                HttpFileParser::requestWithSeparator
        );

        Assertions.assertNotNull(parsed.requestSeparator().RequestSeparator()); // first Separator Line  (starting with "###")
        Assertions.assertEquals(2, parsed.commentBlock().requestSeparator().size()); // 2 Separator Lines below
        Assertions.assertNotNull(parsed.request());
    }

    @Test
    public void requestFileStartingWithSeparators() {
        HttpFileParser.RequestsFileContext parsed = ParserTestUtil.test(
                "###\n" +
                "###\n" +
                "###\n" +
                "GET http://example.com\n",
                HttpFileParser::requestsFile
        );

        Assertions.assertNotNull(parsed.request());
    }

    @Test
    public void requestFileMinimal() {
        HttpFileParser.RequestsFileContext parsed = ParserTestUtil.test(
                "GET http://example.com\n",
                HttpFileParser::requestsFile
        );

        Assertions.assertNotNull(parsed.request());
    }

    @Test
    public void requestFileTrailingSeparators() {
        HttpFileParser.RequestsFileContext parsed = ParserTestUtil.test(
                "GET http://example.com\n" +
                "###\n" +
                "###\n" +
                "###\n",
                HttpFileParser::requestsFile
        );

        Assertions.assertNotNull(parsed.request());
        Assertions.assertEquals(3, parsed.fileFooter().commentBlock().requestSeparator().size());
    }

    @Test
    public void requestFileWithMultipleRequests() {
        HttpFileParser.RequestsFileContext parsed = ParserTestUtil.test(
                "GET http://example.com\n" +
                "\n" +
                "###\n" +
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
