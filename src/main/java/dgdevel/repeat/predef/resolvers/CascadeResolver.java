package dgdevel.repeat.predef.resolvers;

import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.exceptions.ResolveException;

public class CascadeResolver implements Resolver {

	private Resolver[] resolvers;

	public CascadeResolver(Resolver...resolvers) {
		this.resolvers = resolvers;
	}

	public String resolve(String name) throws ResolveException {
		for (Resolver resolver : resolvers) {
			try {
				return resolver.resolve(name);
			} catch (ResolveException e) {}
		}
		throw new ResolveException(new Exception("No suitable resolver"));
	}
}
