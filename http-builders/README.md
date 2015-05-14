# HTTP Builders

Useful builders and related utilities to access the features of the `HttpClient` in more reusable and fluent manner.

## `Request`

Light wrapper for building HTTP requests in similar fashion as Jersey HTTP client.

```java
// build the request
Request request = new Request(httpClient, 
            "http://www.example.org", 
            ImmutableMultimap.of("header", "value"), 
            ImmutableMultimap.of("param", "value"), 
            null);
// call a method which matches the HTTP request method you want to execute            
// remember to use try-with-resources
try (Request request : request.get()) {
    Response response = request.get();
    // and so on and so forth
}
```

## `RequestBuilder`

Build `Request` instances and reuse the builder.

```java
// request list of numbers from example API 
RequestBuilder listOfNumbers = new RequestBuilder(httpClient)
        .withUrl("http://example.org/numbers");
        
try (Response numbersResponse : listOfNumbers.build().get()) {
    List<Integer> numbers = deserialize(numbersRequest.getResponseBody());
    // for each number reuse original builder with extended URL path and added parameter
    // note that the params do not stack as each builder instance is unique
    RequestBuilder translateNumber = listOfNumbers.withUrl("http://example.org/translate"
    for (Integer n : numbers) {
        Request numberRequest = translateNumber
                .withParams(params -> params.put("n", n.toString())
                .build();
        // this will HTTP GET http://example.org/translate?n={n}
        try (Response numberResponse : numberRequest.get()) {
            // and so on and so forth
        }
    }
}
```