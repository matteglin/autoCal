import javafx.scene.layout.BorderStroke;

import javax.print.attribute.IntegerSyntax;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by matt.eglin on 03/03/2016.
 */
public class CalWindow extends JFrame implements WindowListener,ActionListener {

    EventMaker defaultEventMaker;
    JButton createButton = new JButton();
    JButton postButton = new JButton();
    JTextField listBackline = new JTextField();
    JTextField listFrontline = new JTextField();
    JComboBox<Integer> monthCombo = new JComboBox<Integer>();
    JRadioButton rdo_Monday = new JRadioButton();
    JRadioButton rdo_Tuesday = new JRadioButton();
    JRadioButton rdo_Wednesday = new JRadioButton();
    JRadioButton rdo_Thursday = new JRadioButton();
    JRadioButton rdo_Friday = new JRadioButton();
    JLabel errorLabel = new JLabel();

    JTextField fileOut = new JTextField();

    public CalWindow(EventMaker eventMaker) {

        this.defaultEventMaker = eventMaker;

        this.setVisible(true);
        this.setSize(300,300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createButton.setText("Create PDF");
        createButton.addActionListener(this);

        postButton.setText("Post to Jive");
        postButton.addActionListener(this);

        Dimension buttonDm = new Dimension();
        buttonDm.setSize(200,25);
        createButton.setMaximumSize(buttonDm);
        postButton.setMaximumSize(buttonDm);

        Dimension textFieldDM = new Dimension();
        textFieldDM.setSize(275,25);

        listBackline.setMaximumSize(textFieldDM);
        listBackline.setMinimumSize(textFieldDM);
        listBackline.setSize(textFieldDM);
        listFrontline.setMinimumSize(textFieldDM);
        listFrontline.setMaximumSize(textFieldDM);

        monthCombo.addItem(1);
        monthCombo.addItem(2);
        monthCombo.addItem(3);
        monthCombo.addItem(4);
        monthCombo.addItem(5);
        monthCombo.addItem(6);
        monthCombo.addItem(7);
        monthCombo.addItem(8);
        monthCombo.addItem(9);
        monthCombo.addItem(10);
        monthCombo.addItem(11);
        monthCombo.addItem(12);


        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("Enter a CSV list of names");
        descriptionLabel.setAlignmentX(10);
        //descriptionLabel.setSize(300,25);

        JLabel blLabel = new JLabel();
        blLabel.setText("Backline");
        blLabel.setAlignmentX(50);

        JLabel comboLabel = new JLabel();
        comboLabel.setText("Months to generate");
        comboLabel.setAlignmentX(50);

        JLabel flLabel = new JLabel();
        flLabel.setText("Frontline");
        flLabel.setAlignmentX(50);

        JLabel fileLabel = new JLabel();
        fileLabel.setText("Path to output");
        fileLabel.setAlignmentX(50);


        errorLabel.setText("");
        errorLabel.setAlignmentX(50);
        Dimension err_dm = new Dimension(200,25);
        errorLabel.setPreferredSize(err_dm);

        fileOut.setText("C:\\Users\\lee.kingsley\\Desktop\\calendar.pdf");


        rdo_Monday.setText("Monday");
        rdo_Tuesday.setText("Tuesday");
        rdo_Wednesday.setText("Wednesday");
        rdo_Thursday.setText("Thursday");
        rdo_Friday.setText("Friday");

        JPanel radio_panel = new JPanel();
        radio_panel.setLayout(new BoxLayout(radio_panel, BoxLayout.PAGE_AXIS));

        radio_panel.add(rdo_Monday);
        radio_panel.add(rdo_Tuesday);
        radio_panel.add(rdo_Wednesday);
        radio_panel.add(rdo_Thursday);
        radio_panel.add(rdo_Friday);

        JPanel panel = new JPanel();
        panel.setSize(300,300);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(descriptionLabel);
        panel.add(flLabel);
        panel.add(listFrontline);
        panel.add(blLabel);
        panel.add(listBackline);
        panel.add(comboLabel);
        panel.add(monthCombo);
        panel.add(fileLabel);
        panel.add(fileOut);
        panel.add(errorLabel);
        panel.add(createButton);
        panel.add(postButton);


        this.setLayout(new FlowLayout());
        this.add(panel);
        this.add(radio_panel);
        Dimension min_size = new Dimension(500,300);
        this.setMinimumSize(min_size);
        this.setSize(301,350);

    }

    public void actionPerformed(ActionEvent e) {

        boolean proceed = false;

        if (e.getSource().equals(createButton))
        {
            System.out.println("Create PDF Button!");

            if (listBackline.getText().isEmpty()) {
                errorLabel.setText(" ** Need to populate Backline **");
                proceed = false;

            }
            else{
                proceed = true;
            }
            if (listFrontline.getText().isEmpty())
            {
                errorLabel.setText("** Need to populate Frontline **");
                proceed = false;

            }
            else {
                proceed = true;
            }

            if (proceed) {

                defaultEventMaker.populateBL(listBackline.getText());
                defaultEventMaker.populateFL(listFrontline.getText());

                ArrayList<Integer> days = new ArrayList<Integer>();

                if (rdo_Monday.isSelected()) {
                    days.add(2);
                }
                if (rdo_Tuesday.isSelected()) {
                    days.add(3);
                }
                if (rdo_Wednesday.isSelected()) {
                    days.add(4);
                }
                if (rdo_Thursday.isSelected()) {
                    days.add(5);
                }
                if (rdo_Friday.isSelected()) {
                    days.add(6);
                }

                defaultEventMaker.createPDF(monthCombo.getSelectedIndex() + 1, days, this.fileOut.getText());
            }

        }
        else
        {
            System.out.println("Some other Button!");
        }

    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
}
