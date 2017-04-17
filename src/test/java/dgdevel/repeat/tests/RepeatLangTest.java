package dgdevel.repeat.tests;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.Template;
import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;
import dgdevel.repeat.api.exceptions.TemplateExecutionException;
import dgdevel.repeat.predef.resolvers.DirectoryResolver;
import dgdevel.repeat.predef.syntax.RepeatLang;

public class RepeatLangTest extends TestCase {

	public RepeatLangTest() {
		Log.conf();
	}

	@Data
	@AllArgsConstructor
	public static class User {
		private String name;
	}

	public void testScrambledPieces() throws TemplateExecutionException, ResolveException, ParseException, CompilerException {
		Context context = RepeatLang.getContext(new DirectoryResolver("src/test/resources", "template", Charset.forName("UTF-8")));
		Template template = context.get("repeat");
		StringBuilder out = new StringBuilder();
		User[] array = new User[2];
		array[0] = new User("john");
		array[1] = new User("jack");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usersArray", array);
		template.run(params, out);
	}

}
