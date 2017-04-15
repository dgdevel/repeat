package dgdevel.repeat.predef.resolvers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.exceptions.ResolveException;

@AllArgsConstructor
public class DirectoryResolver implements Resolver {

	private String base, extension;
	private Charset charset;

	public String resolve(String name) throws ResolveException {
		try {
			String filepath = base + File.separator + name + "." + extension;
			@Cleanup FileInputStream in = new FileInputStream(filepath);
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
