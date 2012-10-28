package steam.viztools.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {

	public final String steam64id;
	
	private Set<Game> games;
	private Map<Game,Set<Achievement>> achievements;

	/**
	 * ID based constructor. Assumes games for this User will be added one at a time,
	 * e.g. as part of a scraping process.
	 */
	public User(String s64id) {
		this.steam64id = s64id;
		this.games = new HashSet<Game>();
		this.achievements = new HashMap<Game,Set<Achievement>>();
	}

	public void addGame(Game g, Set<Achievement> achs) {
		games.add(g);
		achievements.put(g, achs);
	}
	
	public Set<Game> getGames() {
		return Collections.unmodifiableSet(games);
	}

	public Map<Game, Set<Achievement>> getAchs() {
		return Collections.unmodifiableMap(achievements);
	}

	@Override
	public String toString() {
		
	  int totalGames = games.size();
	  int totalAchs = 0;
	  int totalSuccess = 0;
	  int totalFail = 0;
		
		for (Game g: games) {
			
			Set<Achievement> gAchs = achievements.get(g);

			for (Achievement a : gAchs) {
			  totalAchs ++;
			  
			  if (a.achieved) {
			    totalSuccess ++;
			  } else {
			    totalFail ++;
			  }
			  
			}
		}
		
		String tos = String.format(
			"Id:%s\tGames:%d\tAchievements: %d (%d/%d)",
			steam64id, totalGames, totalAchs, totalSuccess, totalFail);
		
		return tos;
	}

	
}
