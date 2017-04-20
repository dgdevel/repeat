package dgdevel.repeat.impl.textutils;

import java.io.Writer;
import java.util.UUID;

import dgdevel.repeat.api.OutputType;

public class Strings {

	public static String lpad(String input, String pad, int len) {
		while (input.length() < len) {
			input = pad + input;
		}
		return input;
	}

	public static String toJavaString(String input) {
		StringBuilder out = new StringBuilder();
		int length = input.length();
		int c;
		for (int i = 0; i < length; i += Character.charCount(c)) {
		   c = input.codePointAt(i);
	        if (c == '\n') {
				out.append("\\n");
			} else if (c == '\r') {
				out.append("\\r");
			} else if (c == '"' || c == '\'' || c == '\\') {
				out.append("\\").append((char)c);
			} else if ((Character.isSupplementaryCodePoint(c))) {
				out.append(Character.toChars(c));
			} else {
				out.append((char)c);
			}
		}
		return "\"" + out.toString() + "\"";
	}

	public static String randomIdent() {
		return "ID_" + UUID.randomUUID().toString().replace("-", "_");
	}

	public static String randomClassName() {
		return "C_" + UUID.randomUUID().toString().replace("-", "_");
	}

	public static String getPrintInstruction(OutputType outyputType, String content) {
		StringBuilder src = new StringBuilder();
		switch (outyputType) {
		case JAVA_LANG_STRINGBUILDER:
			src.append("out.append(" + content + ");\n");
			break;
		case JAVA_IO_WRITER:
			src.append("out.append(\"\"+" + content + ");\n");
			break;
		}
		return src.toString();
	}

	public static void xmlsafe(Object o, StringBuilder out) throws Exception {
		if (o == null) {
			xmlsafe("null", out);
			return;
		}
		String input = o.toString();
		int length = input.length();
		int c;
		for (int i = 0; i < length; i += Character.charCount(c)) {
		   c = input.codePointAt(i);
		   if (c < 32 || c > 126 || c == '&' || c == '<' || c == '>' || c == '\'' || c == '"') {
			   out.append(String.format("&#x%x;", c));
		   } else {
			   out.append((char)c);
		   }
		}
	}

	public static void xmlsafe(boolean b, StringBuilder out) { out.append(b); }
	public static void xmlsafe(byte b, StringBuilder out) { out.append(b); }
	public static void xmlsafe(short s, StringBuilder out) { out.append(s); }
	public static void xmlsafe(int i, StringBuilder out) { out.append(i); }
	public static void xmlsafe(long l, StringBuilder out) { out.append(l); }
	public static void xmlsafe(float f, StringBuilder out) { out.append(f); }
	public static void xmlsafe(double d, StringBuilder out) { out.append(d); }
	public static void xmlsafe(char c, StringBuilder out) throws Exception {
		xmlsafe(""+c, out);
	}

	public static void xmlsafe(Object o, Writer out) throws Exception {
		if (o == null) {
			xmlsafe("null", out);
			return;
		}
		String input = o.toString();
		int length = input.length();
		int c;
		StringBuilder sb = new StringBuilder((int) (input.length() * 1.2));
		for (int i = 0; i < length; i += Character.charCount(c)) {
		   c = input.codePointAt(i);
		   if (c < 32 || c > 126 || c == '&' || c == '<' || c == '>' || c == '\'' || c == '"') {
			   sb.append(String.format("&#x%x;", c));
		   } else {
			   sb.append((char)c);
		   }
		}
		out.append(sb);
	}
}
