package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_2_1_RequestLineTest {

    // TODO doc

    @Test
    public void requestLine() {
        HttpFileParser.RequestLineContext parsed = ParserTestUtil.test(
                "GET http://example.com HTTP/1.1\n",
                HttpFileParser::requestLine
        );

        Assertions.assertEquals("GET", parsed.method().getText());
        Assertions.assertEquals("http://example.com", parsed.requestTarget().getText());
        Assertions.assertEquals("HTTP/1.1", parsed.httpVersion().getText());
    }

    @Test
    public void httpVersion() {
        HttpFileParser.HttpVersionContext parsed = ParserTestUtil.test(
                "HTTP/2.42\n",
                HttpFileParser::httpVersion
        );

        Assertions.assertNotNull(parsed.HttpVersion().getText());
    }

    /**
     * 3.2.1.2. Authority - example 1
     */
    @Test
    public void authorityIpv6() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://[::1]",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AuthorityContext authority = parsed.absoluteForm().hierPart().authority();
        Assertions.assertEquals("[::1]", authority.host().ipv6Address().getText());
    }

    /**
     * 3.2.1.2. Authority - example 2
     */
    @Test
    public void authorityIpv4WithPort() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://127.0.0.1:8080",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AuthorityContext authority = parsed.absoluteForm().hierPart().authority();
        Assertions.assertEquals("127.0.0.1", authority.host().ipv4OrRegName().getText());
        Assertions.assertEquals(":8080", authority.port().getText());
    }

    /**
     * 3.2.1.2. Authority - example 3
     */
    @Test
    public void authorityDomain() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AuthorityContext authority = parsed.absoluteForm().hierPart().authority();
        Assertions.assertEquals("example.com", authority.host().ipv4OrRegName().getText());
    }

    @Test
    public void hostAsVariable() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://{{host}}",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AuthorityContext authority = parsed.absoluteForm().hierPart().authority();
        Assertions.assertEquals("{{host}}", authority.host().ipv4OrRegName().getText());
    }

    /**
     * 3.2.1.3. Resource path
     */
    @Test
    public void resourcePath() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/status",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AbsolutePathContext absolutePath = parsed.absoluteForm().hierPart().absolutePath();
        Assertions.assertEquals("/api/status", absolutePath.getText());
    }

    /**
     * 3.2.1.3. Resource path
     * Spec: "HTTP Request in Editor supports unicode characters as part of a resources path.
     * A resources path can be split into several lines for better request readability.
     * Line separators won’t be sent as part of the request during execution."
     */
    @Test
    public void resourcePathWithMultiLines() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/\n" +
                "\tstatus\n" +
                "   /foo\n" +
                " bar",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AbsolutePathContext absolutePath = parsed.absoluteForm().hierPart().absolutePath();
        Assertions.assertEquals("/api/status/foobar", absolutePath.getText().replaceAll("[\n\t ]", ""));
    }

    /**
     * see {@link #resourcePathWithMultiLines()}
     */
    @Test
    public void resourcePathShouldNotMatchNewLineWithoutIndent() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/\n" +
                "   indented\n" +
                "not-indented",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AbsolutePathContext absolutePath = parsed.absoluteForm().hierPart().absolutePath();
        // should *not* contain "not-indented":
        Assertions.assertFalse(absolutePath.getText().contains("not-indented"));
    }


    /**
     * 3.2.1.4. Query and Fragment -> query
     */
    @Test
    public void query() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/status?myQuery=foo&yourQuery=bar",
                HttpFileParser::requestTarget
        );

        HttpFileParser.QueryContext query = parsed.absoluteForm().query();
        Assertions.assertEquals("myQuery=foo&yourQuery=bar", query.getText());
    }

    /**
     * 3.2.1.4. Query and Fragment -> query
     */
    @Test
    public void queryWithMultilines() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/status?myQuery=foo\n" +
                "\t&your\n" +
                "   Quer\n" +
                " y=bar",
                HttpFileParser::requestTarget
        );

        HttpFileParser.QueryContext query = parsed.absoluteForm().query();
        Assertions.assertEquals("myQuery=foo&yourQuery=bar", query.getText().replaceAll("[\n\t ]", ""));
    }

    /**
     * 3.2.1.4. Query and Fragment -> query
     */
    @Test
    public void queryShouldNotMatchNewLineWithoutIndent() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/status?myQuery=foo\n" +
                " &indented\n" +
                "&not-indented",
                HttpFileParser::requestTarget
        );

        HttpFileParser.QueryContext query = parsed.absoluteForm().query();
        // should *not* contain "not-indented":
        Assertions.assertFalse(query.getText().contains("not-indented"));
    }

    /**
     * 3.2.1.4. Query and Fragment -> fragment
     */
    @Test
    public void uriFragment() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/status#theFragment&theFragment#theFragment's-on-fire",
                HttpFileParser::requestTarget
        );

        HttpFileParser.UriFragmentContext uriFragment = parsed.absoluteForm().uriFragment();
        Assertions.assertEquals("theFragment&theFragment#theFragment's-on-fire", uriFragment.getText());
    }

    /**
     * 3.2.1.4. Query and Fragment -> fragment
     */
    @Test
    public void uriFragmentWithMultilines() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/status#theFragment\n" +
                "\t&theFragment#the\n" +
                "   Fragment's-on\n" +
                " -fire",
                HttpFileParser::requestTarget
        );

        HttpFileParser.UriFragmentContext uriFragment = parsed.absoluteForm().uriFragment();
        Assertions.assertEquals("theFragment&theFragment#theFragment's-on-fire",
                uriFragment.getText().replaceAll("[\n\t ]", ""));
    }

    /**
     * 3.2.1.4. Query and Fragment -> fragment
     */
    @Test
    public void uriFragmentShouldNotMatchNewLineWithoutIndent() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://example.com/api/status#theFragment\n" +
                " indented\n" +
                "not-indented\n",
                HttpFileParser::requestTarget
        );

        HttpFileParser.UriFragmentContext uriFragment = parsed.absoluteForm().uriFragment();
        // should *not* contain "not-indented":
        Assertions.assertFalse(uriFragment.getText().contains("not-indented"));
    }

}
