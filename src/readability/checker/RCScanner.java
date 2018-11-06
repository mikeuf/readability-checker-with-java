package readability.checker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RCScanner.java
 * 
 * Scans the text and counts the number of items found, such as
 * words, syllables and sentences.
 */
public class RCScanner {
		
	public Integer countIPs(String text) {
	  final String IP_PATTERN = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		Integer matchCount = 0;
		Pattern p = Pattern.compile(IP_PATTERN);
		Matcher matcher = p.matcher(text);
		while (matcher.find()) {
			++matchCount;
		}
		
		return matchCount;
	}

	public Integer countURLs(String text) {
	  final String URL_PATTERN = "((https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$\\/)";
		Integer matchCount = 0;
		Pattern p = Pattern.compile(URL_PATTERN);
		Matcher matcher = p.matcher(text);
		while (matcher.find()) {
			++matchCount;
		}
		
		return matchCount;
	}

	public Integer countWords(String text) {
	  final String WORD_PATTERN = "(\\b\\s?\\w+-?\'?\\w*\\b)";
		Integer matchCount = 0;
		Pattern p = Pattern.compile(WORD_PATTERN);
		Matcher matcher = p.matcher(text);
		while (matcher.find()) {
			++matchCount;
		}

		return matchCount;
	}

	public Integer countSyllables(String text) {
		final String SYLLABLE_PATTERN = "((?i)(?!ed\\b)([aeiouy]{1,3})(?<!e\\b(?<!le\\b)(?<!the|be|he|me)))";
		Integer matchCount = 0;
		Pattern p = Pattern.compile(SYLLABLE_PATTERN);
		Matcher matcher = p.matcher(text);
		while (matcher.find()) {
			++matchCount;
		}
		return matchCount;
	}

	public Integer countSentences(String text) {
	  final String POST_HTML_STRIP_SENTENCE_PATTERN = "(\\s?(([A-Za-z]{1,2}|[0-9]{1,2})[\\.!?;][\"\"\\']?))";

		Integer matchCount = 0;
		Pattern p = Pattern.compile(POST_HTML_STRIP_SENTENCE_PATTERN);
		Matcher matcher = p.matcher(text);
		while (matcher.find()) {
			++matchCount;
		}
		return matchCount;
	}
}
