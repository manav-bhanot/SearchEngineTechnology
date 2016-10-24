package com.csulb.set.homeworks.hw5;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.csulb.set.homeworks.hw3.NaiveInvertedIndex;
import com.csulb.set.homeworks.hw3.SimpleTokenStream;
import com.csulb.set.homeworks.hw4.PorterStemmer;

/**
 * Writes an inverted indexing of a directory to disk.
 */
public class IndexWriter {

	private String mFolderPath;

	/**
	 * Constructs an IndexWriter object which is prepared to index the given
	 * folder.
	 */
	public IndexWriter(String folderPath) {
		mFolderPath = folderPath;
	}

	/**
	 * Builds and writes an inverted index to disk. Creates three files:
	 * vocab.bin, containing the vocabulary of the corpus; postings.bin,
	 * containing the postings list of document IDs; vocabTable.bin, containing
	 * a table that maps vocab terms to postings locations
	 */
	public void buildIndex() {
		buildIndexForDirectory(mFolderPath);
	}

	/**
	 * Builds the normal NaiveInvertedIndex for the folder.
	 */
	private static void buildIndexForDirectory(String folder) {
		NaiveInvertedIndex index = new NaiveInvertedIndex();

		// Index the directory using a naive index
		indexFiles(folder, index);

		// at this point, "index" contains the in-memory inverted index
		// now we save the index to disk, building three files: the postings
		// index,
		// the vocabulary list, and the vocabulary table.

		// the array of terms
		String[] dictionary = index.getDictionary();
		// an array of positions in the vocabulary file
		long[] vocabPositions = new long[dictionary.length];

		buildVocabFile(folder, dictionary, vocabPositions);
		buildPostingsFile(folder, index, dictionary, vocabPositions);
	}

