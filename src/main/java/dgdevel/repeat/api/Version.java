package dgdevel.repeat.api;

import java.io.InputStream;
import java.util.Properties;

public class Version {

	public static String getVersion() {
		try {
			InputStream in =
				Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("META-INF/maven/com.github.dgdevel/repeat/pom.properties");
			Properties pom = new Properties();
			pom.load(in);
			return pom.getProperty("version");
		} catch (Exception e) {
			return null;
		}
	}

}
