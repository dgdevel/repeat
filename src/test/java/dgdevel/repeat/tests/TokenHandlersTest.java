package dgdevel.repeat.tests;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.Template;
import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;
import dgdevel.repeat.api.exceptions.TemplateExecutionException;
import dgdevel.repeat.impl.DefaultContext;
import dgdevel.repeat.predef.resolvers.StringResolver;
import dgdevel.repeat.predef.tokenhandlers.Include;
import dgdevel.repeat.predef.tokenhandlers.IncludeInheritParams;
import dgdevel.repeat.predef.tokenhandlers.JavaCode;
import dgdevel.repeat.predef.tokenhandlers.OutputEscaped;
import dgdevel.repeat.predef.tokenhandlers.OutputEscapedParam;
import dgdevel.repeat.predef.tokenhandlers.OutputRaw;
import dgdevel.repeat.predef.tokenhandlers.OutputRawParam;

public class TokenHandlersTest extends TestCase {

	public TokenHandlersTest() {
		Log.conf();
	}

	public void testOutputHandlers() throws ResolveException, ParseException, CompilerException, TemplateExecutionException {
		Context context = new DefaultContext();
		context.handlers(
				new OutputEscaped("<out-esc>", "</out-esc>"),
				new OutputEscapedParam("<out-esc-param>", "</out-esc-param>"),
				new OutputRaw("<out-raw>", "</out-raw>"),
				new OutputRawParam("<out-raw-param>", "</out-raw-param>")
			);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ascii", "hello world");
		params.put("non-ascii", "h\u00e8llo world");
		params.put("unsafe", "&");
		params.put("surrogate", "\uD83D\uDCA9");

		String[] src = new String[] {
			// expression escaper
			"<out-esc>params.get(\"ascii\")</out-esc>",
			"hello world",
			"<out-esc>params.get(\"non-ascii\")</out-esc>",
			"h&#xe8;llo world",
			"<out-esc>params.get(\"unsafe\")</out-esc>",
			"&#x26;",
			"<out-esc>params.get(\"surrogate\")</out-esc>",
			"&#x1f4a9;",
			// parameter escaper
			"<out-esc-param>ascii</out-esc-param>",
			"hello world",
			"<out-esc-param>  ascii   \t </out-esc-param>",
			"hello world",
			"<out-esc-param>non-ascii</out-esc-param>",
			"h&#xe8;llo world",
			"<out-esc-param>unsafe</out-esc-param>",
			"&#x26;",
			"<out-esc-param>surrogate</out-esc-param>",
			"&#x1f4a9;",
			//expression raw
			"<out-raw>params.get(\"ascii\")</out-raw>",
			"hello world",
			"<out-raw>params.get(\"non-ascii\")</out-raw>",
			"h\u00e8llo world",
			"<out-raw>params.get(\"unsafe\")</out-raw>",
			"&",
			"<out-raw>params.get(\"surrogate\")</out-raw>",
			"\uD83D\uDCA9",
			// parameter raw
			"<out-raw-param>ascii</out-raw-param>",
			"hello world",
			"<out-raw-param>non-ascii</out-raw-param>",
			"h\u00e8llo world",
			"<out-raw-param>unsafe</out-raw-param>",
			"&",
			"<out-raw-param>surrogate</out-raw-param>",
			"\uD83D\uDCA9",
		};
		for (int i = 0; i < src.length; i+=2) {
			String name = String.valueOf(i);
			Template template = context
				.resolver(new StringResolver(name, src[i]))
				.get(name);
			StringBuilder out = new StringBuilder();
			Writer outw = new StringWriter();
			template.run(params, out);
			template.run(params, outw);
			assertEquals(src[i+1], out.toString());
			assertEquals(src[i+1], outw.toString());
		}
	}

	public void testJavaCode() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		StringBuilder out = new StringBuilder();
		new DefaultContext()
			.handlers(new JavaCode("<start>", "</end>"))
			.resolver(new StringResolver("javacode", " test <start> int i = 0 </end> toast"))
			.get("javacode")
			.run(null, out);
		assertEquals(out.toString(), " test  toast");
	}

	public void testIncludes() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("myParam", "myParam");
		params.put("param", "param");
		StringBuilder out = new StringBuilder();
		Context context = new DefaultContext();
		context.handlers(
			new OutputRawParam("<raw>", "</raw>"),
			new Include("<include>", "</include>", context),
			new IncludeInheritParams("<include-inherit>", "</include-inherit>", context))
		.resolvers(
			new StringResolver("child", "<raw> param </raw>"),
			new StringResolver("parent", "<include> child param=myParam </include> <include-inherit> child </include-inherit>")
		).get("parent")
		.run(params, out);
		assertEquals("myParam param", out.toString());
	}

	public void testEdgeCases() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		StringBuilder out = new StringBuilder();
		new DefaultContext().handlers(
			new OutputEscaped("<%", "%>"),
			new OutputRaw("<%=", "%>")
		).resolver(
				new StringResolver("test", "<%= '&' %>")
		).get("test")
			.run(null, out);
		assertEquals("&", out.toString());
	}
}
