package de.conet.fls.apibro.httpfileparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HttpFileParserTest {
    @Test
    public void smokeTest() throws IOException {
        HttpFileParser parsed = HttpFileParserFactory.parse(getClass().getResourceAsStream("/example.http"));
        System.out.println(parsed);
    }
}
