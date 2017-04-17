package dgdevel.repeat.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.Template;
import dgdevel.repeat.api.TokenHandler;
import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;
import dgdevel.repeat.predef.resolvers.CascadeResolver;

@Accessors(fluent=true)
public class DefaultContext implements Context {

	private List<TokenHandler> handlers = new ArrayList<TokenHandler>();

	public List<TokenHandler> handlers() {
		return handlers;
	}

	public DefaultContext handlers(TokenHandler...handlers) {
		for (TokenHandler handler : handlers) {
			this.handlers.add(handler);
		}
		return this;
	}

	public DefaultContext handlers(List<TokenHandler> handlers) {
		this.handlers = handlers;
		return this;
	}

	@Getter
	@Setter
	private Resolver resolver;

	public Context resolvers(Resolver... resolvers) {
		return resolver(new CascadeResolver(resolvers));
	}

	private Map<String, String> generatedClassNames = new HashMap<String, String>();

	public String getGeneratedClassName(String templateName) {
		return generatedClassNames.get(templateName);
	}

	public void registerGeneratedClassName(String templateName, String className) {
		synchronized (generatedClassNames) {
			generatedClassNames.put(templateName, className);
		}
	}

	private Map<String, Template> templates = new HashMap<String, Template>();

	public void put(String name, Template template) {
		synchronized (templates) {
			templates.put(name, template);
		}
	}

	private Object compilerLock = new Object();

	public Template get(String name) throws ResolveException, ParseException, CompilerException {
		if (templates.containsKey(name)) {
			return templates.get(name);
		}
		synchronized (compilerLock) {
			if (templates.containsKey(name)) {
				return templates.get(name);
			}
			return Compiler.compile(this, name); // the compiler will call the put	
		}
	}
}
