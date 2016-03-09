import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import java.text.DateFormatSymbols;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by matt.eglin on 29/02/2016.
 */
public class EventMaker {


    String[] backline;
    String[] frontline;

    Calendar globalCal = Calendar.getInstance();


    ArrayList<String> pairings = new ArrayList<String>();

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

    public void populateFL(String population){

            this.frontline = population.split(",");


    }

    public void populateBL(String population){

        this.backline = population.split(",");


    }

    //Internal method to randomise the order of the Arraylist?
    private void randomise(){

    }

    private void createPairings() {

        //Pair each Frontliner with backline

        for (int x=0;x<= backline.length-1;x++) {

            for (int y=x+1;y<= frontline.length-1;y++) {
                pairings.add(backline[x] + " / " + frontline[y]);
            }

        }


        //Pair Backline with Backline

        for (int x=0;x<= backline.length-1;x++) {

            for (int y=x+1;y<= backline.length-1;y++) {
                pairings.add(backline[x] + " / " + backline[y]);
                // Both ways around
                pairings.add(backline[y] + " / " + backline[x]);
            }

        }
    }

    public void runPDFTest(){
        this.createPDF();
    }

    private void drawCalendar(PDDocument targetDoc) {

        Iterator<String> pairingIterator = pairings.iterator();

        //Iterate for 12 months
        for (int monthInt=0;monthInt<=11;monthInt++) {

            Calendar localCal = Calendar.getInstance();

            float tableWidth;
            float tableHeight;

            float cellX;
            float cellY;


            PDPage page = new PDPage();

            page.setMediaBox(PDPage.PAGE_SIZE_A4);
            page.setRotation(90);

            targetDoc.addPage(page);

            PDFont font = PDType1Font.HELVETICA;

            PDRectangle mediaBox = page.findMediaBox();

            tableHeight = mediaBox.getHeight() - 20f;
            tableWidth = mediaBox.getWidth() - 20f;

            try {

                PDPageContentStream contentStream = new PDPageContentStream(targetDoc, page);
                contentStream.concatenate2CTM(0, 1, -1, 0, mediaBox.getWidth(),
                        0); // Translate starting location for landscape at 0,0 lower corner

                contentStream.setStrokingColor(Color.BLACK);

                //Add Week boxes

                int weeks = 4;

                ArrayList<Integer> eventDays = new ArrayList();
                eventDays.add(new Integer(3));

                eventDays.add(new Integer(5));


                for (int x = 100; x <= 700; x += 100) {
                    //days in the week
                    for (int y = 100; y <= (weeks * 100); y += 100) {

                        //number of weeks
                        contentStream.addRect(x, y, 100, 100);
                        if (!pairingIterator.hasNext()) {
                            pairingIterator = pairings.iterator(); //If we're empty, re-initialise the pairngs
                        }
                        //Need to add parings every selected day
                        if (eventDays.contains(new Integer(x / 100))) {
                            contentStream.beginText();
                            contentStream.setFont(font, 10);
                            contentStream.moveTextPositionByAmount(x + 5, y + 50);
                            contentStream.drawString(pairingIterator.next().toString()); //Draw in a pairing
                            contentStream.endText();
                        }


                    }
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.moveTextPositionByAmount(x + 5, 520);
                    contentStream.drawString(new DateFormatSymbols().getWeekdays()[(x / 100)]);
                    contentStream.endText();


                }
                //drawCalendarGrid();
                contentStream.beginText();
                contentStream.setFont(font, 16);
                contentStream.moveTextPositionByAmount(10, 550);
                contentStream.drawString(new DateFormatSymbols().getMonths()[localCal.MONTH]);

                contentStream.endText();

                contentStream.closeAndStroke();
                contentStream.close();


            }
            catch (IOException ioe) {
                //Failed to write out to PDF
            }
            //Finally, move to the next month
            localCal.add(Calendar.MONTH, 1);
        }

    }

    public void populateNames(String names){

    }



    private void createPDF() {

        if (this.pairings.isEmpty()) {
            this.createPairings();
        }
        Iterator pairingIterator = pairings.iterator();

        PDDocument outputDocument = new PDDocument();

        drawCalendar(outputDocument);


        try {

            outputDocument.save("/Users/matt.eglin/Desktop/Test.pdf");

            outputDocument.close();
        }
        catch (IOException ioe) {
            //ooops
        }
        catch (COSVisitorException cve) {
            //double oops
        }



    }

}
