package com.csulb.set.homeworks.hw4;

import java.util.Scanner;
import java.util.regex.*;

public class PorterStemmer {

	// a single consonant
	private static final String c = "[^aeiou]";
	// a single vowel
	private static final String v = "[aeiouy]";

	// a sequence of consonants; the second/third/etc consonant cannot be 'y'
	private static final String C = c + "[^aeiouy]*";
	// a sequence of vowels; the second/third/etc cannot be 'y'
	private static final String V = v + "[aeiou]*";

	// this regex pattern tests if the token has measure > 0 [at least one VC].
	private static final Pattern mGr0 = Pattern.compile("^(" + C + ")?" + V + C);

	// add more Pattern variables for the following patterns:
	// m equals 1: token has measure == 1
	private static final Pattern mEq1 = Pattern.compile("^(" + C + ")?" + "(" + V + C + "){1}");

	// m greater than 1: token has measure > 1
	private static final Pattern mGr1 = Pattern.compile("^(" + C + ")?" + "(" + V + C + "){2,}");

	// vowel: token has a vowel after the first (optional) C
	private static final Pattern vowelAfterTheFirstOptionalConsonant = Pattern.compile("^(" + C + ")?" + V + "[" + V + C + "]*");

	// double consonant: token ends in two consonants that are the same,
	// unless they are L, S, or Z. (look up "backreferencing" to help
	// with this)
	private static final Pattern doubleConsonants = Pattern.compile("^(" + C + ")?" + "[" + V + C + "]*" + "([^lszaeiou])\\2$");

	// m equals 1, cvc: token is in Cvc form, where the last c is not w, x,
	// or y.
	private static final Pattern cvcForm = Pattern.compile("^(" + C + ")?" + "[" + V + C + "]*" + "(" + c + v + "[^aeiouwxy])$");
	
	private static final String[][] step2Pairs = {new String[] {"ational", "ate"},
			new String[] {"tional", "tion"},
			new String[] {"enci", "ence"},
			new String[] {"anci", "ance"},
			new String[] {"izer", "ize"},
			new String[] {"abli", "able"},
			new String[] {"alli", "al"},
			new String[] {"entli", "ent"},
			new String[] {"eli", "e"},
			new String[] {"ousli", "ous"},
			new String[] {"ization", "ize"},
			new String[] {"ation", "ate"},
			new String[] {"ator", "ate"},
			new String[] {"alism", "al"},
			new String[] {"iveness", "ive"},
			new String[] {"fulness", "ful"},
			new String[] {"ousness", "ous"},
			new String[] {"aliti", "al"},
			new String[] {"iviti", "ive"},
			new String[] {"biliti", "ble"}};
	
	private static final String[][] step3Pairs = {new String[] {"icate", "ic"},
			new String[] {"ative", ""},
			new String[] {"alize", "al"},
			new String[] {"icite", "ic"},
			new String[] {"ical", "ic"},
			new String[] {"ful", ""},
			new String[] {"ness", ""}};
	
	private static final String[] step4suffixes = { "al", "ance", "ence", "er", "ic", "able", "ible", "ant", "ement",
			"ment", "ent", "ion", "ou", "ism", "ate", "iti", "ous", "ive", "ize" };
	
	public static void main(String[] args) {
		
		String word = "";

		while (true) {
			Scanner read = new Scanner(System.in);
			System.out.println("Enter a term to stem : ");
			word = read.next();
			
			if (word.equalsIgnoreCase("quit")) {
				System.out.println("BBye!");
				System.exit(0);
			}
			// System.out.println(processToken(word));
		}
	}

