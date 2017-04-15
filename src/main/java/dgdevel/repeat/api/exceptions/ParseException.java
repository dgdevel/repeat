package dgdevel.repeat.api.exceptions;

public class ParseException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParseException(String reason, String input) {
		super(reason + " starting at:\n" + input);
	}
}
