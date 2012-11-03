package steam.viztools;


/**
 * Various constants related to retrieving and vizualizing gamer data
 */
public class Constants {

	public static String FILE_SEP = System.getProperty("file.separator");
	public static String API_KEY = System.getProperty("steam.viztools.apikey");
	
  static {
    System.out.println("Using steam API key: " + API_KEY);
  }
	
}
