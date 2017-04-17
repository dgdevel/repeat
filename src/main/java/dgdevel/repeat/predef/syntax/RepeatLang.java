package dgdevel.repeat.predef.syntax;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.experimental.Accessors;
import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.OutputType;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.TokenHandler;
import dgdevel.repeat.api.exceptions.MissingDependenciesException;
import dgdevel.repeat.impl.DefaultContext;
import dgdevel.repeat.impl.textutils.Strings;
import dgdevel.repeat.predef.tokenhandlers.Include;
import dgdevel.repeat.predef.tokenhandlers.JavaCode;
import dgdevel.repeat.predef.tokenhandlers.OutputEscaped;
import dgdevel.repeat.predef.tokenhandlers.OutputRaw;

public class RepeatLang {

	public static Context getContext(Resolver resolver) {
		Context context = new DefaultContext();
		context.handlers(
			// <declare type name expression >
			new Declare(),
			// <getp type name param>
			new Getp(),
			// <setp name expression >
			new Setp(),
			// <if expression >
			new If(),
			// <elseif expression >
			new ElseIf(),
			// <else>
			new Else(),
			// <end>
			new End(),
			// <eq name1 name2>
			new Eq(),
			// <loopc type name index expression >
			new LoopC(),
			// <loopa type name index expression >
			new LoopA(),
			// <include tmplName [param=param]*>
			new Include("<include ", ">", context),
			// <includex tmplName [param=expression]* >
			new Includex(context),
			// <print expression >
			new OutputEscaped("<print ", " >"),
			// <print unsafe expression >
			new OutputRaw("<print unsafe ", " >"),
			// <java>code</java>
			new JavaCode("<java>", "</java>")
		);
		return context.resolver(resolver);
	}

	@Accessors(fluent=true) private static class Declare implements TokenHandler {
		@Getter private String prefix = "<declare ", suffix = " >";
		private static Pattern pattern = Pattern.compile("^([^ ]+)[ ]+([^ ]+)[ ]+(.*)$");
		public boolean accept(String content) throws MissingDependenciesException {
			content = content.trim();
			return pattern.matcher(content).find();
		}
		public String transform(String content, OutputType outputType) {
			Matcher matcher = pattern.matcher(content.trim());
			matcher.find();
			String type = matcher.group(1);
			String name = matcher.group(2);
			String expression = matcher.group(3);
			return type + " " + name + " = (" + type + ") " + expression + ";\n";
		}
	}

	@Accessors(fluent=true) private static class Getp implements TokenHandler {
		@Getter private String prefix = "<getp ", suffix = ">";
		private static Pattern pattern = Pattern.compile("^([^ ]+)[ ]+([^ ]+)[ ]+([^ ]+)$");
		public boolean accept(String content) throws MissingDependenciesException {
			content = content.trim();
			return pattern.matcher(content).find();
		}
		public String transform(String content, OutputType outputType) {
			Matcher matcher = pattern.matcher(content.trim());
			matcher.find();
			String type = matcher.group(1);
			String name = matcher.group(2);
			String param = matcher.group(3);
			return type + " " + name + " = (" + type + ") params.get(" + Strings.toJavaString(param) + ");\n";
		}
	}

	@Accessors(fluent=true) private static class Setp implements TokenHandler {
		@Getter private String prefix = "<setp ", suffix = " >";
		private static Pattern pattern = Pattern.compile("^([^ ]+)[ ]+(.*)$");
		public boolean accept(String content) throws MissingDependenciesException {
			content = content.trim();
			return pattern.matcher(content).find();
		}
		public String transform(String content, OutputType outputType) {
			Matcher matcher = pattern.matcher(content.trim());
			matcher.find();
			String name = matcher.group(1);
			String expr = matcher.group(2);
			return "params.put(" + Strings.toJavaString(name) + ", " + expr + ");\n";
		}
	}

	@Accessors(fluent=true) private static class If implements TokenHandler {
		@Getter private String prefix = "<if ", suffix = " >";
		public boolean accept(String content) throws MissingDependenciesException {
			return true;
		}
		public String transform(String content, OutputType outputType) {
			return "if (" + content + ") {\n";
		}
	}

	private static class ElseIf extends If {
		public ElseIf() {
			super();
			super.prefix = "<elseif ";
		}
		public String transform(String content, OutputType outputType) {
			return "} else if (" + content + ") {\n";
		}
	}

