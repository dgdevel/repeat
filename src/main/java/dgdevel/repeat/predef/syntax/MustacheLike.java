package dgdevel.repeat.predef.syntax;

import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.impl.DefaultContext;
import dgdevel.repeat.predef.tokenhandlers.IncludeInheritParams;
import dgdevel.repeat.predef.tokenhandlers.JavaCode;
import dgdevel.repeat.predef.tokenhandlers.OutputEscapedParam;
import dgdevel.repeat.predef.tokenhandlers.OutputRawParam;

public class MustacheLike {

	public static Context getBasicContext(Resolver resolver) {
		Context context = new DefaultContext();
		return context
			.handlers(
				new IncludeInheritParams("{{>", "}}", context),
				new OutputRawParam("{{{", "}}}"),
				new OutputEscapedParam("{{", "}}")
			)
			.resolver(resolver);
	}

	public static Context getExtendedContext(Resolver resolver) {
		Context context = new DefaultContext();
		return context
			.handlers(
				new IncludeInheritParams("{{>", "}}", context),
				new OutputRawParam("{{{", "}}}"),
				new JavaCode("{{#", "}}"),
				new OutputEscapedParam("{{", "}}")
			)
			.resolver(resolver);
	}

}