	/**
	 * Builds the postings.bin file for the indexed directory, using the given
	 * NaiveInvertedIndex of that directory.
	 */
	private static void buildPostingsFile(String folder, NaiveInvertedIndex index, String[] dictionary,
			long[] vocabPositions) {
		FileOutputStream postingsFile = null;
		try {
			postingsFile = new FileOutputStream(new File(folder, "postings.bin"));

			// simultaneously build the vocabulary table on disk, mapping a term
			// index to a
			// file location in the postings file.
			FileOutputStream vocabTable = new FileOutputStream(new File(folder, "vocabTable.bin"));

			// the first thing we must write to the vocabTable file is the
			// number of vocab terms.
			
			// So we allocated a 4 byte sized buffer to store the length of the size of the dictionary.
			// Are we assuming that the size of the dictionary never exceeds a value greater than Integer.MAX_VALUE
			byte[] tSize = ByteBuffer.allocate(4).putInt(dictionary.length).array();
			
			// Writing the dictionary size as a 4 byte value in the vocab table
			vocabTable.write(tSize, 0, tSize.length);
			
			// Creating an index to get the vocabPosition of this word which wre placed in the vocabPositions array 
			// while converting the dictionary into the vocab.bin file
			int vocabI = 0;
			
			// Processing each word from the corpus dictionary one by one and writing it to the vocabTable.bin file
			// Each single iteration of this for loop writes 16 bytes of data in the vocabTable.bin file
			// The first 8 bytes represents the 
			for (String s : dictionary) {
				
				// write the vocab table entry for this term: 
				// byte location of the term in the vocablist file <---> and the byte location of the postings for the term in the postings file.
				byte[] vPositionBytes = ByteBuffer.allocate(8).putLong(vocabPositions[vocabI]).array();
				
				// Printing some info for understanding
				System.out.print("position of the first character of the term : " + s + " : is = " + vocabPositions[vocabI] + ". Byte Representation : ");
				for (int i = 0; i < vPositionBytes.length; i++) {
					System.out.print(vPositionBytes[i]);
				}				
				System.out.print(" ::: number of bytes : " + vPositionBytes.length);
				System.out.println();
				
				// Writes the 8 byte representation of the position of the first character of the word 's' in the vocabTable
				vocabTable.write(vPositionBytes, 0, vPositionBytes.length);
				
				byte[] pPositionBytes = ByteBuffer.allocate(8).putLong(postingsFile.getChannel().position()).array();
				
				// The address from where the postings list of this term starts in postings.bin file				
				System.out.print("The address in postings.bin from where the postings of the term " + s + "starts : ");
				for (int i = 0; i < pPositionBytes.length; i++) {
					System.out.print(pPositionBytes[i]);
				}
				vocabTable.write(pPositionBytes, 0, pPositionBytes.length);

				/**
				 *  BEGIN :: Write the postings.bin file now. Below is a short step by step description
				 *  1. Fetch the postings list of this term from the index created in main memory
				 *  2. Get the size of the postings list => Gives you the number of documents in which this term occurs => doc frequency
				 *  3. Convert the decimal value of the size of the postings list into a 4 byte value
				 *  4. Write those 4 bytes (doc frequency) into the the postings.bin file. => 
				 *  	the next 4 bytes represents the id of the first document in which this term occurs
				 *  5. Get the term frequency in the document i.e. the size of the term positional list 
				 *  6. Convert the decimal value of the size into 4 bytes value
				 *  7. Now write the first position i.e the first value of the positional list => the positions where this term occurs first in the document
				 */
				// write the postings file for this term. first, the document
				// frequency for the term, then
				// the document IDs, encoded as gaps.
				
				// for each String in dictionary, retrieve the list of postings which gives you the document frequency.
				List<Integer> postings = index.getPostings(s);
				
				// Now convert the size of the postings list into a 4 byte value
				byte[] docFreqBytes = ByteBuffer.allocate(4).putInt(postings.size()).array();
				
				// Write the size of the postings list, converted into the 4 byte value in above step, into postings.bin file
				postingsFile.write(docFreqBytes, 0, docFreqBytes.length);
				
				/**
				 * TODO
				 * Get the list of positions where this term occurs in the document and then write all the positions. 
				 * No need to encode positions as gaps. Even in the worst case scenario, a document will not contain the same word 
				 * repeated (Integer.MAX_VALUE) number of times
				 */

				int lastDocId = 0;
				for (int docId : postings) {
					// encode a gap, not a docID
					byte[] docIdBytes = ByteBuffer.allocate(4).putInt(docId - lastDocId).array(); 
					postingsFile.write(docIdBytes, 0, docIdBytes.length);
					lastDocId = docId;
				}

				vocabI++;
			}
			vocabTable.close();
			postingsFile.close();
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		} finally {
			try {
				postingsFile.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * Creates a vocab.bin file on the disk.
	 * The file is located at the folder represented by the folder variable
	 * The file contains the whole lexicographically sorted vocabulary of the corpus as a contiguous sequence of characters
	 * Parallel to this vocab.bin, a new list of vocabPositions[] of type long[] is created
	 * The ith entry in the list contains the byte location of the first character of the ith word in the vocab.bin file.
	 * The length of the ith term would then be (byte_location_of_i+1th_word - byte_location_of_ith_word)
	 * At the end of the execution of this method, the vocabPositions list will be there in the main memory and will be used in the 
	 * creation of vocabTable
	 * 
	 * @param folder
	 * @param dictionary
	 * @param vocabPositions
	 */
	private static void buildVocabFile(String folder, String[] dictionary, long[] vocabPositions) {
		OutputStreamWriter vocabList = null;
		try {
			// first build the vocabulary list: a file of each vocab word
			// concatenated together.
			// also build an array associating each term with its byte location
			// in this file.
			int vocabI = 0;
			vocabList = new OutputStreamWriter(new FileOutputStream(new File(folder, "vocab.bin")), "ASCII");

			int vocabPos = 0;
			for (String vocabWord : dictionary) {
				// for each String in dictionary, save the byte position where
				// that term will start in the vocab file.
				vocabPositions[vocabI] = vocabPos;
				System.out.println("Byte Position where the word " + vocabWord + " starts is : " + vocabPos);
				
				vocabList.write(vocabWord); // then write the String
				
				vocabI++;
				vocabPos += vocabWord.length();
			}
		} catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.toString());
		} catch (IOException ex) {
			System.out.println(ex.toString());
		} finally {
			try {
				vocabList.close();
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}
		}
	}

	private static void indexFiles(String folder, final NaiveInvertedIndex index) {
		int documentID = 0;
		final Path currentWorkingPath = Paths.get(folder).toAbsolutePath();

		try {
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
						// we have found a .txt file; add its name to the
						// fileName list,
						// then index the file and increase the document ID
						// counter.
						// System.out.println("Indexing file " +
						// file.getFileName());

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
		} catch (IOException ex) {
			Logger.getLogger(IndexWriter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void indexFile(File fileName, NaiveInvertedIndex index, int documentID) {

		try {
			SimpleTokenStream stream = new SimpleTokenStream(fileName);
			while (stream.hasNextToken()) {
				String term = stream.nextToken();
				String stemmed = PorterStemmer.processToken(term);

				if (stemmed != null && stemmed.length() > 0) {
					index.addTerm(stemmed, documentID);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
