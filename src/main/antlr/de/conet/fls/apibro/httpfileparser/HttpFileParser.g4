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
requestLine: (method RequiredWhiteSpace)? requestTarget (RequiredWhiteSpace httpVersion)?;
method: HttpMethod; // Diff to Spec: Defined as Token
httpVersion: HttpVersion; // Diff to Spec: Defined as Token

//
// -> 3.2.1.1 Request targethh
requestTarget: originForm | absoluteForm | asteriskForm;

originForm: absolutePath (QuestionMark query)? (Hash uriFragment)?; // Diff to Spec: QuestionMark and Hash as Tokens;

absoluteForm: (scheme ColonSlashSlash)? hierPart (QuestionMark query)? (Hash uriFragment)?;
scheme: HttpScheme; // Diff to Spec: Defined as Token
hierPart: authority absolutePath?;

asteriskForm: Asterisk; // Diff to Spec: Defined as Token

//
// -> 3.2.1.2. Authority

authority: host port?; // Diff to Spec: pulled ':' into token, otherwise sth like '8080' is recognized as ipv4OrRegName
port: PortDefinition;

host: ipv6Address | ipv4OrRegName; // pulled brackets '[' and ']' into ipv6Address;

ipv6Address: UrlIpv6Address;

ipv4OrRegName: Ipv4OrRegName+; // Diff to Spec: Defined as Token


query: Mock; // TODO
uriFragment: Mock; // Diff to Spec: cannot name the rule "fragment" since this is a reserved word in ANTLR4 TODO
absolutePath: Mock; // TODO

headers: Mock; // TODO
messageBody: Mock; // TODO
responseHandler: Mock; // TODO
responseRef: Mock; // TODO
