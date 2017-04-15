package dgdevel.repeat.api;

import dgdevel.repeat.api.exceptions.ResolveException;

/**
 * It will retrive the source of a template for a given name
 */
public interface Resolver {

	public String resolve(String name) throws ResolveException;

}
