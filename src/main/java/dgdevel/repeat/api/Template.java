package dgdevel.repeat.api;

import java.io.Writer;
import java.util.Map;

import dgdevel.repeat.api.exceptions.TemplateExecutionException;

/**
 * Instance of a compiled template, it contains string-form
 * of the sources, and will write its output with the run
 * methods
 */
public interface Template {

	public String source();

	public String javaSourceStringBuilder();

	public String javaSourceWriter();

	public void run(Map<String, Object> params, StringBuilder out) throws TemplateExecutionException;

	public void run(Map<String, Object> params, Writer out) throws TemplateExecutionException;

}
