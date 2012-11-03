package steam.viztools.model;

/**
 * Immutable class representing an achievement in a game.
 */
public class Achievement {

  /** Achievement ID */
	public final String id;

	/**
	 * Factory method for immutable class
	 */
	public static Achievement fromString(String s) {
	  
	  if (s == null) {
	    return null;
	  }
	  
	  String candidateID = s.toLowerCase().trim();
	  
	  if (candidateID.length() == 0) {
      return null;
    }
	  
	  if (candidateID.equals("null")) {
	    return null;
	  }
	  
    if (candidateID.contains("test")) {
      return null;
    }
	  
	  return new Achievement(candidateID);
	}
	
	private Achievement(String id) {
		super();
		this.id = id;
	}

  @Override
  public String toString() {
    return id;
  }
  

  /*
   * Auto-generated methods - DO NOT MODIFY MANUALLY
   */
	
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    Achievement other = (Achievement) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

	
}
