package com.elas.app.resp.model;

import org.elasticsearch.action.index.IndexResponse;

public class WebIndexerResponse {

	private IndexResponse indexResponse;
	private String webResponse;
	private boolean indexExists;
	
	public IndexResponse getIndexResponse() {
		return indexResponse;
	}
	public void setIndexResponse(IndexResponse indexResponse) {
		this.indexResponse = indexResponse;
	}
	public String getWebResponse() {
		return webResponse;
	}
	public void setWebResponse(String webResponse) {
		this.webResponse = webResponse;
	}
	public boolean isIndexExists() {
		return indexExists;
	}
	public void setIndexExists(boolean indexExists) {
		this.indexExists = indexExists;
	}
	
}
