package com.pvlf.search.service;

import com.pvlf.search.model.SitePageDocument;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;

public interface SearchService {

	/**
	 * Returns an instance of the Page implementation.
	 * @param searchTerm
	 * @param page
	 * @return
	 */
	public HighlightPage<SitePageDocument> search(String searchTerm, Pageable page);
}
