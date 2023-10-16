package de.conet.fls.apibro.httpfileparser.testutil;

import de.conet.fls.apibro.httpfileparser.HttpFileParser;
import de.conet.fls.apibro.httpfileparser.HttpFileParserFactory;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ParserTestUtil {

    public static HttpFileParser parse(String input) {
        return parse(input, false);
    }

    public static HttpFileParser parse(String input, boolean printTokens) {
        if (printTokens) {
            // Note: cannot reuse charStream for both: printing and parsing,
            // since the stream will be consumed after the first of both actions.
            CharStream charStream = CharStreams.fromString(input);
            HttpFileParserFactory.printTokens(charStream);
        }

        CharStream freshCharStream = CharStreams.fromString(input);
        return HttpFileParserFactory.parse(freshCharStream);
    }

    public static HttpFileParser parse(InputStream input) {
        CharStream charStream = null;
        try {
            charStream = CharStreams.fromStream(input);
        } catch (IOException e) {
            Assertions.fail("Could not read testdata", e);
        }
        return HttpFileParserFactory.parse(charStream);
    }

    public static <T extends ParserRuleContext> T test(String input, Function<HttpFileParser, T> ruleInvoker) {
        HttpFileParser parser = parse(input, true);
        T result = ruleInvoker.apply(parser);

        Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors(),
                String.format("UNEXPECTED PARSE ERROR: The input '%s' should be parsed without syntax errors.", input));

        return result;
    }

    public static <T extends ParserRuleContext> T test(InputStream input, Function<HttpFileParser, T> ruleInvoker) {
        HttpFileParser parser = parse(input);
        T result = ruleInvoker.apply(parser);

        Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors(),
                String.format("UNEXPECTED PARSE ERROR: The input '%s' should be parsed without syntax errors.", input));

        return result;
    }

    public static <T extends ParserRuleContext> T expectParseError(String input, Function<HttpFileParser, T> ruleInvoker) {
        HttpFileParser parser = parse(input);

        parser.removeErrorListeners(); //do not spam the log with expected syntax errors
//        parser.addErrorListener(new BaseErrorListener() {
//            @Override
//            public void syntaxError(Recognizer<?, ?> recognizer,
//                                    Object offendingSymbol, int line, int charPositionInLine,
//                                    String msg, RecognitionException e) {
//                System.out.println(((Token) offendingSymbol).getText());
//            }
//        });

        T result = ruleInvoker.apply(parser);

        Assertions.assertNotEquals(0, parser.getNumberOfSyntaxErrors(),
                String.format("EXPECTED PARSE ERROR: The input '%s' should throw a syntax error," +
                              " but was considered valid by the parser.", input));

        return result;
    }


    public static <T extends ParserRuleContext> T expectParseError(InputStream input, Function<HttpFileParser, T> ruleInvoker) {
        HttpFileParser parser = parse(input);

        parser.removeErrorListeners(); //do not spam the log with expected syntax errors

        T result = ruleInvoker.apply(parser);

        Assertions.assertNotEquals(0, parser.getNumberOfSyntaxErrors(),
                String.format("EXPECTED PARSE ERROR: The input '%s' should throw a syntax error," +
                              " but was considered valid by the parser.", input));

        return result;
    }

    public static <T extends ParserRuleContext> List<T> test(Function<HttpFileParser, T> ruleInvoker, String... inputs) {
        List<T> results = new ArrayList<>(inputs.length);
        for (String input : inputs) {
            results.add(test(input, ruleInvoker));
        }
        return results;
    }


}
