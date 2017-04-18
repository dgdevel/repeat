package dgdevel.repeat.predef.tokenhandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.OutputType;
import dgdevel.repeat.api.TokenHandler;
import dgdevel.repeat.api.exceptions.MissingDependenciesException;
import dgdevel.repeat.impl.textutils.Strings;

@Accessors(fluent=true)
@AllArgsConstructor
public class IncludeInheritParams implements TokenHandler {

	@Getter
	protected String prefix, suffix;

	protected Context context;

	protected static final Pattern detailPattern = Pattern.compile("^([^=]+)(=([^=]+))$");

	protected static String[] cleanupParts(String[] input) {
		int counter = 0;
		for (String s : input) {
			if (!s.isEmpty()) {
				counter++;
			}
		}
		String[] clean = new String[counter];
		int index = 0;
		for (String s : input) {
			if (!s.isEmpty()) {
				clean[index++] = s;
			}
		}
		return clean;
	}

	public boolean accept(String candidateContent) throws MissingDependenciesException {
		String[] parts = candidateContent.split("\\s+");
		parts = cleanupParts(parts);
		if (parts.length == 0) return false;
		if (context.getGeneratedClassName(parts[0]) == null) {
			throw new MissingDependenciesException(new String[]{parts[0]});
		}
		for (int i = 1; i < parts.length; i++) {
			String detail = parts[i];
			if (!detailPattern.matcher(detail).find()) return false;
		}
		return true;
	}

	public String transform(String content, OutputType outputType) {
		String[] parts = content.split("\\s+");
		parts = cleanupParts(parts);
		String subTemplateId = parts[0];
		String subParamsId = Strings.randomIdent();
		StringBuilder source = new StringBuilder();
		source.append(Map.class.getName()).append(" ").append(subParamsId)
			.append(" = new ").append(HashMap.class.getName()).append("(params);\n");
		for (int i = 1; i < parts.length; i++) {
			Matcher matcher = detailPattern.matcher(parts[i]);
			matcher.find();
			String id = matcher.group(1);
			String v = matcher.group(3);
			source.append(subParamsId).append(".put(").append(Strings.toJavaString(id)).append(", ")
				.append("params.get(").append(Strings.toJavaString(v)).append("));\n");
		}
		
		source.append(context.getGeneratedClassName(subTemplateId)).append(".instance.run(").append(subParamsId).append(", out);\n");
		return source.toString();
	}
}
