//
// Parser Grammar for the JetBrains Http File Format.
// Spec available under
// https://github.com/JetBrains/http-request-in-editor-spec/blob/master/spec.md
// Chapter 3 Grammar Structure
//
parser grammar HttpFileParser;

options {
    tokenVocab=HttpFileLexer;
}


//requiredWhiteSpace: WhiteSpace+; // Diff to Spec: Parser rule instead of token
//newLineWithIndent: NewLine requiredWhiteSpace; // Diff to Spec: Parser rule instead of token

//
// Part 3.1 Requests file
//

// Requests File: Basically
// "requestsFile: requestWithSeparator*"
// would do the trick;
// but then the first request would need a mandatory separator above it, which the spec does not enforce.
// So, we add the "request" before that:
// "requestsFile: request requestWithSeparator*"
// But now, Leading and Trailing Separator Lines are not allowed. So add (request-separator)* on both sides:
requestsFile: (RequestSeparator)* request requestWithSeparator* (RequestSeparator)*;

requestWithSeparator: (RequestSeparator)+ request;

//
// 3.2 Request
//
request: (requestLine NewLine) (headers NewLine)? messageBody? responseHandler? responseRef?; // TODO remove ()? of (headers NewLine)?

//
// 3.2.1 Request line
//
httpVersion: HttpVersion; // Diff to Spec: Defined as Token + Must be defined before requestLine for precedence
requestLine: (method WhiteSpaces)? requestTarget (WhiteSpaces httpVersion)?;
method: GET
     | HEAD
     | POST
     | PUT
     | DELETE
     | CONNECT
     | PATCH
     | OPTIONS
     | TRACE; // Diff to Spec: Defined as Token

//
// -> 3.2.1.1 Request targethh
requestTarget: (originForm | absoluteForm | asteriskForm);

originForm: absolutePath (QuestionMark query)? (Hash uriFragment)?; // Diff to Spec: QuestionMark and Hash as Tokens;

absoluteForm: (scheme ColonSlashSlash)? hierPart (QuestionMark query)? (Hash uriFragment)?;
scheme: HttpScheme; // Diff to Spec: Defined as Token
hierPart: authority absolutePath?;

asteriskForm: Asterisk; // Diff to Spec: Defined as Token

//
// -> 3.2.1.2. Authority

authority: host port?; // Diff to Spec: pulled ':' into token, otherwise sth like '8080' is recognized as ipv4OrRegName
port: Colon Digit+;

host: ipv6Address | ipv4OrRegName; // pulled brackets '[' and ']' into ipv6Address;

ipv6Address: BracketLeft ~(NewLine | BracketRight | Slash)+ BracketRight; // (any inputCharacter except ‘/’ and ‘]’)+

// Spec: (any inputCharacter except ‘/’, ‘:’, ‘?’ and ‘#’)+
// Diff to spec: Do not allow WhiteSpaces, since that might be ambiguous with HttpVersion
// (is http://foo.com HTTP/1.1 the
// domain "foo.com HTTP/1.1" or the domain "foo.com" in Http Version 1.1?)
// TODO: maybe find a way to match HttpVersion only on end of line as Http Version, but as part of otherwise
ipv4OrRegName: ~(NewLine | Slash | Colon | QuestionMark | Hash | WhiteSpaces)+;

//
// -> 3.2.1.3. Resource path

absolutePath: Slash | (pathSeparator segment)+;
pathSeparator: Slash | NewLineWithIndent;
segment: InputCharacter* ;

query: Mock; // TODO
uriFragment: Mock; // Diff to Spec: cannot name the rule "fragment" since this is a reserved word in ANTLR4 TODO

headers: Mock; // TODO
messageBody: Mock; // TODO
responseHandler: Mock; // TODO
responseRef: Mock; // TODO
