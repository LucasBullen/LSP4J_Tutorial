[Back to the table of contents](/README.md#exercises)

# 02 - Creating an Endpoint

> **Learning goal:** Learn how to create an endpoint, in this example the completion-assist endpoint, to receive requests and send back a response.

The endpoint we will be developing in this exercises will return the list of Sessions available at EclipseCon 2017. Endpoints are used by the language server to receive a specific request from the client, and if required, send a response with the intended reaction back. The messages are sent in JSON, but LSP4J is the translator between JSON and Java objects.

## EclipseConLanguageServer.java

When a Language server is initialized, it tells the client what it is capable of doing so that the client knows what requests it should be sending to the server. With LSP4J, this is done within the `initialize(InitializeParams)` function and returns an `InitializeResult` object containing all the capabilities of the server.

Add the completion provider capability to the initialize result so that the client knows the server is able to handle completion requests:
```
res.getCapabilities().setCompletionProvider(new CompletionOptions());
```

## ChamrousseTextDocumentService.java

In the [ChamrousseLanguageServer.java](TODO)'s constructor, you will see an instance of [ChamrousseTextDocumentService.java](TODO) being created. This class contains all the functions that accept requests from the client and form the responses.

We will be working in the `completion` function. This function takes the position that the request for completion was made within the target document, and returns a list of `CompletionItem`s or a `CompletionList`.

The code below takes a stream of all the sessions at EclipseCon, creates `CompletionItem`s for each, then collects them into a list to be returned. This code is added in replacement of the current `return null;` line in the `completion` function in the solution LS:
```
return CompletableFuture.supplyAsync(() -> Either.forLeft(ChamrousseMap.INSTANCE.all.stream()
	.map(word -> {
		CompletionItem item = new CompletionItem();
		item.setLabel(word);
		item.setInsertText(word);
		return item;
	}).collect(Collectors.toList())));
```

To show to yourself that this works, instead of the above, use the following code to return a list of completion suggestions that you create:
```
List<CompletionItem> completionItems = new ArrayList<>();

completionItems.add(new CompletionItem("First Suggestion"));
completionItems.add(new CompletionItem("Second Suggestion"));
completionItems.add(new CompletionItem("Third Suggestion"));

return CompletableFuture.completedFuture(Either.forLeft(completionItems));
```
Here we return a completed future as we do not need to wait for the completion suggestions to be generated. With any responses that require some time to compile, futures are used to asynchronously create the results. That way the client does not have to freeze up waiting for responses from the server.

## Server in Action

With our new made completion assist endpoint, let's see it in action:
 - Right clicking on the `org.eclipsecon.languageserverplugin` project, use **Run as > Eclipse Application** to launch
 - Create a new file called `exer2.txt`
 - Open this file with the Generic Editor
 - User **Ctrl-Space** to request auto-complete suggestions and see the suggestions you created.

### Congratulations! You have created an endpoint for a Language Server in Java!

[To next lesson](TODO)