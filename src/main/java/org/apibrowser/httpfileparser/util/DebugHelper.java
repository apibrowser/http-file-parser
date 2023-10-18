package org.apibrowser.httpfileparser.util;

import org.apibrowser.httpfileparser.HttpFileLexer;
import org.apibrowser.httpfileparser.HttpFileParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.Arrays;

public class DebugHelper {
    public static void printTokens(Lexer lexer) {
        //TODO: this does not print the proper rule name
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();

        for (Token token : tokenStream.getTokens()) {
            if (token.getType() != HttpFileLexer.EOF) {
                System.out.printf("  %-20s %s\n", lexer.getVocabulary().getSymbolicName(token.getType()), token.getText());
            }
        }

        System.out.println();
    }


    public static void printTree(ParserRuleContext parser) {
        System.out.println(TreeUtils.toPrettyTree(parser, Arrays.asList(HttpFileParser.ruleNames)));
    }
}
