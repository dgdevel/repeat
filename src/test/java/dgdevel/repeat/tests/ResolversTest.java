package dgdevel.repeat.tests;

import java.nio.charset.Charset;

import dgdevel.repeat.impl.DefaultContext;
import dgdevel.repeat.predef.resolvers.CascadeResolver;
import dgdevel.repeat.predef.resolvers.ClassLoaderResolver;
import dgdevel.repeat.predef.resolvers.DirectoryResolver;
import dgdevel.repeat.predef.resolvers.StringResolver;
import junit.framework.TestCase;

public class ResolversTest extends TestCase {

	public void testStringResolver() throws Exception {
		new DefaultContext()
			.resolver(new StringResolver("name", "value"))
			.get("name")
			.run(null, new StringBuilder());
	}

	public void testDirectoryResolver() throws Exception {
		new DefaultContext()
		.resolver(new DirectoryResolver("src/test/resources", "template", Charset.forName("UTF-8")))
		.get("file")
		.run(null, new StringBuilder());
	}

	public void testClassLoaderResolver() throws Exception {
		new DefaultContext()
		.resolver(new ClassLoaderResolver(null, null, "template", Charset.forName("UTF-8")))
		.get("file")
		.run(null, new StringBuilder());
	}

	public void testCascadeResolver() throws Exception {
		new DefaultContext()
		.resolver(new CascadeResolver(
			new DirectoryResolver(".", "template", Charset.forName("UTF-8")),
			new ClassLoaderResolver(null, "", "template", Charset.forName("UTF-8")),
			new StringResolver("name", "value")
		))
		.get("name")
		.run(null, new StringBuilder());
	}
}
