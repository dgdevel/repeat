package dgdevel.repeat.tests;

import junit.framework.TestCase;
import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;
import dgdevel.repeat.api.exceptions.TemplateExecutionException;
import dgdevel.repeat.predef.resolvers.CascadeResolver;
import dgdevel.repeat.predef.resolvers.StringResolver;
import dgdevel.repeat.predef.syntax.JspLike;

public class JspLikeTest extends TestCase {

	public JspLikeTest() {
		Log.conf();
	}

	public void testBasicSyntax() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		StringBuilder out = new StringBuilder();
		String input = "static <%= null %><% int i = 0 %>";
		JspLike
			.getBasicContext(new StringResolver("test", input))
			.get("test")
			.run(null, out);
		assertEquals("static null", out.toString());
	}

	public void testExtendedSyntax() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		StringBuilder out = new StringBuilder();
		String input = "static <%= null %><%- '&' %><% int i = 0 %><%< test2 %>";
		String input2 = "ok";
		JspLike
			.getExtendedContext(new CascadeResolver(
				new StringResolver("test", input),
				new StringResolver("test2", input2)
			))
			.get("test")
			.run(null, out);
		assertEquals("static null&#x26;ok", out.toString());
	}

}
