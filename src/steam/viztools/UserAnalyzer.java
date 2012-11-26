package steam.viztools;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import steam.viztools.model.Achievement;
import steam.viztools.model.Game;
import steam.viztools.model.User;
import steam.viztools.serialize.DataSerializer;

/**
 * Command line program for analyzing previously retrieved gamer data
 */
public class UserAnalyzer {

	/**
	 * Main method. Requires arguments:
	 * 0: data file to read
	 * 1: output filename prefix
	 */
	public static void main(String[] args) throws Exception {

	  // TODO Add better CLI handling
	  
	  /*
	   *  Define filenames, create file objects
	   */
	  String inUserFilename = args[0];
    String inGameFilename = args[1];
	  
		
		/*
		 *  Read data, derive further data
		 */
		FileInputStream fisU = new FileInputStream( new File(inUserFilename) );		
    FileInputStream fisG = new FileInputStream( new File(inGameFilename) );   
		DataSerializer ds = new DataSerializer();
		
		Set<User> users = ds.readUsers(fisU);
    Set<Game> games = ds.readGameSet(fisG);
		Map<Game, Integer> gameData = deriveTopGames(users);
		
		/*
		 * Analyze / dump out interesting user data into R format 
		 */
		Map<String, Achievement> globalAchMap = new HashMap<String, Achievement>();
		for (Game g : games) {
		  for (Achievement a : g.achievements()) {	      
		    globalAchMap.put( String.format("%s:%s", g.appID, a.id) , a);		    
		  }
		}
		
    System.out.println("User\tAchievement\tRate");  

		for (User u : users) {
		  
		  for (Game ug : u.games()) {
		    
		    for (Achievement a : u.achieved(ug)) {

		      if (a == null) { // Exact reason for this stray data TBD
		        continue;
		      }		      
		      
		      Achievement globAch = globalAchMap.get( String.format("%s:%s", ug.appID, a.id) );
		      
		      if (globAch == null) {
		        throw new RuntimeException("No global record found for user achievement!");
		      }
		      
		      System.out.println(String.format("~%s~\t~%s~\t%.2f", u.name, a.id, globAch.globalRate()));
		      
		    }
		    
		  }
		  
		}
		
		
    /*
     * EXAMPLE: create a graph from the game data
     */
    /*
    String outFilename1 = String.format("data%s%s.gml", Constants.FILE_SEP, args[1] );
    File outFileGraph = new File(outFilename1);

 
    Graph graph = new TinkerGraph();
    for (Game g : gameData.keySet()) {
      
      Integer count = gameData.get(g);
      
      Vertex gameVert = graph.addVertex(null);
      gameVert.setProperty("appID", g.appID);
      gameVert.setProperty("count", count);

    }

    FileOutputStream out = new FileOutputStream(outFileGraph);
    GMLWriter.outputGraph(graph, out);
		*/
	}

  public static Map<Game, Integer> deriveTopGames(Set<User> users) {
    Map<Game, Integer> gameData = new HashMap<Game,Integer>();
		
		for (User u : users) {
	    System.out.println(u);	
	    
	    for (Game g : u.games()) {
	      
	      Integer count = gameData.get(g);	      
	      count = count == null ? 1 : count + 1;
	      
	      gameData.put(g, count);
	    }
	    
		}
    return gameData;
  }

}
