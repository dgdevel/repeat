package dgdevel.repeat.api;

import java.util.List;

import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;

/**
 * Entry point for template instantiation.
 * It will ask to the current resolver to get the source for a given name,
 * and invoke the compilation.
 * Keep references of all the templates generated.
 * Hold handlers.
 */
public interface Context {

	public Resolver resolver();

	public Context resolver(Resolver resolver);

	public List<TokenHandler> handlers();

	public Context handlers(TokenHandler...handlers);

	public Context handlers(List<TokenHandler> handlers);

	public Template get(String name) throws ResolveException, ParseException, CompilerException;

	public void put(String name, Template template);

	public void registerGeneratedClassName(String templateName, String className);

	public String getGeneratedClassName(String templateName);

}
