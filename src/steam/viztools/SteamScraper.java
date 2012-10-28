package steam.viztools;

import static steam.viztools.Constants.FILE_SEP;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import steam.viztools.model.Game;
import steam.viztools.model.User;
import steam.viztools.scraper.UserScraper;
import steam.viztools.serialize.DataSerializer;

import com.google.common.collect.Sets;

public class SteamScraper {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			throw new RuntimeException("Expected two arguments");
		}
		
		/** Scrape data for users */
		System.out.println("***** SCRAPE DATA *****");

		UserScraper us = new UserScraper();		
		Set<User> users = new HashSet<User>();

		List<String> idLines = FileUtils.readLines( new File(args[0]) );
		for (String steamID : idLines ) {
			User user = us.scrape(steamID);
			users.add(user);			
		}
				
		/*** Display summaries ***/
		for (User u : users) {
		  System.out.println(u);
		}
		
		/** Write data to file in standard format */
		System.out.println("***** STORE DATA *****");
		
		String outFilename = "data" + FILE_SEP + args[1] + ".json";
		File outFile = new File(outFilename);
		
		System.out.println("Writing serialized user data to: "+outFile.getAbsolutePath());

		FileOutputStream fos = new FileOutputStream( outFile );		
		DataSerializer dw = new DataSerializer();		
		dw.writeUsers(users, fos);
		
		fos.close();
	}

}
