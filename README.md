# HTTP client wrapper

Whether it's Jersey, Apache HttpClient 3.x or 4, Ning or any of the *n+1* HTTP clients already available for the JVM, they all share two traits:

 1. They are all very powerful and provide all the bells and whistles one could ever need for HTTP client connectivity
 2. Almost none of them provide a good baseline abstraction for easy testing and mocking of said library

While an argument exists that HTTP client wrappers should be hidden behind a [Gateway](http://martinfowler.com/eaaCatalog/gateway.html) I have found that being able to hotswap HTTP implementation itself has allowed me to reuse integration tests as system tests while still using the actual Gateway object. I have found this immensely valuable, not to mention getting a great visibility into the final call values just before they're sent to the underlying client.

So, this wrapper and its implementations are opinionated. Caveat emptor.