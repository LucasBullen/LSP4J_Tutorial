[Back to the table of contents](/README.md#exercises)

# 01 - Setting up LSP4J

> **Learning goal:** Learn how to lay the foundation of a Java Language Server.

The language server we will be developing through these exercises is the EclipseCon LS. It is able to help create an itinerary for what sessions to attend at EclipseCon 2017. This LS is built off of Mickael Istria's [Le LanguageServer de Chamrousse](https://github.com/mickaelistria/eclipse-languageserver-demo) used in a different LSP4J tutorial.

In this exercise, we will complete the Exercise1 Project with the required connections and foundation steps to create a Language Server.

> **NOTE:** When you first open the project there will be errors; this is OK. Once you complete the exercise they will be gone.

## MANIFEST.MF

Start by opening the [MANIFEST.MF](/Exercises/1/META-INF/MANIFEST.MF) file. Here we will add the dependencies of the project and the exported package; which will be the LS for our plug-ins to consume.

### Dependencies

Either in the **Dependencies** tab or with the **Require-Bundle** tag, add the following two packages:

**`org.eclipse.lsp4j`** Version 0.3.0

This package includes the interface for implementations of the LSP, `lsp4j.services.LanguageServer`, and all the request, parameter, and response formats for the messages sent between the server and client.

**`org.eclipse.lsp4j.jsonrpc`** Version 0.3.0

We use only one class from this package: `Launcher` which is the entry point for applications that use LSP4J. A `Launcher` does all the wiring that is necessary to connect your endpoint via an input stream and an output stream.

### Exports

To allow other packages to use our Language Server, we must export the package. This is done either within the **Runtime** tab or with the **Export-Package** tag by adding `org.eclipsecon.exercise1` to be exported.

## Main.java

Within [Main.java](/Exercises/1/src/org/eclipsecon/exercise1/Main.java), we will be creating an instance of the server, launching it with the program's standard input and output to receive and send messages, set the server's client, and begin waiting for messages.

In the `startServer` function, do the following:

Create an instance of the Language Server, which implements `lsp4j.services.LanguageServer`:
```
EclipseConLanguageServer server = new EclipseConLanguageServer();
```

Use the `Launcher` to start the server with the program's standard input and output:
```
Launcher<LanguageClient> l = LSPLauncher.createServerLauncher(server, in, out);
Future<?> startListening = l.startListening();
```

Set the server's client, this is used when publishing diagnostics to the client:
```
server.setRemoteProxy(l.getRemoteProxy());
```

And have the server process wait until the `Launcher` stops listening:
```
startListening.get();
```

## Server in Action

Now that the EclipseCon 2017 Language Server has been setup (We will go over how the LS is built in the next exercises) we can run it and see what it is capable of.
 - Right clicking on the `org.eclipsecon.languageserverplugin` project, use **Run as > Eclipse Application** to launch
 - Create a new file called `exer1.txt`
 - Open this file with the Generic Editor
 - User **Ctrl-Space** to request auto-complete suggestions and see the sessions available.

**Other Notable Features Available**
 - Hover on the Session to see it's difficulty
 - Put Sessions on following lines for validation of order at EclipseCon 2017

### Congratulations! You have set up a Language Server in Java!

[To next lesson](/Exercises/2/2-README.md)