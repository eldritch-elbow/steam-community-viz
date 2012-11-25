package steam.viztools.serialize;

import static steam.viztools.Constants.FILE_SEP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import steam.viztools.model.Game;

/**
 * Prepares data storage streams
 */
public class Persistence {

  public static OutputStream prepGameFile(String dataset) throws Exception {

    String outGameFilename = "data" + FILE_SEP + dataset + "_games.json";

    File outFile;
    FileOutputStream fos;

    outFile = new File(outGameFilename);
    fos = new FileOutputStream(outFile);

    System.out.println("Writing serialized game data to: " + outFile.getAbsolutePath());

    return fos;
  }
  
}
