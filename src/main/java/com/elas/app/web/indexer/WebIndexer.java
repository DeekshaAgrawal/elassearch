package com.elas.app.web.indexer;

import java.util.TreeMap;
import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Component
public class WebIndexer {

	public TreeMap<String, String> createWebJSONFromAddress(String address) {
		TreeMap<String, String> tm = null;
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			HtmlPage page = client.getPage(address);
			tm = processDomNode(page);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			client.close();
		}
		return tm;
	}
	
	TreeMap<String, String> processDomNode(HtmlPage page){
		TreeMap<String, String> tm = new TreeMap<>();
		DomNodeList<DomNode> childNodes = page.getChildNodes();
		childNodes.forEach(e -> processNode(e, tm));
		return tm;
	}
	
	void processNode(DomNode domNode, TreeMap<String, String> treem){
		
		if(domNode.hasChildNodes()) {
			DomNodeList<DomNode> childNodes = domNode.getChildNodes();
			childNodes.forEach(e-> processNode(e , treem));
		}else {
			if(domNode.getNodeType()==3) {
				String value = domNode.getTextContent().replaceAll("\\n+", " ").replaceAll("\\s{2,}", " ").trim();
				if(value.contains("http://")) {
					String links = treem.get("links");
					if(null!=links) {
						links = links+ " "+value;
						treem.replace("links", links);
					}else {
						treem.put("links", value);
					}
				} else {
					if(treem.containsKey("source")) {
						String data = treem.get("source");
						data = data + "" + value;
						treem.replace("source", data);
					}else {
						treem.put("source", value);
					}
				}
			}
		}
	}
}
