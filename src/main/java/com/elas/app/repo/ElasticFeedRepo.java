package com.elas.app.repo;

import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.CommonTermsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elas.app.req.model.WebFeedModel;

@Repository
public class ElasticFeedRepo {

	@Autowired
	private RestHighLevelClient restHighLevelClient;
	
	public boolean queryElastDbIndexForAddress(String address){
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
//		sourceBuilder.query(QueryBuilders.matchQuery("address",address));
		//MatchQueryBuilder mqb = new MatchQueryBuilder("address", address);
		//TermQueryBuilder tq = new TermQueryBuilder("address", address);
		//MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("address", address);
		MatchQueryBuilder tq = new MatchQueryBuilder("address", address);
		
		sourceBuilder.query(tq);
		sourceBuilder.from(0); 
		sourceBuilder.size(5); 
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("web");
		searchRequest.source(sourceBuilder);
		try {
			IndicesClient indices = this.restHighLevelClient.indices();
			GetIndexRequest request = new GetIndexRequest();
			request.indices("web");
			boolean indexExists = indices.exists(request, RequestOptions.DEFAULT);
			if(!indexExists) {
				return false;
			}
			SearchResponse searchResponse = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			SearchHits hits = searchResponse.getHits();
			if(hits.totalHits>0) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public IndexResponse insertDataIntoCluster(WebFeedModel webFeedModel, TreeMap<String, String> webJSON) {
		try {
			webJSON.put("address", webFeedModel.getAddress());
			webJSON.put("feedDate", new Date().toString());
//			XContentBuilder builder = XContentFactory.jsonBuilder();
//			builder.startObject();
//			{
//				builder.field("address", webFeedModel.getAddress());
//				builder.field("feedDate", new Date());
//				builder.map(webJSON);
//			}
//			builder.endObject();
			IndexRequest indexRequest = new IndexRequest("web","_doc").source(webJSON);
			IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
