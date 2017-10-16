package org.eclipsecon.languageserverplugin;

import java.io.IOException;

import org.eclipsecon.exercise1.EclipseConLanguageServer;

public class ConnectionProvider1 extends AbstractConnectionProvider {
	private static final EclipseConLanguageServer TRAVEL_LANGUAGE_SERVER = new EclipseConLanguageServer();
	public ConnectionProvider1() {
		super(TRAVEL_LANGUAGE_SERVER);
	}
	
	@Override
	public void start() throws IOException {
		super.start();
		TRAVEL_LANGUAGE_SERVER.setRemoteProxy(launcher.getRemoteProxy());
	}
}
