/**
 * 
 */
package com.csulb.set.homeworks.hw4;

import java.util.List;

/**
 * @author Manav
 *
 */
public class PositionalPosting {
	private Integer documentId;
	private List<Integer> positions;

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public List<Integer> getPositions() {
		return positions;
	}

	public void setPositions(List<Integer> positions) {
		this.positions = positions;
	}
}
