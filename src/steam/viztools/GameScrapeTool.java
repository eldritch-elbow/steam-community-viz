package steam.viztools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import steam.viztools.model.Game;
import steam.viztools.scraper.GameScraper;
import steam.viztools.serialize.DataSerializer;
import steam.viztools.serialize.Persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class GameScrapeTool {

  public static void main(String[] args) throws Exception {

    if (args.length != 3) {
      throw new RuntimeException("Expected three arguments");
    }

    File inFile = new File(args[0]);
    String dataset = args[1];
    String format = args[2];

    Map<String, Game> games = readGameList(inFile, format);
    
    OutputStream os = Persistence.prepGameFile(dataset);
    
    scrape(games, os);    
    
    os.close(); // Close output stream
  }

  private static Map<String, Game> readGameList(File inFile, String format) throws IOException {

    // Prepare model storage
    Map<String, Game> games = new HashMap<String, Game>();

    if (format.equals("id-list")) {
      
      List<String> idLines = FileUtils.readLines(inFile);
      for (String gameID : idLines) {
        games.put(gameID, new Game(gameID));
      }
      
    } else if (format.equals("steam-json")) {
      
      JsonReader jr = new JsonReader( new InputStreamReader( new FileInputStream(inFile) ) );    
      JsonParser parser = new JsonParser();
      
      JsonObject data = parser.parse(jr).getAsJsonObject();
      JsonObject applist = data.get("applist").getAsJsonObject();
      JsonArray ja = applist.get("apps").getAsJsonArray();
      
      for (JsonElement gameEl : ja) {
        JsonObject gameObj = gameEl.getAsJsonObject();
        String appID = gameObj.get("appid").getAsString();
        String name = gameObj.get("name").getAsString();
        
        System.out.println(appID + " " + name);
        Game g = new Game(appID);
        g.setName(name);
        games.put(appID, g);
      }
      
    } 
    
    return games;
  }

  private static Set<Game> scrape(Map<String,Game> games, OutputStream os) throws Exception {    

    System.out.println("Scraping global achievement data for " + games.size() + " games");
    
    GameScraper gs = new GameScraper();
    DataSerializer ser = new DataSerializer();

    int count = 1;
    for (String appID : games.keySet()) {
      System.out.print( String.format("(%d/%d)", count, games.size()) );
      
      Game g = games.get(appID);
      
      gs.scrape(g);      
      ser.writeGame(g, os);
      
      os.write( ",\n".getBytes() );
      os.flush(); // Flush after every game to ensure output written
      
      games.put(appID, null); // Clear reference to the game to reduce memory overhead
      
      count++;
      Thread.sleep(500); // Don't overwhelm the API
    }
    
    return new HashSet<Game>(games.values());
  }

}
