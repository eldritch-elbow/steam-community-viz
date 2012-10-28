package steam.viztools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;

import steam.viztools.model.User;
import steam.viztools.serialize.DataSerializer;

public class SteamAnalyzer {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {

		File infile = new File(args[0]);		
		FileInputStream fis = new FileInputStream(infile);
		
		DataSerializer ds = new DataSerializer();
		Set<User> users = ds.readUsers(fis);

		for (User u : users) {
		  System.out.println(u);
		}
		
		
//  Set<Game> sharedGames = Sets.intersection(jimmyjazz.getGames(), kapkapkap.getGames());    
//  Set<Game> jjOnlyGames = Sets.difference(jimmyjazz.getGames(), kapkapkap.getGames());
//  Set<Game> kkkOnlyGames = Sets.difference(kapkapkap.getGames(), jimmyjazz.getGames());
//  
//  System.out.println( String.format( "%d shared games: %s",sharedGames.size(),sharedGames) );
//  System.out.println( String.format( "%d user 1 only: %s",jjOnlyGames.size(),jjOnlyGames) );
//  System.out.println( String.format( "%d user 2 only: %s",kkkOnlyGames.size(),kkkOnlyGames) );

		
	}

}
