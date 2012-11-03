package steam.viztools.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a steam game.
 */
public class Game {

  /** The 'app ID' - a unique identifier for the game within steam */
	public final String appID;
	
	private String name;
	private Set<Achievement> achievements;
	
	/**
	 * App ID based constructor. Other details can be set later;
	 * this constructor allows game creation based on user game lists.
	 */
	public Game(String appID) {
		super();
		this.appID = appID;
		this.name = null;
		this.achievements = new HashSet<Achievement>();
	}

	/*
	 * Getters and setters - game objects may be created when
	 * first encountered for users, then filled in later
	 */
	
	/** Set the name of the game */
	public void setName(String name) {
	  this.name = name;
	}

	/** Add an achievement to the game */
	public void addAchievement(Achievement ach) {	  
	  achievements.add(ach);
	}
	
	/** The name of the game */
  public String name() {
    return name;
  }

  /** Returns an unmodifiable view of the achievements associated with the game */
  public Set<Achievement> achievements() {
    return Collections.unmodifiableSet( achievements );
  }

  @Override
  public String toString() {
    // toString just displays app ID, to help GSON persistence...
    return appID;
  }
	

	/* AUTO GENERATED. DO NOT MODIFY */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appID == null) ? 0 : appID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (appID == null) {
			if (other.appID != null)
				return false;
		} else if (!appID.equals(other.appID))
			return false;
		return true;
	}

}
