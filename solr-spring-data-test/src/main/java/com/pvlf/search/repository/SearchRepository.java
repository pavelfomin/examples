package com.pvlf.search.repository;

import com.pvlf.search.model.SitePageDocument;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;

public interface SearchRepository {
	
	public long count(String searchTerm);
	public HighlightPage<SitePageDocument> search(String searchTerm, Pageable page);
}
