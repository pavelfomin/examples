package com.pvlf.search.service;

import com.pvlf.search.model.SitePageDocument;
import com.pvlf.search.repository.SearchRepository;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

	@Resource
    private SearchRepository repository;
	
	@Override
	public HighlightPage<SitePageDocument> search(String searchTerm, Pageable page) {
		return repository.search(searchTerm, page);
	}

}
