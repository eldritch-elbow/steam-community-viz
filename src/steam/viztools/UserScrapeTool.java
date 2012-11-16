package steam.viztools;

import static steam.viztools.Constants.FILE_SEP;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import steam.viztools.model.Achievement;
import steam.viztools.model.Game;
import steam.viztools.model.User;
import steam.viztools.scraper.GameScraper;
import steam.viztools.scraper.UserScraper;
import steam.viztools.serialize.DataSerializer;

import com.google.common.collect.Sets;

/**
 * Command line program for retrieving gamer data from the Steam
 * Community API
 */
public class UserScrapeTool {

	/**
	 * Main method. Requires arguments:
	 * 0: name of file containing steam IDs
	 * 1: prefix of output file to store data in
	 */
	public static void main(String[] args) throws Exception {

	  // TODO add command line parser
		if (args.length != 2) {
			throw new RuntimeException("Expected two arguments");
		}
		
		String inFile = args[0];
		String dataset = args[1];
		
		// Prepare model storage
	  Set<User> users = new HashSet<User>();
		Map<String,Game> games = new HashMap<String,Game>();
		  
		/* 
		 * Scrape data for users and games 
		 */
		System.out.println("***** SCRAPING DATA *****");
		scrapeUsers(inFile, users, games);
    scrapeGames(games);
    
    /*
     * Validate! All user game achievement counts should match retrieved counts
     */
    System.out.println("***** VALIDATING *****");   
    validate(users, games.values());
    
    /*
     * Write data to file in standard format
     */
    System.out.println("***** STORE DATA *****");
    persistData(dataset, users);
    
		/*
		 * Display summaries
		 */
    System.out.println("***** DATA SUMMARY *****");    
		summarize(users, games);
	
	}


	
  private static void scrapeUsers(String inFile, Set<User> users,
      Map<String, Game> games) throws Exception {
    
    UserScraper us = new UserScraper();		

		List<String> idLines = FileUtils.readLines( new File(inFile) );
		for (String steamID : idLines ) {
			User user = us.scrape(steamID, games);
			users.add(user);			
		}
  }

  private static void scrapeGames(Map<String, Game> games) throws Exception {
    
    GameScraper gs = new GameScraper();  
    
    for (String appID : games.keySet() ) {
      gs.scrape( games.get(appID) );
      Thread.sleep(500); // Don't overwhelm the API
    }
  }

  
  private static void validate(Set<User> users, Collection<Game> games) {

    System.out.println("**** Checking for extra user achievements ****");
    
    // First, detect any additional achievements associated with users
    for (User u : users) {
      for (Game g : u.games()) {
        
        Set<Achievement> gameAchs = g.achievements();
        Set<Achievement> userAchs = u.anyAchievements(g);         
        Set<Achievement> extraUserAchs = Sets.difference(userAchs, gameAchs);
        
        System.out.print( String.format( "Checking user/game [%s/%s] user=%d, game=%d:\t", u.steam64id, g.appID, userAchs.size(), gameAchs.size() ));

        if (extraUserAchs.size() == 0) {
          System.out.println( "OK! :3" );
          continue;
        }

        System.out.println( "DISCREPANCY!" );

        for (Achievement userGameAch : extraUserAchs) {           
          System.out.println( "    WARNING - global list missing user achievement: " + userGameAch);   
          g.addAchievement(userGameAch);
        }          
      }
    }

    // Now make sure all users have all game achievements
    for (Game g : games) {
      for (User u : users) {
        
        // Does the user have the game?
        if (!u.games().contains(g)) {
          continue;
        }
        
        Set<Achievement> gameAchs = g.achievements();
        Set<Achievement> userAchs = u.anyAchievements(g);         
        Set<Achievement> extraGameAchs = Sets.difference(gameAchs, userAchs);
        
        System.out.print( String.format( "Checking game/user [%s/%s] game=%d, user=%d:\t", g.appID, u.steam64id, gameAchs.size(), userAchs.size() ));
        
        if (extraGameAchs.size() == 0) {
          System.out.println( "OK! :3" );
          continue;
        }

        System.out.println( "DISCREPANCY!" );

        for (Achievement newForUser : extraGameAchs) {           
          System.out.println( "    WARNING - user missing global achievement: " + newForUser);   
          u.setNotAchieved(g, newForUser);
        }  
      }
    }
    
  }

  private static void persistData(String dataset, Set<User> users) throws Exception {
    DataSerializer dw = new DataSerializer();   

    String outUserFilename = "data" + FILE_SEP + dataset + "_users.json";
    String outGameFilename = "data" + FILE_SEP + dataset + "_games.json";
    
    File outFile; 
    FileOutputStream fos;
    
    outFile = new File(outUserFilename);    
    fos = new FileOutputStream( outFile );

    System.out.println("Writing serialized user data to: "+outFile.getAbsolutePath());
    dw.writeUsers(users, fos);    
    fos.close();

    outFile = new File(outGameFilename);    
    fos = new FileOutputStream( outFile );

    System.out.println("Writing serialized game data to: "+outFile.getAbsolutePath());
    dw.writeUsers(users, fos);    
    fos.close();
  }

  private static void summarize(Set<User> users, Map<String, Game> games) {
    
    /*
     * Display users and games
     */
    
    Map<Game,Integer> top = UserAnalyzer.deriveTopGames(users);
    
    ValueComparator bvc =  new ValueComparator(top);
    TreeMap<Game,Integer > sorted_map = new TreeMap<Game,Integer>(bvc);
    sorted_map.putAll(top);
    
    System.out.println( String.format( "TOP GAMES (%d total)", games.size() ));
    for (Game g : sorted_map.keySet()) {      
      System.out.println( String.format( "%s users: game %s, %s achievements", top.get(g), g.appID, g.achievements().size() ) );
    }
   
    System.out.println("ALL USERS");
    
    for (User u : users) {
      System.out.println(u);
    }
    
  }
  

  
  static class ValueComparator implements Comparator<Game> {
    
    Map<Game, Integer> base;
    public ValueComparator(Map<Game, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Game a, Game b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
  }
  
}
