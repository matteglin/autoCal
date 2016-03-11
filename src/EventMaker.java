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
import java.util.Random;

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
    private ArrayList<String> getPairings(){

        ArrayList<String> originalPairings = new ArrayList<String>(pairings);
        ArrayList<String> newPairings = new ArrayList<String>();

        Random rand = new Random();
        int x = originalPairings.size();

        while ( x!=0) {

            int index = rand.nextInt(x);

            newPairings.add(originalPairings.remove(index));
            x--;

        }

        return newPairings;

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

    private void drawCalendar(PDDocument targetDoc, int months_to_generate, ArrayList<Integer> days_to_event_on) {

        Iterator<String> pairingIterator = this.getPairings().iterator();
        Calendar localCal = Calendar.getInstance();
        localCal.set(Calendar.DAY_OF_MONTH, 1); //start at the first day of the current month

        //Iterate for x months
        for (int monthInt=1;monthInt<=months_to_generate;monthInt++) {


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

            Calendar monthCal = (Calendar)localCal.clone();

            monthCal.set(Calendar.DAY_OF_MONTH, 1); //start at the first day of the current month

            int start_day_of_week = localCal.get(Calendar.DAY_OF_WEEK); //First day of the month falls on a...


            try {

                PDPageContentStream contentStream = new PDPageContentStream(targetDoc, page);
                contentStream.concatenate2CTM(0, 1, -1, 0, mediaBox.getWidth(),
                        0); // Translate starting location for landscape at 0,0 lower corner

                contentStream.setStrokingColor(Color.BLACK);


                //Add Week boxes

                int weeks = monthCal.getActualMaximum(Calendar.WEEK_OF_MONTH);

                System.out.println("Weeks = " + weeks);

                ArrayList<Integer> eventDays = new ArrayList(days_to_event_on);



                for (int x = 100; x <= 700; x += 100) {

                    //days in the week
                    int y_rect_pos = 20;
                    for (int y = 1; y <= 5; y += 1) {

                        //number of weeks

                        y_rect_pos+=75;

                        contentStream.addRect(x, y_rect_pos, 100, 75);






                    }
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.moveTextPositionByAmount(x + 5, 520);
                    contentStream.drawString(new DateFormatSymbols().getWeekdays()[(x / 100)]);
                    contentStream.endText();
                }
                //draw month name
                contentStream.beginText();
                contentStream.setFont(font, 16);
                contentStream.moveTextPositionByAmount(10, 550);
                System.out.println(monthCal.toString());
                contentStream.drawString(new DateFormatSymbols().getMonths()[monthCal.get(Calendar.MONTH)] + " " + String.valueOf(monthCal.get(Calendar.YEAR)));
                contentStream.endText();

                //Draw day numbers
                int x_pos = 110;
                int y_pos = 450;
                boolean start_day_count = false;
                int day_to_start_on = monthCal.get(Calendar.DAY_OF_WEEK);
                int count_days_for_week = 0;
                int total_day_count = 0;
                boolean month_finished = false;
                int day_count = 0;

                while (!month_finished) {
                     day_count++;
                        //Draw date number in rect
                        if ((start_day_count == false) && (day_to_start_on == day_count) && (month_finished == false)) {
                            start_day_count = true;
                        }
                        //Add date number
                        if (start_day_count && !month_finished) {

                            contentStream.beginText();
                            contentStream.setFont(font, 10);
                            contentStream.moveTextPositionByAmount(x_pos, y_pos);
                            contentStream.drawString(String.valueOf(monthCal.get(Calendar.DAY_OF_MONTH)));
                            contentStream.endText();
                            total_day_count++;
                            if (!pairingIterator.hasNext()) {
                                pairingIterator = this.getPairings().iterator(); //If we're empty, get new random Arraylist
                            }
                            //Need to add parings every selected day
                            if (eventDays.contains(new Integer(monthCal.get(Calendar.DAY_OF_WEEK)))) {

                                contentStream.beginText();
                                contentStream.setFont(font, 10);
                                contentStream.moveTextPositionByAmount(x_pos + 5, y_pos - 20);
                                contentStream.drawString(pairingIterator.next().toString()); //Draw in a pairing
                                contentStream.endText();
                            }
                            if (total_day_count >= monthCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                                //We've moved months so stop writing numbers
                                start_day_count = false;
                                month_finished = true;
                            }
                            monthCal.add(Calendar.DAY_OF_MONTH, 1);

                        }

                        x_pos += 100;
                        count_days_for_week++;
                        if (count_days_for_week == 7) {
                            y_pos -= 75;
                            count_days_for_week = 0;
                            x_pos = 110;
                    }
                }
                // Done - draw out and close
                contentStream.closeAndStroke();
                contentStream.close();

            }
            catch (IOException ioe) {
                //Failed to write out to PDF
            }

            localCal.add(Calendar.MONTH, 1);
        }

    }

    public void populateNames(String names){

    }



    public void createPDF(int months_to_generate,ArrayList<Integer> days_to_event_on, String fileOutput) {

        if (this.pairings.isEmpty()) {
            this.createPairings();
        }
        Iterator pairingIterator = pairings.iterator();

        PDDocument outputDocument = new PDDocument();

        drawCalendar(outputDocument,months_to_generate,days_to_event_on);


        try {

            outputDocument.save(fileOutput);

            outputDocument.close();
        }
        catch (IOException ioe) {
            //ooops
            System.out.println("Unable to comply - I/O Exception");
            System.out.println(ioe.getMessage());
        }
        catch (COSVisitorException cve) {
            //double oops
        }



    }

}
