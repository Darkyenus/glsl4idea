package glslplugin;

/**
 * Various utility functions.
 */
public class Utils {

	private static StringBuilder appendEscape(StringBuilder to, String wholeText, int i, String escapeSequence) {
		StringBuilder sb = to;
		if (sb == null) {
			sb = new StringBuilder(i + (wholeText.length() - i) * 4);
			sb.append(wholeText, 0, i);
		}
		return sb.append('&').append(escapeSequence).append(';');
	}

	/** Escapes given text to be used directly inside HTML. */
	public static String escapeHtml(String text){
		// https://www.w3.org/International/questions/qa-escapes
		StringBuilder escaped = null;
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			switch (c) {
				case '<':
					escaped = appendEscape(escaped, text, i, "lt");
					break;
				case '>':
					escaped = appendEscape(escaped, text, i, "gt");
					break;
				case '&':
					escaped = appendEscape(escaped, text, i, "amp");
					break;
				case '"':
					escaped = appendEscape(escaped, text, i, "quot");
					break;
				case '\'':
					escaped = appendEscape(escaped, text, i, "apos");
					break;
				default:
					if (escaped != null) {
						escaped.append(c);
					}
			}
		}

		if (escaped != null) {
			return escaped.toString();
		}
		return text;
	}

}
