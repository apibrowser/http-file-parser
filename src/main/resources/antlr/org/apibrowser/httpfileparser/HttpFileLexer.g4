//
// Lexer for JetBrains Http File Format.
// Spec available under:
// https://github.com/JetBrains/http-request-in-editor-spec/blob/master/spec.md
// Chapter 2 Lexical Structure
//
lexer grammar HttpFileLexer;

RequestSeparatorTag: '###'; // diff to spec: inlined "line-tail" token
RequestSeparator: RequestSeparatorTag (InputCharacter)* NewLine; // diff to spec: inlined "line-tail" token
//LineTail: (InputCharacter)* NewLine; This is way too greedy to put it in a lexer
//fragment LineTail: (InputCharacter)* NewLine;

// Diff to spec: this must be individual Tokens
GET: 'GET';
HEAD: 'HEAD';
POST: 'POST';
PUT: 'PUT';
DELETE: 'DELETE';
CONNECT: 'CONNECT';
PATCH: 'PATCH';
OPTIONS: 'OPTIONS';
TRACE: 'TRACE';

HttpVersion: 'HTTP/' Digit+ '.' Digit+;
HttpVersionNewLine: 'HTTP/' Digit+ '.' Digit+;

fragment WhiteSpace: (' ' | '\t');
WhiteSpaces: (' ' | '\t')+;
NewLine: ('\n' | '\r' | '\r\n');

HttpScheme: 'http' | 'https';

Digit: ('0'..'9');
fragment HexDigit : [0-9a-fA-F];  // A single hexadecimal digi
fragment Alpha: ('a'..'z') | ('A'..'Z');

QuestionMark: '?'; // Not in Spec
Hash: '#'; // Not in Spec
ColonSlashSlash: '://'; // Not in Spec
Asterisk: '*';
Colon: ':'; // Not in Spec
Slash: '/';
BracketLeft: '[';
BracketRight: ']';
LowerThan: '<';
GreaterThan: '>';
ResponseReferenceTag: '<>' | '<>' WhiteSpace+;
ResponseHandlerTag: '>' WhiteSpace+;
ResponseHandlerScriptStart: '{%';
ResponseHandlerScriptEnd: '%}';

InputCharacter: ~[\r\n\u2028\u2029];

Mock: 'xxxMOCKxxx';
