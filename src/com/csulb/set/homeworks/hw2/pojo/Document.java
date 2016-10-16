package com.csulb.set.homeworks.hw2.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Document {

	private String body;
	private String url;
	private String title;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Document() {
	}

	/**
	 * 
	 * @param body
	 * @param title
	 * @param url
	 */
	public Document(String body, String url, String title) {
		this.body = body;
		this.url = url;
		this.title = title;
	}

	/**
	 * 
	 * @return The body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * 
	 * @param body
	 *            The body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * 
	 * @return The url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 *            The url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 *            The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(body).append(url).append(title).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Document) == false) {
			return false;
		}
		Document rhs = ((Document) other);
		return new EqualsBuilder().append(body, rhs.body).append(url, rhs.url).append(title, rhs.title).isEquals();
	}

}