//
// Lexer for JetBrains Http File Format.
// Spec available under:
// https://github.com/JetBrains/http-request-in-editor-spec/blob/master/spec.md
// Chapter 2 Lexical Structure
//
lexer grammar HttpFileLexer;

RequestSeparator: OptionalWhiteSpace '###' LineTale; // Note: added OptionalWhitespace, not in spec

HttpMethod: // Note: Different to the spec, this must be a Token for ANTLR
    'GET'
    | 'HEAD'
    | 'POST'
    | 'PUT'
    | 'DELETE'
    | 'CONNECT'
    | 'PATCH'
    | 'OPTIONS'
    | 'TRACE'
    ;

HttpVersion: 'HTTP/' Digit+ '.' Digit+;

fragment WhiteSpace: (' ' | '\t');
RequiredWhiteSpace: (' ' | '\t')+;
fragment OptionalWhiteSpace: (' ' | '\t')*;
NewLine: '\n' | '\r' | '\r\n';

HttpScheme: 'http' | 'https';

// Ipv6Address by spec: (any inputCharacter except ‘/’ and ‘]’)+;
// Ipv4OrRegName by spec: (any inputCharacter except ‘/’, ‘:’, ‘?’ and ‘#’)+;
// In both cases we must be more rigid to prevent producing an all-consuming-greedy type of token;
// Current try to achieve this: by adding whitespaces to the non-permitted chars
UrlIpv6Address: '[' ~[\r\n\u2028\u2029/\] \t]+ ']'; //
Ipv4OrRegName: ~[\r\n\u2028\u2029/:?# \t]+;

PortDefinition: ':' Digit+;

fragment Digit: ('0'..'9');
fragment HexDigit : [0-9a-fA-F];  // A single hexadecimal digi
fragment Alpha: ('a'..'z') | ('A'..'Z');

fragment LineTale: (InputCharacter)* NewLine;
fragment InputCharacter: ~[\r\n\u2028\u2029]+; // must be fragment, otherwise would match pretty much every text
QuestionMark: '?'; // Not in Spec
Hash: '#'; // Not in Spec
ColonSlashSlash: '://'; // Not in Spec
Asterisk: '*';
Colon: ':'; // Not in Spec

Mock: 'xxx';
