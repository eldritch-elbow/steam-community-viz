package steam.viztools.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Represents one Steam user
 */
public class User {

  /** Unique ID for the user */
	public final String steam64id;
	
	/** User profile name */
	public final String name;
		
	private Set<Game> games;
	private Map<Game,Set<Achievement>> achieved;
  private Map<Game,Set<Achievement>> notAchieved;

	/**
	 * ID based constructor. Assumes games for this User will be added one at a time,
	 * e.g. as part of a data harvesting process.
	 */
	public User(String s64id, String name) {
		this.steam64id = s64id;
		this.name = name;
		
		this.games = new HashSet<Game>();		
		this.achieved = new HashMap<Game,Set<Achievement>>();
    this.notAchieved = new HashMap<Game,Set<Achievement>>();
	}

	/**
	 * Add the given game to this user, with the given breakdown of achievements
	 * obtained and not obtained. 
	 */
	public void addGame(Game g, Set<Achievement> achs, Set<Achievement> notYet) {
		games.add(g);
		achieved.put(g, achs);
    notAchieved.put(g, notYet);
	}
	
	/** A setter to allow additionally discovered achievements to be recorded against the user */
	public void setNotAchieved(Game g, Achievement a) {
	  notAchieved.get(g).add(a);
	}
	
	/** Return the games this user has */
	public Set<Game> games() {
		return Collections.unmodifiableSet(games);
	}

	/** Returns the achievements held by the user for the given game */
	public Set<Achievement> achieved(Game g) {
		return Collections.unmodifiableSet(achieved.get(g));
	}

  /** Returns the achievements NOT held by the user for the given game */
  public Set<Achievement> notAchieved(Game g) {
    return Collections.unmodifiableSet(notAchieved.get(g));
  }

  /** Returns any achievements associated with this user/game */
  public Set<Achievement> anyAchievements(Game g) {    
    return Collections.unmodifiableSet( Sets.union(achieved.get(g), notAchieved.get(g)) );
  }

	@Override
	public String toString() {
		
	  // toString generates a human readable form of the User
	  
	  int totalGames = games.size();
	  int totalAchs = 0;
    int totalSuccess = 0;
    int totalFailed = 0;
		
		for (Game g: games) {			
		  
			Set<Achievement> achs;
			
			achs = achieved.get(g);			
			totalAchs += achs.size();
			totalSuccess += achs.size();
			
	    achs = notAchieved.get(g);     
	    totalAchs += achs.size();
	    totalFailed += achs.size();
		}
		
		String tos = String.format(
			"%s/%s\tGames:%d\tAchievements: %d (%d/%d)",
			steam64id, name, totalGames, totalAchs, totalSuccess, totalFailed);
		
		return tos;
	}

	
}
