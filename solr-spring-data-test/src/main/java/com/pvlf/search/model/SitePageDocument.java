package com.pvlf.search.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

public class SitePageDocument {

	@Id
    @Field
    private String id;
 
    @Field
    private String content;
 
    @Field
    private String title;
 
    @Field
    private String url;

    /**
     * Highlighted version of the document.
     */
    private SitePageDocument highlighted;
    
    public SitePageDocument() {
    }
    
    public SitePageDocument(String id, String title, String content, String url) {
    	setId(id);
    	setTitle(title);
    	setContent(content);
    	setUrl(url);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public SitePageDocument getHighlighted() {
		return highlighted;
	}

	public void setHighlighted(SitePageDocument highlighted) {
		this.highlighted = highlighted;
	}
	
	/**
	 * Returns the highlighted content if available, or the original content otherwise. 
	 * @return
	 */
	public String getHighlightedContent() {
		return (highlighted != null && highlighted.getContent() != null ? highlighted.getContent() : content);
	}

	/**
	 * Returns the highlighted title if available, or the original title otherwise. 
	 * @return
	 */
	public String getHighlightedTitle() {
		return (highlighted != null && highlighted.getTitle() != null ? highlighted.getTitle() : title);
	}

	/**
	 * Returns the highlighted url if available, or the original url otherwise. 
	 * @return
	 */
	public String getHighlightedUrl() {
		return (highlighted != null && highlighted.getUrl() != null ? highlighted.getUrl() : url);
	}

	@Override
	public String toString() {
		return String.format("SitePageDocument [id=%s, title=%s, url=%s]", id, title, url);
	}
}
