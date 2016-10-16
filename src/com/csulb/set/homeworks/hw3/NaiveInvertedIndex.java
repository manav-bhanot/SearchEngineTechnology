package com.csulb.set.homeworks.hw3;

import java.util.*;

public class NaiveInvertedIndex {
	private HashMap<String, List<Integer>> mIndex;

	public NaiveInvertedIndex() {
		mIndex = new HashMap<String, List<Integer>>();
	}

	public void addTerm(String term, int documentID) {
		// TO-DO: add the term to the index hashtable. If the table does not
		// have
		// an entry for the term, initialize a new ArrayList<Integer>, add the
		// docID to the list, and put it into the map. Otherwise add the docID
		// to the list that already exists in the map, but ONLY IF the list does
		// not already contain the docID.

		
		// Check if the term already exists in the map
		// If it is null, then add the term and a new ArrayList for the postings list.
		if (mIndex.get(term) == null) {
			mIndex.put(term, new ArrayList<Integer>());
			mIndex.get(term).add(documentID);
		}
		
		List<Integer> postingsList = mIndex.get(term);
		
		if (postingsList.size() != 0 && postingsList.get(postingsList.size() - 1) != documentID) {
			mIndex.get(term).add(documentID);
		}
	}

	public List<Integer> getPostings(String term) {
		// TO-DO: return the postings list for the given term from the index
		// map.

		return mIndex.get(term);
	}

	public int getTermCount() {
		// TO-DO: return the number of terms in the index.
		
		return mIndex.keySet().size();
	}

	public String[] getDictionary() {
		// TO-DO: fill an array of Strings with all the keys from the hashtable.
		// Sort the array and return it.
		
		String[] dict = mIndex.keySet().toArray(new String[0]);
		Arrays.sort(dict);
		
		return dict;
	}
}
