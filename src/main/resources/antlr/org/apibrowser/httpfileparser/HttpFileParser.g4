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

newLineWithIndent: NewLine WhiteSpaces; // Diff to Spec: Parser rule instead of token

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
requestsFile: (RequestSeparator | NewLine)* request requestWithSeparator* (RequestSeparator | NewLine)*;

requestWithSeparator: (RequestSeparator)+ request;

//
// 3.2 Request
//
request:
    requestLine NewLine
    headers?
    (NewLine bodyAndOrResponseHandlers)?;

bodyAndOrResponseHandlers: // "at least one" of the sequence: messageBody responseHandler responseRef
    messageBody responseHandler? responseRef?
    | responseHandler responseRef?
    | responseRef
    ;
// original spec:
//request:
//    requestLine NewLine
//    headers NewLine // *)
//    messageBody?
//    responseHandler?
//    responseRef?;
// non-empty headers also enforce a linebreak on the end of each headerField, effectively making this NewLine
// a mandatory empty line under the headers:
//      | myheader: leValue\n // linebreak from "headerField" rule
//      | \n // linebreak from "request" rule
// if you skip headers (possible, since "headers" rule allows that):
//      | POST https://example.com \n // linebreak from "request" rule after requestLine
//      | \n                          // empty headers + linebreak from "request" rule after headers
//
// this is actually how .http files behave in IntelliJ - except that a requestSeperator is allowed
//      | POST https://example.com \n
//      | ###                          // not allowed in spec, but works in intelli

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
pathSeparator: Slash | newLineWithIndent;
segment: InputCharacter* ;

//
// -> 3.2.1.4. Query and Fragment

//Spec: "qery: (any input-character except ‘#’)* [new-line-with-indent query]"
// note: input-character excludes NewLine
query: ~(Hash | NewLine)* (newLineWithIndent query)?;

//Spec: "fragment: (any input-character except ‘?’)* [new-line-with-indent fragment]"
// Diff to Spec: cannot name the rule "fragment" since this is a reserved word in ANTLR4
// note: input-character excludes NewLIne
uriFragment: ~(QuestionMark | NewLine)* (newLineWithIndent uriFragment)?;

//
// 3.2.2 Headers
//
headers: (headerField NewLine)+;
headerField: fieldName Colon WhiteSpaces? fieldValue WhiteSpaces?;
fieldName: ~(Colon | NewLine)+; //(any input-character except ‘:’)+ - note: input-character excludes NewLine

// field-value spec: "line-tail [new-line-with-indent field-value]"
// this seems odd, since line-tail contains an enforced linebreak; the mandatory linebreak after a header is already
// defined in "headers" rule.
// Moreover, the NewLineWithIndent addition does not seem to work in .http files executed with idea;
// on this Request, it says 'unknown header "cation/json" and tries to add a header with this name
//      | GET http://example.com/api/   // RequestLIne
//      | Content-Type: appli           // Header -> headervalue = " appli" + NewLineWithIndent
//      |     cation/json               // second part of
//
//fieldValue: ~(NewLine)* NewLine (NewLineWithIndent fieldValue)?; -> nope
fieldValue: ~(NewLine)*;

//
// 3.2.3. Message body
//

messageBody: messages | multipartFormData;

messages: messageLine (NewLine messageLine)*; // spec says ? instead of * but this seems a bug

messageLine:
    // (any input-character except ‘< ’, ’<> ’ and ‘###’) line-tail:
    ( ~(NewLine | LowerThan | ResponseReferenceTag | RequestSeparatorTag) ~(NewLine)*)
     | inputFileRef;

inputFileRef: LowerThan WhiteSpaces filePath;
filePath: ~(NewLine)*;
multipartFormData: Mock;

responseHandler: Mock; // TODO
responseRef: Mock; // TODO
