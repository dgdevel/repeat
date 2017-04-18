package dgdevel.repeat.predef.resolvers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import lombok.AllArgsConstructor;
import dgdevel.repeat.api.Resolver;
import dgdevel.repeat.api.exceptions.ResolveException;

@AllArgsConstructor
public class DirectoryResolver implements Resolver {

	private String base, extension;
	private Charset charset;

	public String resolve(String name) throws ResolveException {
		FileInputStream in = null;
		try {
			String filepath = base + File.separator + name + "." + extension;
			in = new FileInputStream(filepath);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[4096];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			return new String(out.toByteArray(), charset);
		} catch (IOException e) {
			throw new ResolveException(e);
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				// we already had an exception or we completely read the file
			}
		}
	}
}
