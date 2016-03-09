import javafx.scene.layout.BorderStroke;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by matt.eglin on 03/03/2016.
 */
public class CalWindow extends JFrame implements WindowListener,ActionListener {

    EventMaker defaultEventMaker;
    JButton createButton = new JButton();
    JButton postButton = new JButton();
    JTextField listBackline = new JTextField();
    JTextField listFrontline = new JTextField();

    public CalWindow(EventMaker eventMaker) {

        this.defaultEventMaker = eventMaker;

        this.setVisible(true);
        this.setSize(300,300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createButton.setText("Run PDF Test");
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

        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("Enter a CSV list of names");
        descriptionLabel.setAlignmentX(10);
        //descriptionLabel.setSize(300,25);

        JLabel blLabel = new JLabel();
        blLabel.setText("Backline");
        blLabel.setAlignmentX(50);

        JLabel flLabel = new JLabel();
        flLabel.setText("Frontline");
        flLabel.setAlignmentX(50);

        JPanel panel = new JPanel();
        panel.setSize(300,300);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(descriptionLabel);
        panel.add(flLabel);
        panel.add(listFrontline);
        panel.add(blLabel);
        panel.add(listBackline);
        panel.add(createButton);
        panel.add(postButton);

        this.add(panel);


    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(createButton))
        {
            System.out.println("Create Button!");

            if (listBackline.getText().isEmpty()) {
                System.out.println("Need to populate Backline");

            }
                else{
                    if (listFrontline.getText().isEmpty())
                    {
                        System.out.println("Need to populate Frontline");

                    }
                else {
                        defaultEventMaker.populateBL(listBackline.getText());
                        defaultEventMaker.populateFL(listFrontline.getText());

                        defaultEventMaker.runPDFTest();
                    }

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
