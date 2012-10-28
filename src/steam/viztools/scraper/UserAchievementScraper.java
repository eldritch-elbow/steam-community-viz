package steam.viztools.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import steam.viztools.Constants;
import steam.viztools.model.Achievement;

public class UserAchievementScraper {

	private HttpClient httpclient;
	
	private List<Achievement> achievements;
	
	public UserAchievementScraper() {
		httpclient = new DefaultHttpClient();
	}
	
	/*
	 * Build a list of game information for a user
	 */
	public void scrape(String id, String game) throws ClientProtocolException, IOException, ParserConfigurationException, IllegalStateException, SAXException {
		
		System.out.println("Scraping achievements for "+id+"/"+game);
		achievements = new ArrayList<Achievement>();
		
		String uri = String.format(
				"http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/" +
				"?appid=%s&key=%s&steamid=%s&format=xml",
					game,
					Constants.API_KEY,
					id);
		
		// Query achievements
		HttpGet httpGet = new HttpGet(uri);
		
		HttpResponse response1 = httpclient.execute(httpGet);		
	    System.out.println("Response: " + response1.getStatusLine());

		try {
			// Get response, parse it, consume entire stream
		    HttpEntity entity1 = response1.getEntity();
		    Document document = XMLWrappers.parseDocument(entity1.getContent());
		    EntityUtils.consume(entity1);
		    	
		    // Get achievements
		    NodeList nl = document.getElementsByTagName("achievement");		    
		    System.out.println("Found "+nl.getLength() + " achievements");
    
		    // Process achievements
		    for(int i=0; i<nl.getLength(); i++){
		    	
		      Element achElement = (Element)nl.item(i);		      
		      scrapeAchievementElement(achElement);		      
		    }
		    
		    EntityUtils.consume(entity1);
		    
		} finally {
		    httpGet.releaseConnection();
		}
		
	}


	/*
	* Extract achievement information from an XML element for a user's game
	*/
	private void scrapeAchievementElement(Element gameEl) {
	
		NodeList achData = gameEl.getChildNodes();
		
		Map<String,String> textEls = new HashMap<String,String>(  );
		textEls.put("apiname", null);
		textEls.put("achieved", null);
		
		XMLWrappers.getElementText(achData, textEls);
		
		achievements.add(new Achievement(
				textEls.get("apiname"), 
				textEls.get("achieved").equals("1")));
	}
	
	public List<Achievement> getAchievements() {
		return Collections.unmodifiableList(achievements);
	}
	
	
	
}
