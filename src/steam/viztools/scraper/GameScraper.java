package steam.viztools.scraper;

import java.io.IOException;
import java.text.DecimalFormat;
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

import steam.viztools.model.Achievement;
import steam.viztools.model.Game;

/**
 * A scraper for Steam game data. 
 */
public class GameScraper {

  private HttpClient httpclient;

  public GameScraper() {
    httpclient = new DefaultHttpClient();
  }

  /**
   * Given a Game object, retrieve available data and set it into
   * said object.
   */
  public void scrape(Game g) throws ClientProtocolException, IOException,
      IllegalStateException, ParserConfigurationException, SAXException {

    System.out.println( String.format("SCRAPING: %s %s ", g.appID, g.name() ) );

    String uri = String.format(
        "http://api.steampowered.com/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/"+
        "?" +
        "gameid=%s&" +
        "format=xml", 
           g.appID);

    // Query achievements
    HttpGet httpGet = new HttpGet(uri);
    HttpResponse response1 = httpclient.execute(httpGet);
    
    System.out.println("Response: " + response1.getStatusLine());

    try {
      
      // Get response, parse it, consume entire stream
      HttpEntity entity1 = response1.getEntity();
      Document document = XMLWrappers.parseDocument(entity1.getContent());      
      EntityUtils.consume(entity1);
      
      parseAchievements(g, document);

    } finally {
      httpGet.releaseConnection();
    }

  }

  private void parseAchievements(Game g, Document document) {
    
    // Get list of all achievements
    NodeList nl = document.getElementsByTagName("achievement");    
    System.out.println( String.format("Game %s: %d achievement nodes", g.appID, nl.getLength() ) ); 

    // Process achievements
    for (int i = 0; i < nl.getLength(); i++) {
      Element achElement = (Element) nl.item(i);
      parseGlobalAchievementElement(achElement, g);
    }
  }

  private void parseGlobalAchievementElement(Element achElement, Game g) {

    NodeList achData = achElement.getChildNodes();

    Map<String, String> textEls = XMLWrappers.getElementText(achData, "name", "percent");
    String readName = textEls.get("name");
    String globRate = textEls.get("percent");
    
    Achievement ach = Achievement.fromString(readName);
    if (ach == null) {
      System.out.println(" Discarding: "+readName);
      return;
    }
    
    ach.setRate( Float.valueOf(globRate) );
    
    g.addAchievement(ach);
  }

}
