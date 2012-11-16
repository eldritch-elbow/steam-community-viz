package steam.viztools.serialize;

import static steam.viztools.Constants.FILE_SEP;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

import steam.viztools.model.Game;

/**
 * Stores data sets into the local data folder
 */
public class Persistence {

  public static void storeGameSet(String dataset, Set<Game> games) throws Exception {
    
    DataSerializer dw = new DataSerializer();

    String outGameFilename = "data" + FILE_SEP + dataset + "_games.json";

    File outFile;
    FileOutputStream fos;

    outFile = new File(outGameFilename);
    fos = new FileOutputStream(outFile);

    System.out.println("Writing serialized game data to: " + outFile.getAbsolutePath());
    dw.writeGames(games, fos);
    fos.close();
  }


}
