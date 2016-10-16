package com.csulb.set.homeworks.hw3;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

/**
 * A very simple search engine. Uses an inverted index over a folder of TXT
 * files.
 */
public class SimpleEngine {

	public static void main(String[] args) throws IOException {

		Scanner read = new Scanner(System.in);
		System.out.println("Enter the directory path containing the corpus");

		String userInput = read.next();
		// Do we need to replace the forward or backward slashes in the
		// directory path
		// String userInput = read.next().replaceAll("\\*", "\\");

		final Path currentWorkingPath = Paths.get(userInput).toAbsolutePath();

		// the inverted index
		final NaiveInvertedIndex index = new NaiveInvertedIndex();

		// the list of file names that were processed
		final List<String> fileNames = new ArrayList<String>();

		// This is our standard "walk through all .txt files" code.
		Files.walkFileTree(currentWorkingPath, new SimpleFileVisitor<Path>() {
			int mDocumentID = 0;

			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				// make sure we only process the current working directory
				if (currentWorkingPath.equals(dir)) {
					return FileVisitResult.CONTINUE;
				}
				return FileVisitResult.SKIP_SUBTREE;
			}

			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				// only process .txt files
				if (file.toString().endsWith(".txt")) {
					// we have found a .txt file; add its name to the fileName
					// list,
					// then index the file and increase the document ID counter.
					System.out.println("Indexing file " + file.getFileName());

					fileNames.add(file.getFileName().toString());
					indexFile(file.toFile(), index, mDocumentID);
					mDocumentID++;
				}
				return FileVisitResult.CONTINUE;
			}

			// don't throw exceptions if files are locked/other errors occur
			public FileVisitResult visitFileFailed(Path file, IOException e) {

				return FileVisitResult.CONTINUE;
			}

		});

		printResults(index, fileNames);

		// Implement the same program as in Homework 1: ask the user for a term,
		// retrieve the postings list for that term, and print the names of the
		// documents which contain the term.

		String word = "";

		while (true) {
			Scanner getUserInput = new Scanner(System.in);
			System.out.print("Enter a term to search for : ");
			word = getUserInput.next();

			if (word.equalsIgnoreCase("quit")) {
				System.out.println("\nBBye!");
				break;
			}
			
			System.out.print(word+": ");
			for (int i=0; i < index.getPostings(word).size(); i++) {
				int fileIndex = index.getPostings(word).get(i);
				System.out.print(fileNames.get(fileIndex));
				if (i != index.getPostings(word).size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println("\n");
		}

	}

	/**
	 * Indexes a file by reading a series of tokens from the file, treating each
	 * token as a term, and then adding the given document's ID to the inverted
	 * index for the term.
	 * 
	 * @param file
	 *            a File object for the document to index.
	 * @param index
	 *            the current state of the index for the files that have already
	 *            been processed.
	 * @param docID
	 *            the integer ID of the current document, needed when indexing
	 *            each term from the document.
	 */
	private static void indexFile(File file, NaiveInvertedIndex index, int docID) {
		// TO-DO: finish this method for indexing a particular file.
		// Construct a SimpleTokenStream for the given File.
		// Read each token from the stream and add it to the index.

		try {
			TokenStream tokenStream = new SimpleTokenStream(file);

			while (tokenStream.hasNextToken()) {
				index.addTerm(tokenStream.nextToken(), docID);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printResults(NaiveInvertedIndex index, List<String> fileNames) {

		// TO-DO: print the inverted index.
		// Retrieve the dictionary from the index. (It will already be sorted.)
		// For each term in the dictionary, retrieve the postings list for the
		// term. Use the postings list to print the list of document names that
		// contain the term. (The document ID in a postings list corresponds to
		// an index in the fileNames list.)

		// Print the postings list so they are all left-aligned starting at the
		// same column, one space after the longest of the term lengths.
		// Example:
		//
		// as: document0 document3 document4 document5
		// engines: document1
		// search: document2 document4

		// Finding the length of the longest term which is required to format
		// the output
		int longestTerm = 0;
		for (String term : index.getDictionary()) {
			longestTerm = Math.max(longestTerm, term.length());
		}

		for (String term : index.getDictionary()) {
			System.out.print(term + ":");
			printSpaces(longestTerm - term.length() + 2);

			for (int i = 0; i < index.getPostings(term).size(); i++) {
				int fileIndex = index.getPostings(term).get(i);
				System.out.print(fileNames.get(fileIndex));
				if (i != index.getPostings(term).size() - 1) {
					System.out.print(", ");
				}
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
}
