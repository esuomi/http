# HTTP client wrapper

Whether it's Jersey, Apache HttpClient 3.x or 4, Ning or any of the *n+1* HTTP clients already available for the JVM,
they all share the following two traits:

 1. They are all very powerful and provide all the bells and whistles one could ever need for HTTP client connectivity
 2. Almost none of them provide a good baseline abstraction for both easy usage and mocking of said library

## The reasoning

While an argument exists that HTTP client should be hidden behind a
[Gateway](http://martinfowler.com/eaaCatalog/gateway.html) and used as is I have through experience found that being
able to hotswap HTTP implementation itself has allowed me to reuse integration tests as system tests while still using
the actual Gateway object. I have found this immensely valuable as this proves the Gateway object behaves correctly with
the rest of the code base while keeping especially the tests snappy.

## How to use

The meat of this wrapper is the `HttpClient` interface which provides the commonly used HTTP 1.1 methods as standard
Java methods. Going line by line below is the most idiomatic use for this wrapper:

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

As you can see, no magic is involved. Headers aren't automatically set, parameters are not automatically converted from
various object types to Strings *(you don't need to encode them though - implementations take care of that)* and in
general there isn't a ton of builders nor other classes involved in the request itself.