	public static String processToken(String token) {
		if (token.length() < 3) {
			return token; // token must be at least 3 chars
		}
		// step 1a
		// program the other steps in 1a.
		// note that Step 1a.3 implies that there is only a single 's' as the
		// suffix; ss does not count. you may need a regex pattern here for
		// "not s followed by s".
		if (token.endsWith("sses") || token.endsWith("ies")) {
			token = token.substring(0, token.length() - 2);
		} else if (token.endsWith("ss")) {
			// do nothing, the token remains same
		} else if (token.endsWith("s")) {
			token = token.substring(0, token.length() - 1);
		}
		
		// System.out.println("Token after Step 1a : "+token);

		// step 1b
		boolean doStep1bb = false;
		// step 1b
		if (token.endsWith("eed")) { // 1b.1
			// token.substring(0, token.length() - 3) is the stem prior to
			// "eed".
			// if that has m>0, then remove the "d".
			String stem = token.substring(0, token.length() - 3);
			if (mGr0.matcher(stem).find()) { // if the pattern matches the stem
				token = stem + "ee";
			}
		}
		// program the rest of 1b. set the boolean doStep1bb to true if Step 1b*
		// should be performed.
		else if (token.endsWith("ed")) {
			String stem = token.substring(0, token.length() - 2);
			if (vowelAfterTheFirstOptionalConsonant.matcher(stem).find()) {
				token = stem;
				doStep1bb = true;
			}
		} else if (token.endsWith("ing")) {
			String stem = token.substring(0, token.length() - 3);
			if (vowelAfterTheFirstOptionalConsonant.matcher(stem).find()) {
				token = stem;
				doStep1bb = true;
			}
		}
		
		// System.out.println("Token after Step 1b : "+token);

		// step 1b*, only if the 1b.2 or 1b.3 were performed.
		if (doStep1bb) {
			//System.out.println(doubleConsonants.matcher(token).find());
			if (token.endsWith("at") || token.endsWith("bl") || token.endsWith("iz")) {
				token = token + "e";
			}
			// use the regex patterns you wrote for 1b*.4 and 1b*.5
			else if (doubleConsonants.matcher(token).find()) {
				token = token.substring(0, token.length() - 1);
			} else if (mEq1.matcher(token).find() && cvcForm.matcher(token).find()) {
				token = token + "e";
			}
			// System.out.println("Token after Step 1bb : "+token);
		}

		// step 1c
		// program this step. test the suffix of 'y' first, then test the
		// condition *v* on the stem.
		if (token.endsWith("y")) {
			String stem = token.substring(0, token.length() - 1);
			if (vowelAfterTheFirstOptionalConsonant.matcher(stem).find()) {
				token = stem + "i";
			}
		}
		
		// System.out.println("Token after Step 1c : "+token);

		// step 2
		// program this step. for each suffix, see if the token ends in the
		// suffix.
		// * if it does, extract the stem, and do NOT test any other suffix.
		// * take the stem and make sure it has m > 0.
		// * if it does, complete the step and do not test any others.
		// if it does not, attempt the next suffix.

		// you may want to write a helper method for this. a matrix of
		// "suffix"/"replacement" pairs might be helpful. It could look like
		// string[][] step2pairs = { new string[] {"ational", "ate"},
		// new string[] {"tional", "tion"}, ....
		
		for (String[] suffix : step2Pairs) {
			if (token.endsWith(suffix[0])) {
				String stem = token.substring(0, token.length() - suffix[0].length());				
				if (mGr0.matcher(stem).find()) {
					token = stem + suffix[1];
					break;
				}				
			}
		}
		
		// System.out.println("Token after Step 2 : "+token);

		// step 3
		// program this step. the rules are identical to step 2 and you can use
		// the same helper method. you may also want a matrix here.
		for (String[] suffix : step3Pairs) {
			if (token.endsWith(suffix[0])) {
				String stem = token.substring(0, token.length() - suffix[0].length());				
				if (mGr0.matcher(stem).find()) {
					token = stem + suffix[1];
					break;
				}				
			}
		}
		
		// System.out.println("Token after Step 3 : "+token);

		// step 4
		// program this step similar to step 2/3, except now the stem must have
		// measure > 1.
		// note that ION should only be removed if the suffix is SION or TION,
		// which would leave the S or T.
		// as before, if one suffix matches, do not try any others even if the
		// stem does not have measure > 1.
		for (String[] suffix : step3Pairs) {			
			if (token.endsWith(suffix[0])) {
				String stem = token.substring(0, token.length() - suffix[0].length());	
				if (!(suffix[0].equals("ion") && (stem.charAt(token.length()-1) == 's' || stem.charAt(token.length()-1) == 't'))) {
					continue;
				}
				if (mGr1.matcher(stem).find()) {
					token = stem;
					break;
				}
			}
		}
		
		// System.out.println("Token after Step 4 : "+token);

		// step 5
		// program this step. you have a regex for m=1 and for "Cvc", which
		// you can use to see if m=1 and NOT Cvc.
		// all your code should change the variable token, which represents
		// the stemmed term for the token.
		
		// Step 5a
		if (token.endsWith("e")) {
			String stem = token.substring(0, token.length() - 1);
			if (mGr1.matcher(stem).find() || (mEq1.matcher(stem).find() && !cvcForm.matcher(stem).find())) {
				token = stem;
			}
		}
		
		// System.out.println("Token after Step 5a : "+token);
		
		// Step 5b
		if (mEq1.matcher(token).find() &&  doubleConsonants.matcher(token).find() && token.endsWith("l")) {
			token = token.substring(0, token.length() - 1);
		}
		
		// System.out.println("Token after Step 5b : "+token);
		
		return token;
	}
}
