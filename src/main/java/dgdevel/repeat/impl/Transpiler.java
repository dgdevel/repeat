package dgdevel.repeat.impl;

import java.util.List;

import lombok.extern.log4j.Log4j;
import dgdevel.repeat.api.OutputType;
import dgdevel.repeat.api.TokenHandler;
import dgdevel.repeat.api.exceptions.MissingDependenciesException;
import dgdevel.repeat.api.exceptions.ParseException;

@Log4j
public class Transpiler {

	public static String javaSource(String input, List<TokenHandler> handlers, OutputType outputType) throws ParseException, MissingDependenciesException {
		TokenHandler current = null;
		StaticTextTokenHandler staticTextHandler = new StaticTextTokenHandler();
		int p;
		StringBuilder output = new StringBuilder();
		output.append("{\n");
		output.append("java.util.Map params = $1 ;\n");
		switch (outputType) {
		case JAVA_IO_WRITER:
			output.append("java.io.Writer out = $2 ;\n");
			break;
		case JAVA_LANG_STRINGBUILDER:
			output.append("java.lang.StringBuilder out = $2 ;\n");
			break;
		}
		while (!input.isEmpty()) {
			if (current == null) {
				// handling static text: search for the closest non-static token
				int min = Integer.MAX_VALUE;
				TokenHandler selected = null;
				for (TokenHandler handler : handlers) {
					if ((p = input.indexOf(handler.prefix())) != -1) {
						if (p < min) {
							min = p;
							selected = handler;
						} else if (p == min) {
							if (selected.prefix().length() < handler.prefix().length()) {
								selected = handler;
							}
						}
					}
				}
				p = min;
				if (p != 0 && p != Integer.MAX_VALUE) {
					output.append(staticTextHandler.transform(input.substring(0, p), outputType));
					input = input.substring(p);
				}
				current = selected;
				// no handler triggered: static text to the end
				if (current == null) {
					output.append(staticTextHandler.transform(input, outputType));
					input = "";
				}
			} else {
				if ((p = input.indexOf(current.suffix())) != -1) {
					String content = input.substring(current.prefix().length(), p);
					if (!current.accept(content)) {
						throw new ParseException("Invalid syntax of type " + current, input);
					}
					output.append(current.transform(content, outputType));
					input = input.substring(p + current.suffix().length());
					current = null;
				} else {
					throw new ParseException("Unterminated sequence of type " + current, input);
				}
			}
		}
		output.append("}");
		log.trace("---- Transpiler.javaSource ----");
		log.trace(output);
		return output.toString();
	}

}
