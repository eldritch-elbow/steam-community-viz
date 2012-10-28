package steam.viztools.model;

public class Achievement {

	public final String id;
	public final boolean achieved;

	public Achievement(String id, boolean achieved) {
		super();
		this.id = id;
		this.achieved = achieved;
	}
	
	/* Overridden object behaviour - AUTO GENERATED, DO NOT MODIFY DIRECTLY */

	@Override
	public String toString() {
		return "Achievement [id=" + id + ", achieved=" + achieved + "]";
	}

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
