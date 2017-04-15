package dgdevel.repeat.predef.resolvers;

import lombok.AllArgsConstructor;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.exceptions.ResolveException;

@AllArgsConstructor
public class StringResolver implements Resolver {

	private String name, source;

	public String resolve(String name) throws ResolveException {
		if (name.equals(this.name)) {
			return source;
		}
		throw new ResolveException(new Exception("Not resolve: " + name));
	}
}
