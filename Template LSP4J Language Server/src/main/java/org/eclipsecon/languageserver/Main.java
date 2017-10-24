package org.eclipsecon.languageserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		startServer(System.in, System.out);
	}

	public static void startServer(InputStream in, OutputStream out) throws InterruptedException, ExecutionException {
		TemplateLanguageServer server = new TemplateLanguageServer();
		Launcher<LanguageClient> l = LSPLauncher.createServerLauncher(server, in, out);
		Future<?> startListening = l.startListening();
		server.setRemoteProxy(l.getRemoteProxy());
		startListening.get();
	}

}
