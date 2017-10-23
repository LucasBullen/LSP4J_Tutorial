[Back to the table of contents](/README.md#exercises)

# 03 - Consuming the Document

> **Learning goal:** Learn how to use the client's document to form responses from endpoints.

The endpoint we will be developing in this exercises will be the `hover` endpoint and we will be using the `didOpen` and `didChange` notifications to keep an updated model of the document on the server.

> **NOTE:** File validation and error checking is removed from this exercise's language server to increase clarity of what we need to add.

## EclipseConTextDocumentService.java

When a file is opened or edited `didOpen` and `didChange` notifications are sent to the server respectfully. These are different from the other endpoints, such as the `completion` endpoint used in the second exercise, as they do not send back a response, instead the notification is used to update the responses that the other endpoints will send.

### Consume the Document

In the `didOpen` function, We will be using the text from the text document given to us to create a model. We create a model instead of just saving the text for simplicity and convenience when accessing the data in other functions. We then save the model in a map of all the server's documents with the document URI as the key. **One Language Server is used for multiple documents** so it is important to be able to access the right document model when requested:
```
EclipseConDocumentModel model = new EclipseConDocumentModel(params.getTextDocument().getText());
this.docs.put(params.getTextDocument().getUri(), model);
```

Similarly, In the `didChange` function, create a new model from the document text and update the document map with the new model:
```
EclipseConDocumentModel model = new EclipseConDocumentModel(params.getContentChanges().get(0).getText());
this.docs.put(params.getTextDocument().getUri(), model);
```

### Creating Location Dependent Hover

In the `hover` function, we will be generating a response that changes based on where it is within the document and what information is there.

The code below when put in the `hover` function takes the document model for the current text document, filters through the session present in the document and finds the one on the same line as the hover request. Hover content is then created dependent on this session, containing the session difficulty, and returned:
```
return CompletableFuture.supplyAsync(() -> {
	EclipseConDocumentModel doc = docs.get(position.getTextDocument().getUri());
	Hover res = new Hover();
	res.setContents(doc.getResolvedRoutes().stream()
		.filter(route -> route.line == position.getPosition().getLine())
		.map(route -> route.name)
		.map(EclipseConMap.INSTANCE.type::get)
		.map(this::getHoverContent)
		.collect(Collectors.toList()));
	return res;
});
```

To show to yourself that this works, within the `getHoverContent` function, return text whose color reflects their difficulty:
```
if ("Beginner".equals(difficulty)) {
	return Either.forLeft("<font color='green'>Beginner</font>");
} else if ("Intermediate".equals(difficulty)) {
	return Either.forLeft("<font color='blue'>Intermediate</font>");
} else if ("Advanced".equals(difficulty)) {
	return Either.forLeft("<font color='red'>Advanced</font>");
}
return Either.forLeft(difficulty);
```

And then within the `hover` function replace `.map(this::getHoverContent)` with `.map(this::getCustomHoverContent)`.

## Server in Action

With our new made hover endpoint, let's see the language server in action:
 - Right clicking on the `org.eclipsecon.languageserverplugin` project, use **Run as > Eclipse Application** to launch
 - Create a new file called `exer3.txt`
 - Open this file with the Generic Editor
 - User **Ctrl-Space** to request auto-complete suggestions and add a new Session
 - Hover your cursor over the Session and see its difficulty in color

### Congratulations! You have consumed a document and used it to customize responses from a Language Server in Java!

[To next lesson](/Exercises/4/4-README.md)
