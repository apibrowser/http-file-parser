package de.conet.fls.apibro.httpfileparser;

import de.conet.fls.apibro.httpfileparser.testutil.ParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpFileParser_3_2_0_RequestTest {

    // TODO doc

    @Test
    public void request() {
        HttpFileParser.RequestContext parsed = ParserTestUtil.test(
                "GET http://example.com\n",
                HttpFileParser::request
        );

        Assertions.assertNotNull(parsed.requestLine());
    }
}
