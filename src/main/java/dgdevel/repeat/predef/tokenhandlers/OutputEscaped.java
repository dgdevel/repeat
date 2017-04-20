package dgdevel.repeat.predef.tokenhandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import dgdevel.repeat.api.OutputType;
import dgdevel.repeat.api.TokenHandler;
import dgdevel.repeat.api.exceptions.MissingDependenciesException;
import dgdevel.repeat.impl.textutils.Strings;

@Accessors(fluent=true)
@AllArgsConstructor
public class OutputEscaped implements TokenHandler {

	@Getter
	protected String prefix, suffix;

	public boolean accept(String content) throws MissingDependenciesException {
		return true;
	}

	public String transform(String content, OutputType outputType) {
		switch (outputType) {
		case JAVA_LANG_STRINGBUILDER:
		case JAVA_IO_WRITER:
			return Strings.class.getName() + ".xmlsafe(" + content + ", out);\n";
		}
		throw new IllegalArgumentException("Unsupported output type: "+outputType);
	}
}
