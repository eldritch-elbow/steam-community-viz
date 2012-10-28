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
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class DataSerializer {

  Gson gson = new Gson();
  JsonParser parser = new JsonParser();

  public void writeUsers(Set<User> users, OutputStream os) {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String json = gson.toJson(users);
    byte[] jbytes = json.getBytes();

    try {
      os.write(jbytes);
    } catch (IOException e) {
      throw new RuntimeException(
          "IO exception detected while writing json data", e);
    }

  }

  public Set<User> readUsers(InputStream is) throws IOException {

    JsonReader jr = new JsonReader(new InputStreamReader(is));
    return readUserArray(jr);
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
    User user = new User(steamID);
    for (Game g : games) {
      user.addGame(g, achievements.get(g.appID));
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
