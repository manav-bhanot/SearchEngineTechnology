package com.csulb.set.homeworks;

public class CheckGrams {

	public static void main(String[] args) {
		
		String word = "repercussion";
		
		String kGramString = '$' + word + '$';

		// Put the word in all the corresponding k-grams
		for (int i = 0; i < word.length(); i++) {

			String oneGram = word.charAt(i) + "";
			System.out.println(oneGram);

			String twoGram = kGramString.substring(i, i + 2);
			System.out.println(twoGram);

			String threeGram = kGramString.substring(i, i + 3);
			System.out.println(threeGram);
		}
		
		String twoGram = kGramString.substring(kGramString.length() - 2);
		System.out.println(twoGram);
	}

}
