package dgdevel.repeat.impl;

import java.io.Writer;
import java.util.Map;

import lombok.experimental.Accessors;
import dgdevel.repeat.api.Template;
import dgdevel.repeat.api.exceptions.TemplateExecutionException;

@Accessors(fluent=true)
public class BaseTemplate implements Template {

	public String source() {
		return null;
	}

	public String javaSourceStringBuilder() {
		return null;
	}

	public String javaSourceWriter() {
		return null;
	}

	public void run(Map<String, Object> params, StringBuilder out) throws TemplateExecutionException {}

	public void run(Map<String, Object> params, Writer out) throws TemplateExecutionException {}

	public static Template instance;

	public static void instance(Template i) {
		instance = i;
	}
}
