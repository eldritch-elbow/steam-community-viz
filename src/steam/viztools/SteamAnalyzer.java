package steam.viztools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class SteamAnalyzer {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {

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

		Map<Game, Integer> gameData = new HashMap<Game,Integer>();
		
		for (User u : users) {
	    System.out.println(u);	
	    
	    for (Game g : u.getGames()) {
	      
	      Integer count = gameData.get(g);	      
	      count = count == null ? 1 : count + 1;
	      
	      gameData.put(g, count);
	    }
	    
		}
		
    
    /*
     * Create a graph from the game data
     */
    Graph graph = new TinkerGraph();
    for (Game g : gameData.keySet()) {
      
      Integer count = gameData.get(g);
      
      Vertex gameVert = graph.addVertex(null);
      gameVert.setProperty("appID", g.appID);
      gameVert.setProperty("name", g.name);
      gameVert.setProperty("count", count);

    }

    //Edge e = graph.addEdge(null, a, b, "knows");
    

    FileOutputStream out = new FileOutputStream(outFileGraph);
    GMLWriter.outputGraph(graph, out);
    
		
	}

	
	
	
	
	
//Set<Game> sharedGames = Sets.intersection(jimmyjazz.getGames(), kapkapkap.getGames());    
//Set<Game> jjOnlyGames = Sets.difference(jimmyjazz.getGames(), kapkapkap.getGames());
//Set<Game> kkkOnlyGames = Sets.difference(kapkapkap.getGames(), jimmyjazz.getGames());
//
//System.out.println( String.format( "%d shared games: %s",sharedGames.size(),sharedGames) );
//System.out.println( String.format( "%d user 1 only: %s",jjOnlyGames.size(),jjOnlyGames) );
//System.out.println( String.format( "%d user 2 only: %s",kkkOnlyGames.size(),kkkOnlyGames) );


}
