package steam.viztools.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import steam.viztools.model.Game;


// The underlying HTTP connection is still held by the response object 
// to allow the response content to be streamed directly from the network socket. 
// In order to ensure correct deallocation of system resources 
// the user MUST either fully consume the response content  or abort request 
// execution by calling HttpGet#releaseConnection().



public class UserGameScraper {

	private HttpClient httpclient;
	
	private List<Game> games;
	
	public UserGameScraper() {
		httpclient = new DefaultHttpClient();
	}

	/*
	 * Build a list of game information for a user
	 */
	public void scrape(String id) throws ClientProtocolException, IOException, ParserConfigurationException, IllegalStateException, SAXException {
		
		System.out.println("Scraping games for user "+id);
		games = new ArrayList<Game>();
		
		// Query games
		HttpGet httpGet = new HttpGet(
				"http://steamcommunity.com/" +
				"profiles/"+
				id+
				"/games?" +
				"tab=all" +
				"&xml=1");
		
		HttpResponse response1 = httpclient.execute(httpGet);		
	    System.out.println("Response: " + response1.getStatusLine());

		try {
			// Get response, parse it, consume entire stream
		    HttpEntity entity1 = response1.getEntity();
		    Document document = XMLWrappers.parseDocument(entity1.getContent());
		    EntityUtils.consume(entity1);
		    
		    // Get games
		    NodeList nl = document.getElementsByTagName("game");		    
		    System.out.println("Found "+nl.getLength() + " games");
		    
		    // Process games
		    for(int i=0; i<nl.getLength(); i++){
		    	
		      Element gameElement = (Element)nl.item(i);		      
		      scrapeGameElement(gameElement);		      
		    }
		    
		    
		} finally {
		    httpGet.releaseConnection();
		}
		
	}

	
	/*
	 * Extract game information from an XML element for a user
	 */
	private void scrapeGameElement(Element gameEl) {
		
		NodeList gameData = gameEl.getChildNodes();
		
		Map<String,String> gameEls = new HashMap<String,String>(  );
		gameEls.put("appID", null);
		gameEls.put("name", null);

	    XMLWrappers.getElementText(gameData, gameEls);
	    
		games.add(new Game(
				gameEls.get("appID"), 
				gameEls.get("name")));
	}


	public List<Game> getGames() {
		return Collections.unmodifiableList(games);
	}

	
}
