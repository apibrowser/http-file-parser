package de.conet.fls.apibro.httpfileparser;

import de.conet.fls.apibro.httpfileparser.testutil.ParserTestUtil;
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
        Assertions.assertEquals(":8080", authority.port().PortDefinition().getText());
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
    public void host() {
        HttpFileParser.RequestTargetContext parsed = ParserTestUtil.test(
                "http://{{host}}",
                HttpFileParser::requestTarget
        );

        HttpFileParser.AuthorityContext authority = parsed.absoluteForm().hierPart().authority();
        Assertions.assertEquals("{{host}}", authority.host().ipv4OrRegName().getText());
    }


}
