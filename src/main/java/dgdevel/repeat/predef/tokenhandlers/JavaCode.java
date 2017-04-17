package dgdevel.repeat.predef.tokenhandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import dgdevel.repeat.api.OutputType;
import dgdevel.repeat.api.TokenHandler;
import dgdevel.repeat.api.exceptions.MissingDependenciesException;

@Accessors(fluent=true)
@AllArgsConstructor
public class JavaCode implements TokenHandler {

	@Getter
	protected String prefix, suffix;

	public boolean accept(String content) throws MissingDependenciesException {
		return true;
	}

	public String transform(String content, OutputType outputType) {
		return content + ";\n";
	}
}
