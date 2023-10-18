package org.apibrowser.httpfileparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HttpFileParserSmokeTest {
    @Test
    public void smokeTest() throws IOException {
        HttpFileParser parsed = HttpFileParserFactory.parse(getClass().getResourceAsStream("/example.http"));
        System.out.println(parsed);
    }
}
