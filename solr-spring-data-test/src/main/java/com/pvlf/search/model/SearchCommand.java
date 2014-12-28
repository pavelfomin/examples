package com.pvlf.search.model;

import java.io.Serializable;

public class SearchCommand implements Serializable {
	private static final long serialVersionUID = -1833143624358391081L;

	private String searchTerm;
	
	public SearchCommand() {
	}

	/**
	 * @param searchTerm
	 */
	public SearchCommand(String searchTerm) {
		setSearchTerm(searchTerm);
	}

	public String getSearchTerm() {
		return searchTerm;
	}
	
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
}
