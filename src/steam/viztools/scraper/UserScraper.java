package steam.viztools.scraper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import steam.viztools.model.Game;
import steam.viztools.model.User;

/**
 * Retrieves game and achievement data for users.
 */
public class UserScraper {

  private HttpClient httpclient;

  public UserScraper() {
    httpclient = new DefaultHttpClient();
  }

  /**
   * Retrieves game data and achievement data for the user with the given steam
   * ID. Creates and returns a new User object accordingly. Also updates the
   * given Map with new games, if encountered in the user's game list.
   */
  public User scrape(String steam64id, Map<String, Game> allGames) throws Exception {

    System.out.println("***** Scraping user " + steam64id + " *****");

    User user = scrapeCoreData(steam64id);

    scrapeMoarData(steam64id, allGames, user);

    return user;
  }

  private User scrapeCoreData(String steam64id) throws Exception {
    /*
     * Scrape some basic user data
     */
    String uri = String.format(
        "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/"+
         "?&key=%s&steamids=%s&format=xml", 
           Constants.API_KEY, 
           steam64id);

    HttpGet httpGet = new HttpGet(uri);
    HttpResponse response1 = httpclient.execute(httpGet);
    System.out.println("Response: " + response1.getStatusLine());

    String persona = "UNKNOWN";
    try {

      // Get response, parse it, consume entire stream
      HttpEntity entity1 = response1.getEntity();
      Document document = XMLWrappers.parseDocument(entity1.getContent());

      String personaTag = "personaname";
      NodeList nl = document.getElementsByTagName(personaTag);
      persona = XMLWrappers.getElementText(nl, personaTag).get(personaTag);

      EntityUtils.consume(entity1);

    } finally {
      httpGet.releaseConnection();
    }

    User user = new User(steam64id, persona);
    return user;
  }

  private void scrapeMoarData(String steam64id, Map<String, Game> allGames,
      User user) throws ClientProtocolException, IOException,
      ParserConfigurationException, SAXException {
    
    /*
     * Define further scrapers
     */
    UserGameScraper ugs = new UserGameScraper();
    UserAchievementScraper uas = new UserAchievementScraper();

    /*
     * Scrape games and achievements
     */

    ugs.scrape(steam64id);

    for (String appID : ugs.getGames()) {

      uas.scrape(steam64id, appID);

      // Consolidate scraped into lists prior to adding to user
      Set<Achievement> achieved = new HashSet<Achievement>(uas.getAchieved());
      Set<Achievement> notAchieved = new HashSet<Achievement>(
          uas.getNotAchieved());

      Game g = allGames.get(appID);
      if (g == null) {
        g = new Game(appID);
        allGames.put(appID, g);
      }

      user.addGame(g, achieved, notAchieved);
    }
  }

}
