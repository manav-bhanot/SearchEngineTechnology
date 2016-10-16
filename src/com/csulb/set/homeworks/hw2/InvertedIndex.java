/**
 * 
 */
package com.csulb.set.homeworks.hw2;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * @author Manav
 *
 */
public class InvertedIndex {

	Map<String, Collection<Integer>> invertedIndexMap = new HashMap<String, Collection<Integer>>();

	// To print the evenly spaced results
	int longestWord;
	int longestFileName;

	/**
	 * Indexes all .txt files in the specified directory. First builds a
	 * dictionary of all terms in those files, then builds a boolean
	 * term-document matrix as the index.
	 * 
	 * @param directory
	 *            the Path of the directory to index.
	 */
	public void indexDirectory(final Path directory) {
		
		// will need a data structure to store all the terms in the document
		// HashSet: a hashtable structure with constant-time insertion; does not
		// allow duplicate entries; stores entries in unsorted order.

		try {
			// go through each .txt file in the working directory
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					// make sure we only process the current working directory
					if (directory.equals(dir)) {
						return FileVisitResult.CONTINUE;
					}
					return FileVisitResult.SKIP_SUBTREE;
				}

				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					// only process .txt files
					if (file.toString().endsWith(".txt")) {

						// stores the length of the longest file name
						longestFileName = Math.max(longestFileName, file.getFileName().toString().length());

						Scanner scan = new Scanner(file);
						while (scan.hasNext()) {
							// \\W replaces any character that is not a word.
							// i.e which does not lie between [a-zA-Z_0-9]
							String word = scan.next().toLowerCase().replaceAll("\\W", "");
							if (word.length() > 0) {

								// Stores the length of the longest word
								longestWord = Math.max(longestWord, word.length());

								if (invertedIndexMap.get(word) == null) {
									invertedIndexMap.put(word, new ArrayList<Integer>());
								}
								//System.out.println(file.getFileName().toString().substring(3, 4));
								invertedIndexMap.get(word).add(Integer.parseInt(
										file.getFileName().toString().substring(3, 4)));
							}
						}
					}
					return FileVisitResult.CONTINUE;
				}

				// don't throw exceptions if files are locked/other errors occur
				public FileVisitResult visitFileFailed(Path file, IOException e) {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (Exception ex) {
		}
	}

	public void printInvertedIndex(Map<String, Collection<Integer>> iiMap) {
		for (String word : iiMap.keySet()) {
			System.out.print(word);
			printSpaces(longestWord - word.length() + 1);
			
			System.out.print(" --> ");
			
			for (Integer doc : iiMap.get(word)) {
				System.out.print("Doc"+doc);
				printSpaces(longestFileName - ("Doc"+doc).length() + 1 - 3);
			}
			System.out.println();
		}
	}

	// prints a bunch of spaces
	private static void printSpaces(int spaces) {
		for (int i = 0; i < spaces; i++) {
			System.out.print(" ");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InvertedIndex index = new InvertedIndex();

		// this gets the current working path.
		final Path currentWorkingPath = Paths
				.get("C:\\Users\\Manav\\Documents\\CECS-529_Search_Engine_Technology\\Homeworks\\hw2\\Excercise1.2")
				.toAbsolutePath();

		// System.out.println(currentWorkingPath);

		index.indexDirectory(currentWorkingPath);

		index.printInvertedIndex(index.invertedIndexMap);
	}

}
