package org.eclipsecon.exercise3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class EclipseConDocumentModel {

	public static abstract class DocumentLine {
		final int line;
		final String text;
		final int charOffset;
		
		protected DocumentLine(int line, int charOffset, String text) {
			this.line = line;
			this.charOffset = charOffset;
			this.text = text;
		}
	}
	
	public static class Route extends DocumentLine {
		final String name;
		
		public Route(int line, int charOffset, String text, String name) {
			super(line, charOffset, text);
			this.name = name;
		}
	}

	public static class VariableDefinition extends DocumentLine {
		final String variableName;
		final String variableValue;
		
		public VariableDefinition(int lineNumber, int charOffset, String text, String variableName, String value) {
			super(lineNumber, charOffset, text);
			this.variableName = variableName;
			variableValue = value;
		}

	}
	
	private final List<DocumentLine> lines = new ArrayList<>();
	private final List<Route> routes = new ArrayList<>();
	private final Map<String, VariableDefinition> variables = new HashMap<>();
	
	public EclipseConDocumentModel(String text) {
		try (
			Reader r = new StringReader(text);
			BufferedReader reader = new BufferedReader(r);
		) {
			String lineText;
			int lineNumber = 0;
			while ((lineText = reader.readLine()) != null) {
				DocumentLine line = null;
				// TODO: languge syntax change
				/*if (line.startsWith("#")) {
					continue;
				}*/
				if (lineText.contains("=")) {
					line = variableDefinition(lineNumber, lineText);
				} else if (!lineText.trim().isEmpty()) {
					Route route = new Route(lineNumber, 0, lineText, resolve(lineText));
					routes.add(route);
					line = route;
				}
				if (line != null) {
					lines.add(line);
				}
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String resolve(String line) {
		for (Entry<String, VariableDefinition> variable : variables.entrySet()) {
			line = line.replace("${" + variable.getKey() + "}", variable.getValue().variableValue);
		}
		return line;
	}

	private VariableDefinition variableDefinition(int lineNumber, String line) {
		String[] segments = line.split("=");
		if (segments.length == 2) {
			VariableDefinition def = new VariableDefinition(lineNumber, 0, line, segments[0], segments[1]);
			variables.put(def.variableName, def);
			return def;
		}
		return null;
	}

	public List<Route> getResolvedRoutes() {
		return Collections.unmodifiableList(this.routes);
	}

	public String getVariable(int lineNumber, int character) {
		Optional<DocumentLine> docLine = this.lines.stream().filter(line -> line.line == lineNumber).findFirst();
		if (!docLine.isPresent()) {
			return null;
		}
		String text = docLine.get().text;
		if (text.contains("=") && character < text.indexOf("=")) {
			return text.split("=")[0];
		}
		int prefix = text.substring(0, character).lastIndexOf("${");
		int suffix = text.indexOf("}", character);
		if (prefix >= 0 && suffix >= 0) {
			return text.substring(prefix + "${".length(), suffix);
		}
		return null;
	}
	
	public Route getRoute(int line) {
		for (Route route : getResolvedRoutes()) {
			if (route.line == line) {
				return route;
			}
		}
		return null;
	}

	public int getDefintionLine(String variable) {
		if (this.variables.containsKey(variable)) {
			return this.variables.get(variable).line;
		}
		return -1;
	}

	public List<DocumentLine> getResolvedLines() {
		return Collections.unmodifiableList(this.lines);
	}

}
