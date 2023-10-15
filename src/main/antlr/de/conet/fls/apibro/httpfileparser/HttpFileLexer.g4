//
// This is an ANTLR translation of the spec https://github.com/JetBrains/http-request-in-editor-spec/blob/master/spec.md
//
lexer grammar HttpFileLexer;

// Additions to original spec for better Parser Experience
fragment WhiteSpace: (' ' | '\t')*;
//

fragment LineTale: (InputCharacter)* NewLine;
fragment NewLine: ('\n' | '\r' | '\r\n');
fragment InputCharacter: ~[\r\n\u2028\u2029]+; // must be fragment, otherwise would match pretty much every text

RequestSeparator: WhiteSpace '###' LineTale;
MockRequest: WhiteSpace 'GET http://example.com' WhiteSpace NewLine; // TODO remove Mock with real rules in Parser Grammar

