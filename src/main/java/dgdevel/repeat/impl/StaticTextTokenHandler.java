package dgdevel.repeat.impl;

import dgdevel.repeat.api.OutputType;
import dgdevel.repeat.api.TokenHandler;
import dgdevel.repeat.impl.textutils.Strings;

public class StaticTextTokenHandler implements TokenHandler {

	public String prefix() {
		return null;
	}

	public String suffix() {
		return null;
	}

	public boolean accept(String content) {
		return true;
	}

	public String transform(String content, OutputType outputType) {
		return Strings.getPrintInstruction(outputType, Strings.toJavaString(content));
	}

	public String[] dependencies(String content) {
		return null;
	}
}
