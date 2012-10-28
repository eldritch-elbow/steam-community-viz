package steam.viztools.scraper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;


import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import steam.viztools.model.Achievement;
import steam.viztools.model.Game;
import steam.viztools.model.User;

public class UserScraper {


	public User scrape(String steam64id)
			throws ClientProtocolException, IOException,
			ParserConfigurationException, SAXException {
		
		System.out.println("***** Scraping user "+steam64id+" *****");

		/*** Define scrapers ***/
		UserGameScraper us = new UserGameScraper();
		UserAchievementScraper uas = new UserAchievementScraper();
		
		/*** Scrape games and achievements ***/
		User user = new User(steam64id);

		us.scrape(steam64id);
		
		for (Game g : us.getGames()) {
			
			uas.scrape(steam64id, g.appID);
			Set<Achievement> achievements = new HashSet<Achievement>(uas.getAchievements());
			
			user.addGame(g, achievements);			
		}
		
		return user;
	}

	
}
