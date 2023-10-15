package de.conet.fls.apibro.httpfileparser;

import de.conet.fls.apibro.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_1_RequestsFileTest {

    // TODO doc

    @Test
    public void request() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "GET http://example.com\n",
                HttpFileParser::request
        );

        Assertions.assertTrue(parsed.children.size() > 0);
//        System.out.println(parsed.toStringTree());
    }

    @Test
    public void requestWithNakedSeparator() {
        HttpFileParser.RequestWithSeparatorContext parsed = ParserTestUtil.test(
                "###\n" +
                "GET http://example.com\n",
                HttpFileParser::requestWithSeparator
        );

        Assertions.assertNotNull(parsed.RequestSeparator(0));
        Assertions.assertNotNull(parsed.request());
    }

    @Test
    public void requestWithCommentedSeparator() {
        HttpFileParser.RequestWithSeparatorContext parsed = ParserTestUtil.test(
                "### This is a demo request! $=o)))\n" +
                "GET http://example.com\n",
                HttpFileParser::requestWithSeparator
        );

        Assertions.assertNotNull(parsed.RequestSeparator(0));
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

        Assertions.assertEquals(3, parsed.RequestSeparator().size()); // 3 Separator Lines (starting with "###")
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
        Assertions.assertEquals(3, parsed.RequestSeparator().size());
    }

    @Test
    public void requestFileWhitespaces() {
        HttpFileParser.RequestsFileContext parsed = ParserTestUtil.test(
                "   GET http://example.com\t   \t\n" +
                "\t###\n" +
                "\t   \t###\n" +
                "###\t   \t\n",
                HttpFileParser::requestsFile
        );

        Assertions.assertNotNull(parsed.request());
        Assertions.assertEquals(3, parsed.RequestSeparator().size());
    }
}
