# HTTP wrapper's design or _why does this even exist?_

## Preface

Whether it's Jersey, Ning, Apache HttpClient 3.x or any subtly-incompatible subversions of 4 or any of the other *n+1*
HTTP clients already available for the JVM,they all share the following two traits:

 1. They are all very powerful and provide all the bells and whistles one could ever need for HTTP client connectivity
 2. Almost none of them provide a good baseline abstraction for both easy usage and mocking of said library

While an argument exists that HTTP client should be hidden behind a
[Gateway](http://martinfowler.com/eaaCatalog/gateway.html) and used as is I have through experience found that being
able to hotswap HTTP implementation itself has allowed me to reuse integration tests as system tests while still using
the actual Gateway object. I have found this immensely valuable as this proves the Gateway object behaves correctly with
the rest of the code base while keeping especially the tests snappy.

## About the actual API

The API provided by `io.induct.http.HttpClient` mirrors HTTP 1.1 as methods where

 - method parameters are the components of a request
 - method name matches the HTTP 1.1 method name and
 - method return value represents the HTTP response
