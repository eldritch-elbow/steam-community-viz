package steam.viztools.model;

public class Game {

	public final String appID;
	public final String name;

	public Game(String appID, String name) {
		super();
		this.appID = appID;
		this.name = name;
	}

	@Override
	public String toString() {
	  /* Default GSON serialization uses toString to serialize map keys ... */
		return appID;
	}


	/* Overridden object behaviour - AUTO GENERATED, DO NOT MODIFY DIRECTLY */
	
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
