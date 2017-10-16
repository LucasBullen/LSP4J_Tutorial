# Build Language Servers in Java

> A tutorial for EclipseCon Europe 2017

## Contents
1. [Learning Goals](#learning-goals)
2. [Before We Begin](#before-we-begin)
3. [Introduction](#introduction)
4. [Exercises](#exercises)
5. [Resources](#resources)
6. [Next Steps](#next-steps)

***

## Learning Goals

- Become familiar with LSP4J
- Learn how to:
	- Setup a LS with LSP4J
	- Create an endpoint
	- Consume workspace files
	- Perform validation

***

## Before We Begin

### Required programs

We will be using [The Eclipse IDE](https://www.eclipse.org/home/index.php) to work through the tutorial. Eclipse is not mandatory for lsp4j, but included in this repository is an Eclipse Plugin to allow testing your language server.

### Exercise Preparation

This is an interactive tutorial, if you are viewing this on GitHub, download the project now to be able to follow along with the exercise. Within Eclipse use **File > Open Projects from File System** to import the required projects by setting the Import source to the `LSP4J_Tutorial` directory by using the **Directory...** button

 - [LSP4J_Tutorial/Eclipse_LS_Plugin](/Eclipse_LS_Plugin/)
 - [LSP4J_Tutorial/Exercises/solution](/Exercises/solution)
 - [LSP4J_Tutorial/Exercises/1](/Exercises/1)
 - [LSP4J_Tutorial/Exercises/2](/Exercises/2)
 - [LSP4J_Tutorial/Exercises/3](/Exercises/3)
 - [LSP4J_Tutorial/Exercises/4](/Exercises/4)

Ignore the `LSP4J_Tutorial/Exercises/X/src` and `LSP4J_Tutorial/Exercises/X/src/org/eclipsecon/exerciseX` projects.

***

## Introduction

This tutorial will not go over the Language Server Protocol itself, if you are unaware of what the LSP is, refer to its [GitHub page](https://github.com/Microsoft/language-server-protocol) or read the actual [protocol](https://github.com/Microsoft/language-server-protocol/blob/master/protocol.md).

[LSP4J](https://github.com/eclipse/lsp4j) (Language Server Protocol for Java) is an Eclipse project which provides Java binding for the [Language Server Protocol](https://github.com/Microsoft/language-server-protocol). The LSP is based on an extended version of [JSON RPC v2.0](http://www.jsonrpc.org/specification), for which LSP4J provides a Java implementation. Therefore, we are able to use it to create a language server without having to deal with the JSON specifics of the LSP and instead create endpoints for which we are given parameters from the client and return the required actions in object form based on which message our server receives.

In this tutorial, we are using [LSP4E](https://projects.eclipse.org/projects/technology.lsp4e) (Language Server Protocol for Eclipse) to consume and interact with our language server, however, as the value of language servers, the server you develop within this tutorial can be used by any IDE.

***

## Exercises

Each exercise is contained in the `org.eclipsecon.lsp4jtutorial.exercises` package and each is its own Language Server that can be tested on it's own file type:

- [1 - Setting up LSP4J](/Exercises/1/1-README.md)
- [2 - Creating an Endpoint](/Exercises/2/2-README.md)
- [3 - Consuming the Document](/Exercises/3/3-README.md)
- [4 - Sending Notifications](/Exercises/4/4-README.md)

***

## Resources

- [LSP](https://github.com/Microsoft/language-server-protocol)
- [LSP4J](https://github.com/eclipse/lsp4j)
- [LSP4E](https://projects.eclipse.org/projects/technology.lsp4e)
- [Eclipse IDE](https://www.eclipse.org/downloads/)

***

## Next Steps

The Language Servers used in the previous exercises are all set up to be Eclipse plugins. In this repo there is a [template Language Server written in Java using LSP4E](/Template-LSP4J-Language-Server/) made to be compiled to a jar and ran from any IDE. To set it up, follow the instructions found in its [README.md](/Template-LSP4J-Language-Server/README.md) file and begin work on your next LSP project!

Other things that you can do to continue your LS involvment:

- Work on an existing LS or LS client
	- Here is a [list of existing language servers and clients](http://langserver.org/) (Bottom of page) as most are open to PRs

- Develop your own
	- There are tons more of file types and languages that currently do not have a LS, you can change that!

- Develop an Eclipse LS plug-in
	- Such as [aCute](https://github.com/eclipse/aCute) (which is always looking for new committers), make a plug-in to add IDE specific features to compliment a LS

> :fork_and_knife: Feel free to fork this repo and run your own tutorial!

### Following this tutorial at EclipseCon 2017?

![Sign in and evaluate this session at eclipsecon.org][leaveFeedback.png]