	@Accessors(fluent=true) private static class Else implements TokenHandler {
		@Getter private String prefix = "<else", suffix = ">";
		public boolean accept(String content) throws MissingDependenciesException {
			return content.isEmpty();
		}
		public String transform(String content, OutputType outputType) {
			return "} else {\n";
		}
	}

	@Accessors(fluent=true) private static class End implements TokenHandler {
		@Getter private String prefix = "<end", suffix = ">";
		public boolean accept(String content) throws MissingDependenciesException {
			return content.isEmpty();
		}
		public String transform(String content, OutputType outputType) {
			return "}\n";
		}
	}

	@Accessors(fluent=true) private static class Eq implements TokenHandler {
		@Getter private String prefix = "<eq ", suffix = ">";
		private static Pattern pattern = Pattern.compile("^([^ ]+)[ ]+([^ ]+)$");
		public boolean accept(String content) throws MissingDependenciesException {
			content = content.trim();
			return pattern.matcher(content).find();
		}
		public String transform(String content, OutputType outputType) {
			Matcher matcher = pattern.matcher(content.trim());
			matcher.find();
			String obj1 = matcher.group(1);
			String obj2 = matcher.group(2);
			return "if ((" + obj1 + " == null && " + obj2 + " == null) ||"
					+ " (" + obj1 + " != null && " + obj1 + ".equals(" + obj2 + "))) {\n";
		}
	}

	@Accessors(fluent=true) private static class LoopC implements TokenHandler {
		@Getter private String prefix = "<loopc ", suffix = " >";
		private static Pattern pattern = Pattern.compile("^([^ ]+)[ ]+([^ ]+)[ ]+([^ ]+)[ ]+(.*)$");
		public boolean accept(String content) throws MissingDependenciesException {
			content = content.trim();
			return pattern.matcher(content).find();
		}
		public String transform(String content, OutputType outputType) {
			Matcher matcher = pattern.matcher(content.trim());
			matcher.find();
			String type = matcher.group(1);
			String name = matcher.group(2);
			String index = matcher.group(3);
			String expr = matcher.group(4);
			String iteratorId = Strings.randomIdent();
			return "int " + index + " = -1;"
				+ "for (java.util.Iterator " + iteratorId + " = (" + expr + ").iterator(); "
				+ iteratorId + ".hasNext();) {\n"
				+ type + " " + name + " = (" + type + ") " + iteratorId + ".next();\n"
				+ index + "++;\n";
		}
	}

	private static class LoopA extends LoopC {
		public LoopA() {
			super();
			super.prefix = "<loopa ";
		}
		public String transform(String content, OutputType outputType) {
			@SuppressWarnings("static-access")
			Matcher matcher = super.pattern.matcher(content.trim());
			matcher.find();
			String type = matcher.group(1);
			String name = matcher.group(2);
			String index = matcher.group(3);
			String expr = matcher.group(4);
			String arrayId = Strings.randomIdent();
			return type + "[] " + arrayId + " = (" + expr + ");\n" +
				"for (int " + index + " = 0; " + index + " < " + arrayId + ".length; " + index + "++) {\n" +
				type + " " + name + " = " + arrayId + "[" + index + "];\n";
		}
	}

	private static class Includex extends Include {
		public Includex(Context context) {
			super("<includex ", " >", context);
		}
		@Override
		public String transform(String content, OutputType outputType) {
			String[] parts = content.split("\\s+");
			parts = cleanupParts(parts);
			String subTemplateId = parts[0];
			String subParamsId = Strings.randomIdent();
			StringBuilder source = new StringBuilder();
			source.append(Map.class.getName()).append(" ").append(subParamsId)
				.append(" = new ").append(HashMap.class.getName()).append("();\n");
			for (int i = 1; i < parts.length; i++) {
				Matcher matcher = detailPattern.matcher(parts[i]);
				matcher.find();
				String id = matcher.group(1);
				String v = matcher.group(3);
				source.append(subParamsId).append(".put(").append(Strings.toJavaString(id)).append(", ")
					.append(v).append(");\n");
			}
			source.append(context.getGeneratedClassName(subTemplateId)).append(".instance.run(").append(subParamsId).append(", out);\n");
			return source.toString();
		}
	}
}
