package steam.viztools;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import steam.viztools.model.Achievement;
import steam.viztools.model.Game;
import steam.viztools.serialize.DataSerializer;

/**
 * Command line program for analyzing previously retrieved game data - mostly 
 * just transformational stuff
 */
public class GameAnalyzer {

	/**
	 * Main method. Requires arguments:
	 * 0: data file to read
	 * 1: output filename prefix
	 */
	public static void main(String[] args) throws Exception {

	  // TODO Add better CLI handling	  
		FileInputStream fis = new FileInputStream( new File(args[0]) );
		
		DataSerializer ds = new DataSerializer();
		Set<Game> games = ds.readGameSet(fis);
		
		/* Generate some useful data here - template code */
    System.out.println("Game\tAchievement\tRate");  
		for (Game g : games) {
		  
		  for (Achievement a : g.achievements()) {
		    
		    if (a.globalRate() < 0 || a.globalRate() > 100) {
		      continue;
		    }
		    
		    System.out.println(String.format("%s\t|%s|\t%.2f",g.appID, a.id, a.globalRate()));  
		  }
		  
		  
		}
		
		
	}


}
