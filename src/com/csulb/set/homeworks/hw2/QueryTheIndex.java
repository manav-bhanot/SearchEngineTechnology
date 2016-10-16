/**
 * 
 */
package com.csulb.set.homeworks.hw2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manav
 *
 */
public class QueryTheIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		InvertedIndex index = new InvertedIndex();
		// this gets the current working path.
		final Path currentWorkingPath = Paths
				.get("C:\\Users\\Manav\\Documents\\CECS-529_Search_Engine_Technology\\Homeworks\\hw2\\Excercise1.2")
				.toAbsolutePath();

		index.indexDirectory(currentWorkingPath);

		System.out.println("Printing Results for the query : \"schizophrenia AND drug\"");

		Integer[] postingListOne = index.invertedIndexMap.get("schizophrenia").toArray(new Integer[0]);
		Integer[] postingListTwo = index.invertedIndexMap.get("drug").toArray(new Integer[0]);

		int i = 0, j = 0;
		while (i < postingListOne.length && j < postingListTwo.length) {
			if (postingListOne[i].equals(postingListTwo[j])) {
				System.out.println(postingListOne[i]);
				i++;
				j++;
			} else if (postingListOne[i].compareTo(postingListTwo[j]) < 0) {
				i++;
			} else {
				j++;
			}
		}

		System.out.println("\n\nPrinting Results for the query : \"for AND NOT(drug OR approach)\"");

		postingListOne = index.invertedIndexMap.get("drug").toArray(new Integer[0]);
		postingListTwo = index.invertedIndexMap.get("approach").toArray(new Integer[0]);
		Integer[] postingListThree = index.invertedIndexMap.get("for").toArray(new Integer[0]);

		List<Integer> postingList = new ArrayList<Integer>();

		i = 0;
		j = 0;

		while (i < postingListOne.length && j < postingListTwo.length) {
			if (postingListOne[i].equals(postingListTwo[j])) {
				postingList.add(postingListOne[i]);
				i++;
				j++;
			} else if (postingListOne[i].compareTo(postingListTwo[j]) < 0) {
				postingList.add(postingListOne[i]);
				i++;
			} else {
				postingList.add(postingListTwo[j]);
				j++;
			}
		}

		while (i < postingListOne.length) {
			postingList.add(postingListOne[i]);
			i++;
		}

		while (j < postingListTwo.length) {
			postingList.add(postingListTwo[j]);
			j++;
		}

		i = 0;
		j = 0;

		while (i < postingListThree.length && j < postingList.size()) {
			if (postingListThree[i].equals(postingList.get(j))) {				
				i++;
				j++;
			} else if (postingListThree[i].compareTo(postingList.get(j)) < 0) {
				System.out.print(postingListThree[i]+" ");
				i++;
			} else {
				j++;
			}
		}
		
		while (i < postingListThree.length) {
			System.out.print(postingListThree[i]+" ");
			i++;
		}
	}

}
