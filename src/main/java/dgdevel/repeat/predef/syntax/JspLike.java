package dgdevel.repeat.predef.syntax;

import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.impl.DefaultContext;
import dgdevel.repeat.predef.tokenhandlers.Include;
import dgdevel.repeat.predef.tokenhandlers.JavaCode;
import dgdevel.repeat.predef.tokenhandlers.OutputEscaped;
import dgdevel.repeat.predef.tokenhandlers.OutputRaw;

public class JspLike {

	public static Context getBasicContext(Resolver resolver) {
		return new DefaultContext()
			.handlers(
				new OutputRaw("<%=", "%>"),
				new JavaCode("<%", "%>")
			)
			.resolver(resolver);
	}

	public static Context getExtendedContext(Resolver resolver) {
		Context context = new DefaultContext();
		return context.handlers(
				new OutputEscaped("<%-", "%>"),
				new OutputRaw("<%=", "%>"),
				new Include("<%<", "%>", context),
				new JavaCode("<%", "%>")
			)
			.resolver(resolver);
	}

}
