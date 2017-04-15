package dgdevel.repeat.predef.resolvers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.exceptions.ResolveException;

@AllArgsConstructor
public class ClassLoaderResolver implements Resolver {

	private ClassLoader classLoader;
	private String basePackage, extension;
	private Charset charset;

	public String resolve(String name) throws ResolveException {
		try {
			String basePath = basePackage != null
				? basePackage.replace(".", "/") + "/"
				: "";
			String resource = basePath + name + "." + extension;
			@Cleanup InputStream in =
				(classLoader != null
					? classLoader
					: Thread.currentThread().getContextClassLoader())
				.getResourceAsStream(resource);
			if (in == null) throw new IOException("Resource not found: " + resource);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[4096];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			return new String(out.toByteArray(), charset);
		} catch (IOException e) {
			throw new ResolveException(e);
		}
	}
}
