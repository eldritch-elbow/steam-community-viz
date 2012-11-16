package steam.viztools;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import steam.viztools.model.Game;
import steam.viztools.scraper.GameScraper;
import steam.viztools.serialize.Persistence;

public class GameScrapeTool {

  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      throw new RuntimeException("Expected two arguments");
    }

    File inFile = new File(args[0]);
    String dataset = args[1];

    Set<Game> games = scrape(inFile);    
    Persistence.storeGameSet(dataset, games);
    
  }

  private static Set<Game> scrape(File inFile) throws Exception {
    
    // Prepare model storage
    Map<String, Game> games = new HashMap<String, Game>();

    List<String> idLines = FileUtils.readLines(inFile);
    for (String gameID : idLines) {
      games.put(gameID, new Game(gameID));
    }

    GameScraper gs = new GameScraper();

    for (String appID : games.keySet()) {
      gs.scrape(games.get(appID));
      Thread.sleep(500); // Don't overwhelm the API
    }
    
    return new HashSet<Game>(games.values());
  }

}
