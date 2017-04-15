package dgdevel.repeat.examples.mustachelike;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.Template;
import dgdevel.repeat.predef.resolvers.ClassLoaderResolver;
import dgdevel.repeat.predef.syntax.MustacheLike;

public class Main {
	public static void main(String[] args) throws Exception {
		Resolver resolver = new ClassLoaderResolver(
			// a classloader can be passed, if null
			// the context class loader will be used
			Thread.currentThread().getContextClassLoader(),
			// the base package to search for templates
			"dgdevel.repeat.examples.mustachelike",
			// the template extension
			"template",
			// the file charset
			Charset.forName("UTF-8")
		);
		// get a predefined context, with loaded syntax
		Context context = MustacheLike.getExtendedContext(resolver);
		// get or compile the named template
		Template template = context.get("master");
		// run the template
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "earth & moon");
		StringBuilder out = new StringBuilder();
		template.run(params, out);
		// resulting output
		System.out.println(out);
	}
}
