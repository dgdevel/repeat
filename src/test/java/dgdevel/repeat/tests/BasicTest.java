package dgdevel.repeat.tests;

import java.io.StringWriter;

import junit.framework.TestCase;
import dgdevel.repeat.api.Template;
import dgdevel.repeat.impl.DefaultContext;
import dgdevel.repeat.predef.resolvers.StringResolver;

public class BasicTest extends TestCase {

	public void testCharacterHandling() throws Exception {
		StringBuilder _out = new StringBuilder();
		for (int i = 32; i < 127; i++) {
			_out.append((char)i);
		}
		_out.append("\n");
		for (int i = 256; i < 512; i++) {
			_out.append((char)i);
		}
		_out.append("\n");
		_out.append("\uD83D\uDCA9"); // pile of poo: high + low surrogate
		String src = _out.toString();
		Template template = new DefaultContext()
			.resolver(new StringResolver("name", src))
			.get("name");
//		System.out.println(template.getClass().getName());
//		System.out.println("Source: "+template.source());
//		System.out.println("JavaSource SB: "+template.javaSourceStringBuilder());
//		System.out.println("JavaSource IO: "+template.javaSourceWriter());
//		System.out.println("Expected : "+src);
		StringBuilder out = new StringBuilder();
		template.run(null, out);
//		System.out.println("Output SB: "+out);
		StringWriter outw = new StringWriter();
		template.run(null, outw);
//		System.out.println("Output IO: "+outw);
		assertEquals(_out.toString(), out.toString());
		assertEquals(out.toString(), outw.toString());
	}

}
