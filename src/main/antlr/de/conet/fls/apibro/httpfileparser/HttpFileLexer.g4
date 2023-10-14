lexer grammar HttpFileLexer;

fragment InputCharacter: ~[\r\n\u2028\u2029]+;
fragment NewLine: '\n' | '\r' | '\r\n';
fragment LineTale: (InputCharacter)* NewLine;
RequestSeparator: '###' LineTale;

MockRequest: 'GET http://example.com' NewLine | 'GET xxx' NewLine; // TODO remove Mock with real rules in Parser Grammar
