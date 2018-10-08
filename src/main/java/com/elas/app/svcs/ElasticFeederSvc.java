package com.elas.app.svcs;

import java.util.TreeMap;

import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elas.app.repo.ElasticFeedRepo;
import com.elas.app.req.model.WebFeedModel;
import com.elas.app.resp.model.WebIndexerResponse;
import com.elas.app.web.indexer.WebIndexer;

@Service
public class ElasticFeederSvc {

	@Autowired
	private WebIndexer webIndexer;
	
	@Autowired
	private ElasticFeedRepo elasticFeedRepo;
	
	public WebIndexerResponse feedData(WebFeedModel webFeedModel) {
		WebIndexerResponse webIndexerResponse = null;
		IndexResponse response = null;
		String searchAddress = "";
		if(webFeedModel.getAddress().contains("https://")) {
			searchAddress = webFeedModel.getAddress().substring(8);
		}
		if(webFeedModel.getAddress().contains("http://")) {
			searchAddress = webFeedModel.getAddress().substring(7);
		}
		if(this.elasticFeedRepo.queryElastDbIndexForAddress(searchAddress)) {
			webIndexerResponse = new WebIndexerResponse();
			webIndexerResponse.setIndexExists(true);
			webIndexerResponse.setIndexResponse(null);
		}else {
			webIndexerResponse = new WebIndexerResponse();
			TreeMap<String, String> webJson = this.webIndexer.createWebJSONFromAddress(webFeedModel.getAddress());
			webFeedModel.setAddress(searchAddress);
			response = elasticFeedRepo.insertDataIntoCluster(webFeedModel, webJson);
			webIndexerResponse.setIndexResponse(response);
			webIndexerResponse.setIndexExists(false);
		}
		
		return webIndexerResponse;
	}
	
}
