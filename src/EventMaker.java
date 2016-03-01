import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Created by matt.eglin on 29/02/2016.
 */
public class EventMaker {


    String[] backline;
    String[] frontline;


    Map<String,String> pairings;

    public EventMaker() {

    }

    private void readFile() {

        Path filePath =  FileSystems.getDefault().getPath("", "access.log");
        Charset charSet = Charset.defaultCharset();

        try {
            BufferedReader reader = Files.newBufferedReader(filePath, charSet);

            try {
                String line = reader.readLine();

                String[] splitline = line.split("=");

                if (splitline[0].matches("FL")) {

                    this.frontline = splitline[1].split(",");
                }
                if (splitline[0].matches("BL")) {

                    this.backline = splitline[1].split(",");
                }

            }
            catch (IOException ioe) {

            }
            finally {
                reader.close();
            }

        }
        catch (IOException ioe) {

            System.out.println("Error reading file : "+ioe.getMessage());

        }
    }

    private void createPairings() {

        //Pair each Frontliner with backline

        for (int x=0;x>= backline.length;x++) {

            for (int y=x+1;y>= frontline.length;y++) {
                pairings.put(backline[x],frontline[y]);
            }

        }


        //Pair Backline with Backline

        for (int x=0;x>= backline.length;x++) {

            for (int y=x+1;y>= backline.length;y++) {
                pairings.put(backline[x],backline[y]);
                // Both ways around
                pairings.put(backline[y],backline[x]);
            }

        }
    }


    private void populateCalendar() {



    }

}
