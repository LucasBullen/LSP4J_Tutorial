package org.eclipsecon.exercise3;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.MarkedString;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.SymbolKind;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipsecon.exercise3.EclipseConMap;
import org.eclipsecon.exercise3.EclipseConDocumentModel.Route;
import org.eclipsecon.exercise3.EclipseConDocumentModel.VariableDefinition;

public class EclipseConTextDocumentService implements TextDocumentService {

	private final Map<String, EclipseConDocumentModel> docs = Collections.synchronizedMap(new HashMap<>());
	private final EclipseConLanguageServer eclipseConLanguageServer;

	public EclipseConTextDocumentService(EclipseConLanguageServer eclipseConLanguageServer) {
		this.eclipseConLanguageServer = eclipseConLanguageServer;
	}

	@Override
	public CompletableFuture<Hover> hover(TextDocumentPositionParams position) {
		/* Replace the following return statement with one that returns the hover information */
		return null;
	}

	private Either<String, MarkedString> getHoverContent(String difficulty) {
		return Either.forLeft(difficulty);
	}

	private Either<String, MarkedString> getCustomHoverContent(String difficulty) {
		/* Replace the following return statement with one that returns a custom addition to the hover */
		return null;
	}

	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		/*Create a EclipseCon Document Model from the text document and save it in the document map below*/

	}

	@Override
	public void didChange(DidChangeTextDocumentParams params) {
		/*Create a EclipseCon Document Model from the text document and save it in the document map below*/

	}

	@Override
	public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(
			TextDocumentPositionParams position) {
		return CompletableFuture.supplyAsync(() -> Either.forLeft(EclipseConMap.INSTANCE.all.stream()
				.map(word -> {
					CompletionItem item = new CompletionItem();
					item.setLabel(word);
					item.setInsertText(word);
					return item;
				}).collect(Collectors.toList())));
	}

	@Override
	public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
		return null;
	}

	@Override
	public CompletableFuture<SignatureHelp> signatureHelp(TextDocumentPositionParams position) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends Location>> definition(TextDocumentPositionParams position) {
		return CompletableFuture.supplyAsync(() -> {
			EclipseConDocumentModel doc = docs.get(position.getTextDocument().getUri());
			String variable = doc.getVariable(position.getPosition().getLine(), position.getPosition().getCharacter()); 
			if (variable != null) {
				int variableLine = doc.getDefintionLine(variable);
				if (variableLine == -1) {
					return Collections.emptyList();
				}
				Location location = new Location(position.getTextDocument().getUri(), new Range(
					new Position(variableLine, 0),
					new Position(variableLine, variable.length())
					));
				return Collections.singletonList(location);
			}
			return null;
		});
	}

	@Override
	public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
		return CompletableFuture.supplyAsync(() -> {
			EclipseConDocumentModel doc = docs.get(params.getTextDocument().getUri());
			String variable = doc.getVariable(params.getPosition().getLine(), params.getPosition().getCharacter()); 
			if (variable != null) {
				return doc.getResolvedRoutes().stream()
					.filter(route -> route.text.contains("${" + variable + "}") || route.text.startsWith(variable + "="))
					.map(route -> new Location(params.getTextDocument().getUri(), new Range(
						new Position(route.line, route.text.indexOf(variable)),
						new Position(route.line, route.text.indexOf(variable) + variable.length())
					)))
					.collect(Collectors.toList());
			}
			String routeName = doc.getResolvedRoutes().stream()
					.filter(route -> route.line == params.getPosition().getLine())
					.collect(Collectors.toList())
					.get(0)
					.name;
			return doc.getResolvedRoutes().stream()
					.filter(route -> route.name.equals(routeName))
					.map(route -> new Location(params.getTextDocument().getUri(), new Range(
							new Position(route.line, 0),
							new Position(route.line, route.text.length()))))
					.collect(Collectors.toList());
		});
	}

	@Override
	public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(TextDocumentPositionParams position) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends SymbolInformation>> documentSymbol(DocumentSymbolParams params) {
		return CompletableFuture.supplyAsync(() -> 
			docs.get(params.getTextDocument().getUri()).getResolvedLines().stream().map(line -> {
				SymbolInformation symbol = new SymbolInformation();
				symbol.setLocation(new Location(params.getTextDocument().getUri(), new Range(
						new Position(line.line, line.charOffset),
						new Position(line.line, line.charOffset + line.text.length()))));
				if (line instanceof VariableDefinition) {
					symbol.setKind(SymbolKind.Variable);
					symbol.setName(((VariableDefinition) line).variableName);
				} else if (line instanceof Route) {
					symbol.setKind(SymbolKind.String);
					symbol.setName(((Route) line).name);
				}
				return symbol;
			}).collect(Collectors.toList())
		);
	}

	@Override
	public CompletableFuture<List<? extends Command>> codeAction(CodeActionParams params) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
		return null;
	}

	@Override
	public CompletableFuture<CodeLens> resolveCodeLens(CodeLens unresolved) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
		return null;
	}

	@Override
	public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
		return null;
	}

	@Override
	public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
		return null;
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		this.docs.remove(params.getTextDocument().getUri());
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
	}

}
