package steam.viztools.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import steam.viztools.model.Achievement;
import steam.viztools.model.Game;
import steam.viztools.model.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Data serialization support. Uses Google Gson to store gamer data,
 * then retrieve it for offline processing.
 * 
 * NOTE: user data retrieval is currently unsupported, due to some tweaks
 * to the core domain model. 
 */
public class DataSerializer {

  private Gson gson = new Gson();

  /**
   * Write the given set of User objects to the given output stream, in JSON format
   */
  public void writeUsers(Set<User> users, OutputStream os) {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String json = gson.toJson(users);
    byte[] jbytes = json.getBytes();

    try {
      os.write(jbytes);
    } catch (IOException e) {
      throw new RuntimeException(
          "IO exception detected while writing user json data", e);
    }

  }

  /**
   * Write the given set of Game objects to the given output stream, in JSON format
   */
  public void writeGames(Set<Game> games, OutputStream os) {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String json = gson.toJson(games);
    byte[] jbytes = json.getBytes();

    try {
      os.write(jbytes);
    } catch (IOException e) {
      throw new RuntimeException(
          "IO exception detected while writing game json data", e);
    }

  }

  public void writeGame(Game game, OutputStream os) {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String json = gson.toJson(game);
    byte[] jbytes = json.getBytes();

    try {
      os.write(jbytes);
    } catch (IOException e) {
      throw new RuntimeException(
          "IO exception detected while writing game json data", e);
    }

  }

  /**
   * Reads a list of User objects from the given input stream.
   */
  public Set<User> readUsers(InputStream is) throws IOException {

    JsonReader jr = new JsonReader(new InputStreamReader(is));
    return readUserArray(jr);
  }

  public Set<Game> readGameSet(InputStream is) throws IOException {
    
    JsonReader jr = new JsonReader( new InputStreamReader(is) );    
    JsonParser parser = new JsonParser();    
    JsonArray array = parser.parse(jr).getAsJsonArray();
    
    Set<Game> games = new HashSet<Game>();
    
    for (JsonElement jsel : array) {
      Game g = gson.fromJson(jsel, Game.class);
      games.add(g);
    }
    
    return games;
  }
  
  /*
   * Private JSON parsing methods
   */

  private Set<User> readUserArray(JsonReader jr) throws IOException {

    Set<User> users = new HashSet<User>();

    jr.beginArray();
    while (jr.hasNext()) {
      users.add( readUser(jr) );
    }
    jr.endArray();

    return users;

  }

  private User readUser(JsonReader jr) throws IOException {

    String steamID = null;
    Set<Game> games = null;
    Map<String, Set<Achievement>> achievements = null;
    
    jr.beginObject();

    while (jr.hasNext()) {
    
      String name = jr.nextName();
      
      if (name.equals("steam64id")) {
  
        steamID = jr.nextString();      
        System.out.println( String.format("Steam ID: %s", steamID) );
        
      } else if (name.equals("games")) {
  
        games = readGameSet(jr);
        System.out.println( String.format("Games: %s", games) );
  
      } else if (name.equals("achievements")) {
  
        achievements = readUserAchievements(jr);
      }
      
    }
    
    jr.endObject();
    
    if (steamID == null || games == null || achievements == null) {
      throw new RuntimeException( String.format( "Invalid data detected for user: %s", steamID ));
    }
    
    // Construct the actual in-memory User representation from the various pieces    
    User user = new User(steamID, null);
    for (Game g : games) {
      user.addGame(g, null, null);
    }
    
    return user;
  }


  private Map<String, Set<Achievement>> readUserAchievements(JsonReader jr) throws IOException {

    Map<String, Set<Achievement>> uAchs = new HashMap<String, Set<Achievement>>();
        
    jr.beginObject();
    while (jr.hasNext()) {

      String gameId = jr.nextName();
      Set<Achievement> achs = readAchievementSet(jr);
      
      System.out.println( String.format("%s:\t%s", gameId, achs) );
      
      uAchs.put(gameId, achs);      
    }
    jr.endObject();
    
    return uAchs;
  }


  private Set<Game> readGameSet(JsonReader jr) throws IOException {
    
    Type collectionType = new TypeToken<Set<Game>>(){}.getType();
    return gson.fromJson(jr, collectionType);
    
  }
  
  private Set<Achievement> readAchievementSet(JsonReader jr) throws IOException {
    
    Type collectionType = new TypeToken<Set<Achievement>>(){}.getType();
    return gson.fromJson(jr, collectionType);

  }

}
