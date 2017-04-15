package dgdevel.repeat.api;

import dgdevel.repeat.api.exceptions.MissingDependenciesException;

/**
 * syntax extension point
 */
public interface TokenHandler {

	public String prefix();

	public String suffix();

	public boolean accept(String content) throws MissingDependenciesException;

	public String transform(String content, OutputType outputType);

}
