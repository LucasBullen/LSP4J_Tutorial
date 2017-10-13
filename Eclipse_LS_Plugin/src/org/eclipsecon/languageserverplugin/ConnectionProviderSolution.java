package org.eclipsecon.languageserverplugin;

import java.io.IOException;

import org.eclipsecon.solution.EclipseConLanguageServer;

public class ConnectionProviderSolution extends AbstractConnectionProvider {
	private static final EclipseConLanguageServer TRAVEL_LANGUAGE_SERVER = new EclipseConLanguageServer();
	public ConnectionProviderSolution() {
		super(TRAVEL_LANGUAGE_SERVER);
	}
	
	@Override
	public void start() throws IOException {
		super.start();
		TRAVEL_LANGUAGE_SERVER.setRemoteProxy(launcher.getRemoteProxy());
	}
}
