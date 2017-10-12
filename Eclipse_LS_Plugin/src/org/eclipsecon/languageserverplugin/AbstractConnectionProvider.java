package org.eclipsecon.languageserverplugin;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.eclipse.lsp4e.server.StreamConnectionProvider;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

public class AbstractConnectionProvider  implements StreamConnectionProvider {

	private InputStream inputStream  ;
	private OutputStream outputStream;
	private LanguageServer ls;
	protected Launcher<LanguageClient> launcher;
	
	public AbstractConnectionProvider(LanguageServer ls) {
		this.ls = ls;
	}
	
	@Override
	public void start() throws IOException {
		PipedInputStream in = new PipedInputStream();
		PipedOutputStream out = new PipedOutputStream();
		PipedInputStream in2 = new PipedInputStream();
		PipedOutputStream out2 = new PipedOutputStream();
		
		in.connect(out2);
		out.connect(in2);
	
		launcher = LSPLauncher.createServerLauncher(ls, in2, out2);
		inputStream = in;
		outputStream = out;
		launcher.startListening();
	}

	@Override
	public InputStream getInputStream() {
		return new FilterInputStream(inputStream) {
			@Override
			public int read() throws IOException {
				int res = super.read();
				System.err.print((char) res);
				return res;
			}

			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				int bytes = super.read(b, off, len);
				byte[] payload = new byte[bytes];
				System.arraycopy(b, off, payload, 0, bytes);
				System.err.print(new String(payload));
				return bytes;
			}

			@Override
			public int read(byte[] b) throws IOException {
				int bytes = super.read(b);
				byte[] payload = new byte[bytes];
				System.arraycopy(b, 0, payload, 0, bytes);
				System.err.print(new String(payload));
				return bytes;
			}
		};
	}

	@Override
	public OutputStream getOutputStream() {
		return new FilterOutputStream(outputStream) {
			@Override
			public void write(int b) throws IOException {
				System.err.print((char) b);
				super.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				System.err.print(new String(b));
				super.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				byte[] actual = new byte[len];
				System.arraycopy(b, off, actual, 0, len);
				System.err.print(new String(actual));
				super.write(b, off, len);
			}
		};
	}

	@Override
	public void stop() {
	}
	
	/*
	 * 
	 * 
	 * try {
				DidChangeConfigurationParams params = new DidChangeConfigurationParams();
				Map<String, Object> msbuildProjectTools = new HashMap<>();
				List<String> completionsFromProject = new ArrayList<>();
				completionsFromProject.add("Property"); //$NON-NLS-1$
				completionsFromProject.add("ItemType"); //$NON-NLS-1$
				completionsFromProject.add("ItemMetadata"); //$NON-NLS-1$
				completionsFromProject.add("Target"); //$NON-NLS-1$
				completionsFromProject.add("Task"); //$NON-NLS-1$

				List<String> experimentalFeatures = new ArrayList<>();
				experimentalFeatures.add("empty-completion-lists"); //$NON-NLS-1$
				experimentalFeatures.add("expressions"); //$NON-NLS-1$

				Map<String, Object> language = new HashMap<>();
				language.put("enable", true); //$NON-NLS-1$
				language.put("disableHover", false); //$NON-NLS-1$
				language.put("logLevel", "Information"); //$NON-NLS-1$ //$NON-NLS-2$
				language.put("experimentalFeatures", experimentalFeatures); //$NON-NLS-1$
				language.put("completionsFromProject", completionsFromProject); //$NON-NLS-1$
				msbuildProjectTools.put("language", language); //$NON-NLS-1$

				params.setSettings(
						Collections.singletonMap("msbuildProjectTools", //$NON-NLS-1$
								Collections.singletonMap("language", language)));//$NON-NLS-1$

				info.getLanguageClient().getWorkspaceService().didChangeConfiguration(params);

				CompletableFuture<Hover> hover = info.getLanguageClient().getTextDocumentService().hover(LSPEclipseUtils.toTextDocumentPosistionParams(info.getFileUri(), offset, info.getDocument()));
				requests.add(hover.thenAccept(hoverResults::add));
			} catch (BadLocationException e) {
				LanguageServerPlugin.logError(e);
			}
	 */
}