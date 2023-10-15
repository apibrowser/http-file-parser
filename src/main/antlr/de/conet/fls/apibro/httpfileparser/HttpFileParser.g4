parser grammar HttpFileParser;

options {
    tokenVocab=HttpFileLexer;
}

// Request Files: Basically
// "requestsFile: requestWithSeparator*"
// would do the trick;
// but then the first request would need a mandator separator above it, which the spec does not enforce.
// so, we add the "request" before that:
// "requestsFile: request requestWithSeparator*"
// But now, Leading and Trailing Separator Lines are not allowed. So add (request-separator)* on both sides:
requestsFile: (RequestSeparator)* request requestWithSeparator* (RequestSeparator)*;

requestWithSeparator: (RequestSeparator)+ request;

request: MockRequest; // TODO
