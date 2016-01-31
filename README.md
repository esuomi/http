# HTTP 1.1 client wrapper

## Overview

`io.induct.http` is a modular HTTP client wrapper which adds an unified API for _any_ actual HTTP client with
testability and DI/IoC in mind.

------

## Quickstart

### Prerequisites

#### 1. Understand HTTP wrapper's modular design

HTTP Wrapper is extremely modularized to allow for its easy inclusion in any project, both new and on-going. These
modules are referenced elsewhere in the docs so it is good to understand at this point what each module _type_ means:

 - *`core`* module contains minimal set of dependencies for the wrapper itself and all utility classes etc.
 - *`builders`* module contains _request_ wrapper and related builder utility classes
 - *`provider`* modules are the actual wrapper implementations
 - *`ioc`* modules contain readymade configurations for dependency injection libraries and frameworks

### Setup

 1. Select modules which provide implementations you need
 2. Add the modules as dependencies. All modules are available through Maven Central.

## Usage

The wrapper provides a Java API for all HTTP methods through [`HttpClient`](http-core/src/main/java/io/induct/http/HttpClient)
interface. The wrapper does not do any hidden header modifications, parameter escapings etc.

```java
// Instantiate any implementation. The instance is reusable.
HttpClient httpClient = ...;
// provide params and headers as multimaps
Multimap<String, String> params = HashMultimap.create();
Multimap<String, String> headers = HashMultimap.create();
// provide request body as stream
InputStream requestBody = new ByteInputStream();
// use try-with-resources to perform HTTP call
try (Response response = httpClient.get("http://www.example.com", params, headers, requestBody)) {
    // status code for the response
    int statusCode = response.getStatusCode();
    // headers returned by the server
    Multimap<String, String> responseHeaders = response.getResponseHeaders();
    // optional response body as stream
    Optional<InputStream> responseBody = response.getResponseBody();
}
```

## Further reading

 - [RFC 2616: Hypertext transfer protocol HTTP/1.1](https://tools.ietf.org/html/rfc2616)
 - about the [design](docs/design.md) of this wrapper

## (Frequently) Asked Questions

### WebSockets

> Where's WebSocket support? I believe WebSockets are awesome and should also be supported by any self respecting HTTP
> client!

You and me both, buddy! WebSockets, however, are _not_ part of HTTP 1.1 itself. The initial handshake/upgrade is handled
with HTTP 1.1, that's true, but after that the domain switches to something completely different. Practical reason for
not supporting WebSockets in this library is that I have not had an use case for myself to actually abstract WebSockets
in same way as HTTP clients. This may change of course but for time being we're going with just the traditional HTTP
stuff.
