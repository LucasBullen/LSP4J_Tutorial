package org.eclipsecon.languageserverplugin;

import java.io.IOException;

import org.eclipsecon.solution.ChamrousseLanguageServer;

public class ConnectionProviderSolution extends AbstractConnectionProvider {
	private static final ChamrousseLanguageServer TRAVEL_LANGUAGE_SERVER = new ChamrousseLanguageServer();
	public ConnectionProviderSolution() {
		super(TRAVEL_LANGUAGE_SERVER);
	}
	
	@Override
	public void start() throws IOException {
		super.start();
		TRAVEL_LANGUAGE_SERVER.setRemoteProxy(launcher.getRemoteProxy());
	}
}
