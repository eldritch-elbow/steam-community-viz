package steam.viztools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import steam.viztools.model.Game;
import steam.viztools.model.User;
import steam.viztools.serialize.DataSerializer;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLWriter;

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
	  String inFilename = args[0];
	  String outFilename1 = String.format("data%s%s.gml", Constants.FILE_SEP, args[1] );
	  
		File infile = new File(inFilename);
		File outFileGraph = new File(outFilename1);
		
		/*
		 *  Read user data, derive further data
		 */
		FileInputStream fis = new FileInputStream(infile);
		
		DataSerializer ds = new DataSerializer();
		Set<User> users = ds.readUsers(fis);

		Map<Game, Integer> gameData = deriveTopGames(users);
		
    /*
     * Create a graph from the game data
     */
    Graph graph = new TinkerGraph();
    for (Game g : gameData.keySet()) {
      
      Integer count = gameData.get(g);
      
      Vertex gameVert = graph.addVertex(null);
      gameVert.setProperty("appID", g.appID);
      gameVert.setProperty("name", g.name());
      gameVert.setProperty("count", count);

    }

    FileOutputStream out = new FileOutputStream(outFileGraph);
    GMLWriter.outputGraph(graph, out);
		
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
