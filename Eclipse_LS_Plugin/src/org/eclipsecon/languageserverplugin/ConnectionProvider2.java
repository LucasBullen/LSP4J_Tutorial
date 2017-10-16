package org.eclipsecon.languageserverplugin;

import java.io.IOException;

import org.eclipsecon.exercise2.EclipseConLanguageServer;

public class ConnectionProvider2 extends AbstractConnectionProvider {
	private static final EclipseConLanguageServer TRAVEL_LANGUAGE_SERVER = new EclipseConLanguageServer();
	public ConnectionProvider2() {
		super(TRAVEL_LANGUAGE_SERVER);
	}
	
	@Override
	public void start() throws IOException {
		super.start();
		TRAVEL_LANGUAGE_SERVER.setRemoteProxy(launcher.getRemoteProxy());
	}
}
