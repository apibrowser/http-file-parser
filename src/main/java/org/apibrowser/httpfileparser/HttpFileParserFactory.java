package org.apibrowser.httpfileparser;

import org.apibrowser.httpfileparser.util.DebugHelper;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class HttpFileParserFactory {
    public static HttpFileParser parse(CharStream charStream) {
        HttpFileLexer lexer = new HttpFileLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new HttpFileParser(tokens);
    }

    public static HttpFileParser parse(InputStream input) throws IOException {
        return parse(CharStreams.fromStream(input));
    }

    public static void printTokens(CharStream charStream) {
        HttpFileLexer lexer = new HttpFileLexer(charStream);
        DebugHelper.printTokens(lexer);
    }
}
