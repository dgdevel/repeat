package dgdevel.repeat.tests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;
import dgdevel.repeat.api.exceptions.TemplateExecutionException;
import dgdevel.repeat.predef.resolvers.CascadeResolver;
import dgdevel.repeat.predef.resolvers.StringResolver;
import dgdevel.repeat.predef.syntax.MustacheLike;


public class MustacheLikeTest extends TestCase {

	public MustacheLikeTest() {
		Log.conf();
	}

	public void testBasicSyntax() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		String input = "{{ escaped }} {{{ not_escaped }}} {{> included }}";
		String input2 = "{{ param }}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("param", "ok");
		params.put("escaped", "&");
		params.put("not_escaped", "&");
		StringBuilder out = new StringBuilder();
		MustacheLike.getBasicContext(new CascadeResolver(
			new StringResolver("test", input),
			new StringResolver("included", input2)
		)).get("test")
			.run(params, out);
		assertEquals("&#x26; & ok", out.toString());
	}

	public void testExtendedSyntax() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		String input = "{{ escaped }} {{{ not_escaped }}} {{> included }} {{# int i = 0 }}";
		String input2 = "{{ param }}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("param", "ok");
		params.put("escaped", "&");
		params.put("not_escaped", "&");
		StringBuilder out = new StringBuilder();
		MustacheLike.getExtendedContext(new CascadeResolver(
			new StringResolver("test", input),
			new StringResolver("included", input2)
		)).get("test")
			.run(params, out);
		assertEquals("&#x26; & ok ", out.toString());
	}
}
