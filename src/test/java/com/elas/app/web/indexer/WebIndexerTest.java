package com.elas.app.web.indexer;

import org.junit.BeforeClass;
import org.junit.Test;

public class WebIndexerTest {

	private static WebIndexer webIndexer;
	
	@BeforeClass
	public static void onStart() {
		webIndexer = new WebIndexer();
	}
	
	
	@Test
	public void testCreateWebJSONFromAddress() {
		Object jsonObject = webIndexer.createWebJSONFromAddress("https://en.wikipedia.org/wiki/JSON");
		System.out.println("Test stated");
	}
	
}
