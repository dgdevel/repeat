package dgdevel.repeat.api.exceptions;

public class CompilerException extends Exception {

	private static final long serialVersionUID = 1L;

	public CompilerException(String source, Throwable cause) {
		super("Compilation exception: " + cause.getMessage() + "\nGenerated source was:\n" + source, cause);
	}
}
