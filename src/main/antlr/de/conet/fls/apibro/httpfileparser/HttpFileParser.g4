parser grammar HttpFileParser;

options {
    tokenVocab=HttpFileLexer;
}

requestsFile: (RequestSeparator)* request (requestWithSeparator)* (RequestSeparator)*;

requestWithSeparator: (RequestSeparator)+ request;

request: MockRequest; // TODO
