package com.elas.app.rest.endpts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elas.app.req.model.WebFeedModel;
import com.elas.app.resp.model.WebIndexerResponse;
import com.elas.app.svcs.ElasticFeederSvc;

@RestController()
@RequestMapping(path="/api")
public class FeedController {

	@Autowired
	private ElasticFeederSvc elasticFeederSvc;
	
	@PostMapping(path="/feedWebAddress",produces="application/json", consumes="application/json")
	public ResponseEntity<WebIndexerResponse> feedWebAddress(@RequestBody WebFeedModel webFeedModel){
		WebIndexerResponse feedData = this.elasticFeederSvc.feedData(webFeedModel);
		ResponseEntity<WebIndexerResponse> response = new ResponseEntity<WebIndexerResponse>(feedData, HttpStatus.OK); 
		return response;
	}
}
