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


    /**
     * Print the tree of the given context for TypeScript.
     */
    public static void printTsTree(ParserRuleContext ctx) {
        if (ctx == null) {
            System.out.println("null");
            return;
        }

        String stringTree = ctx.toStringTree(Arrays.asList(HttpFileParser.ruleNames));

        StringBuilder sb = new StringBuilder();
        int level = 0;
        for (char c : stringTree.toCharArray()) {
            if(c == ')') {
                sb.append('\n');
                level--;
                for (int i = 0; i < level; i++)
                    sb.append('\t');
            }

            sb.append(c);

            if (c == '(') {
                sb.append('\n');
                level++;
                for (int i = 0; i < level; i++)
                    sb.append('\t');
            }
        }


        System.out.println(sb.toString());
    }
}
