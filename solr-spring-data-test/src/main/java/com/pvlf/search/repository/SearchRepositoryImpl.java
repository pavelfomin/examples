package com.pvlf.search.repository;

import com.pvlf.search.model.SitePageDocument;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Repository;

@Repository
public class SearchRepositoryImpl implements SearchRepository {

	private static final String[] fields = new String[]{"title", "content", "url"};

	@Resource
	private SolrTemplate solrTemplate;

	@Override
	public long count(String searchTerm) {
		String[] words = searchTerm.split(" ");
		Criteria conditions = createSearchCriteria(words, fields);
		SimpleQuery countQuery = new SimpleQuery(conditions);

		return solrTemplate.count(countQuery);
	}

    @Override
    public HighlightPage<SitePageDocument> search(String searchTerm, Pageable page) {

		Criteria criteria = createSearchCriteria(searchTerm.split(" "), fields);
    	SimpleHighlightQuery query = new SimpleHighlightQuery(criteria);
        query.setHighlightOptions(new HighlightOptions().addFields(Arrays.asList(fields)).setSimplePrefix("<b>").setSimplePostfix("</b>").setNrSnipplets(10));
        query.setPageRequest(page);
 
        //Page<SitePageDocument> results = solrTemplate.queryForPage(query, SitePageDocument.class);
        HighlightPage<SitePageDocument> results = solrTemplate.queryForHighlightPage(query, SitePageDocument.class);
        processHighlighted(results.getHighlighted());
        return results;
    }
	
    /**
     * Processes the highlighted resulults, setting them on the SitePageDocument instances.
     * @param results list of SitePageDocument with highlighted attribute set.
     * @return
     */
    private void processHighlighted(List<HighlightEntry<SitePageDocument>> results) {
    	
    	for (HighlightEntry<SitePageDocument> entry : results) {
			SitePageDocument entity = entry.getEntity();
			List<Highlight> highlights = entry.getHighlights();
			entity.setHighlighted(new SitePageDocument());
			for (Highlight highlight : highlights) {
				setHighlight(highlight, entity);
			}
		}
	}
    
    /**
     * Sets the highlighted attribute extracted from the Highlight. 
     * @param highlight
     * @param entity SitePageDocument instance with highlighted attribute set.
     */
    private void setHighlight(Highlight highlight, SitePageDocument entity) {

    	SitePageDocument highlighted = entity.getHighlighted();
		
		Field field = highlight.getField();
		String name = field.getName();
		String text = getHighlightedText(highlight);
		if("title".equals(name)) {
			highlighted.setTitle(text);
		} else if("content".equals(name)) {
			highlighted.setContent(text);
		} else if("url".equals(name)) {
			highlighted.setUrl(text);
		}
		
	}

	private String getHighlightedText(Highlight highlight) {
		List<String> snipplets = highlight.getSnipplets();
		StringBuilder builder = new StringBuilder();
		for (String snippet : snipplets) {
			builder.append(snippet);
		}
		return builder.toString();
	}

	/**
     * Creates the search criteria.
     * @param words
     * @param fields
     * @return
     */
	private Criteria createSearchCriteria(String[] words, String[] fields) {
		
		StringBuilder queryString = null;
		
		for (String field : fields) {
			StringBuilder fieldCriteria = null;
			for (String word : words) {
				fieldCriteria = (fieldCriteria == null ? new StringBuilder() : fieldCriteria.append(" AND "));
				fieldCriteria.append(field).append(":").append(Criteria.WILDCARD).append(word).append(Criteria.WILDCARD);
			}
			
			queryString = (queryString == null ? new StringBuilder() : queryString.append(" OR "));
			queryString.append("(").append(fieldCriteria).append(")");
		}

		return new SimpleStringCriteria(queryString.toString());
	}
}
