/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jchess.display.windows;

import jchess.JChessApp;
import jchess.core.Game;
import jchess.core.Player;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.TextListener;
import java.awt.event.TextEvent;
import java.awt.*;

import javax.swing.text.BadLocationException;

import jchess.utils.Settings;
import jchess.utils.TimeFrame;

import org.apache.log4j.Logger;

/**
 * @author: Mateusz Sławomir Lach ( matlak, msl )
 * Class responsible for drawing the fold with local game settings
 */
public class DrawLocalSettings extends JPanel implements ActionListener, TextListener
{

    private static final Logger LOG = Logger.getLogger(DrawLocalSettings.class);
    
    JDialog parent;//needet to close newGame window
    JComboBox color;//to choose color of player
    JRadioButton oponentComp;//choose oponent
    JRadioButton oponentHuman;//choose oponent (human)
    ButtonGroup oponentChoos;//group 4 radio buttons
    JFrame localPanel;
    JLabel compLevLab;
    JSlider computerLevel;//slider to choose jChess Engine level
    JTextField firstName;//editable field 4 nickname
    JTextField secondName;//editable field 4 nickname
    JLabel firstNameLab;
    JLabel secondNameLab;
    JCheckBox upsideDown;//if true draw chessboard upsideDown(white on top)
    GridBagLayout gbl;
    GridBagConstraints gbc;
    Container cont;
    JSeparator sep;
    JButton okButton;

    String colors[] =
    {
        Settings.lang("white"), Settings.lang("black")
    };
    
    //Ajout d'une classe Time permettant les r�glages du temps de partie
    
    TimeFrame time;
        
    /** 
     * Method witch is checking correction of edit tables
     * @param e Object where is saving this what contents edit tables
     */
    @Override
    public void textValueChanged(TextEvent e)
    {
        Object target = e.getSource();
        if (target == this.firstName || target == this.secondName)
        {
            JTextField temp = new JTextField();
            if (target == this.firstName)
            {
                temp = this.firstName;
            }
            else if (target == this.secondName)
            {
                temp = this.secondName;
            }

            int len = temp.getText().length();
            if (len > 8)
            {
                try
                {
                    temp.setText(temp.getText(0, 7));
                }
                catch (BadLocationException exc)
                {
                    LOG.error("BadLocationException: Something wrong in editables, msg: " + exc.getMessage() + " object: " + exc);
                }
            }
        }
    }

    /** Method responsible for changing the options which can make a player
     * when he want to start new local game
     * @param e where is saving data of performed action
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object target = e.getSource(); 
        if (target == this.oponentComp) //toggle enabled of controls depends of oponent (if computer)
        {
            this.computerLevel.setEnabled(true);//enable level of computer abilities
            this.secondName.setEnabled(false);//disable field with name of player2
        }
        else if (target == this.oponentHuman) //else if oponent will be HUMAN
        {
            this.computerLevel.setEnabled(false);//disable level of computer abilities
            this.secondName.setEnabled(true);//enable field with name of player2
        }
        else if (target == this.okButton) //if clicked OK button (on finish)
        {
            if (this.firstName.getText().length() > 9) //make names short to 10 digits
            {
                this.firstName.setText(this.trimString(firstName, 9));
            }
            if (this.secondName.getText().length() > 9)  //make names short to 10 digits
            {
                this.secondName.setText(this.trimString(secondName, 9));
            }
            if (!this.oponentComp.isSelected()
                    && (this.firstName.getText().length() == 0 || this.secondName.getText().length() == 0))
            {
                JOptionPane.showMessageDialog(this, Settings.lang("fill_names"));
                return;
            }
            if ((this.oponentComp.isSelected() && this.firstName.getText().length() == 0))
            {
                JOptionPane.showMessageDialog(this, Settings.lang("fill_name"));
                return;
            }
            
            JChessApp.demarrerJeu(this.firstName.getText(), this.secondName.getText(), color.getActionCommand().equals("biały"), oponentComp.isSelected(), 
            		upsideDown.isSelected(), time.getTimeGame().isSelected(), time.getTimes()[time.getTime4Game().getSelectedIndex()], time.getTime4Game().getActionCommand(), parent);
            
            
        }
    }

    public DrawLocalSettings(JDialog parent)
    {
        super();
        //this.setA//choose oponent
        this.parent = parent;
        this.color = new JComboBox(colors);
        this.gbl = new GridBagLayout();
        this.gbc = new GridBagConstraints();
        this.sep = new JSeparator();
        this.okButton = new JButton(Settings.lang("ok"));
        this.compLevLab = new JLabel(Settings.lang("computer_level"));

        this.firstName = new JTextField("", 10);
        this.firstName.setSize(new Dimension(200, 50));
        this.secondName = new JTextField("", 10);
        this.secondName.setSize(new Dimension(200, 50));
        this.firstNameLab = new JLabel(Settings.lang("first_player_name") + ": ");
        this.secondNameLab = new JLabel(Settings.lang("second_player_name") + ": ");
        this.oponentChoos = new ButtonGroup();
        this.computerLevel = new JSlider();
        this.upsideDown = new JCheckBox(Settings.lang("upside_down"));
        
        time = new TimeFrame();

        this.oponentComp = new JRadioButton(Settings.lang("against_computer"), false);
        this.oponentHuman = new JRadioButton(Settings.lang("against_other_human"), true);

        this.setLayout(gbl);
        this.oponentComp.addActionListener(this);
        this.oponentHuman.addActionListener(this);
        this.okButton.addActionListener(this);

        this.secondName.addActionListener(this);

        this.oponentChoos.add(oponentComp);
        this.oponentChoos.add(oponentHuman);
        this.computerLevel.setEnabled(false);
        this.computerLevel.setMaximum(3);
        this.computerLevel.setMinimum(1);

        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
        this.gbc.insets = new Insets(3, 3, 3, 3);
        this.gbl.setConstraints(oponentComp, gbc);
        this.add(oponentComp);
        this.gbc.gridx = 1;
        this.gbl.setConstraints(oponentHuman, gbc);
        this.add(oponentHuman);
        this.gbc.gridx = 0;
        this.gbc.gridy = 1;
        this.gbl.setConstraints(firstNameLab, gbc);
        this.add(firstNameLab);
        this.gbc.gridx = 0;
        this.gbc.gridy = 2;
        this.gbl.setConstraints(firstName, gbc);
        this.add(firstName);
        this.gbc.gridx = 1;
        this.gbc.gridy = 2;
        this.gbl.setConstraints(color, gbc);
        this.add(color);
        this.gbc.gridx = 0;
        this.gbc.gridy = 3;
        this.gbl.setConstraints(secondNameLab, gbc);
        this.add(secondNameLab);
        this.gbc.gridy = 4;
        this.gbl.setConstraints(secondName, gbc);
        this.add(secondName);
        this.gbc.gridy = 5;
        this.gbc.insets = new Insets(0, 0, 0, 0);
        this.gbl.setConstraints(compLevLab, gbc);
        this.add(compLevLab);
        this.gbc.gridy = 6;
        this.gbl.setConstraints(computerLevel, gbc);
        this.add(computerLevel);
        this.gbc.gridy = 7;
        this.gbl.setConstraints(upsideDown, gbc);
        this.add(upsideDown);
        this.gbc.gridy = 8;
        this.gbc.gridwidth = 1;
        this.gbl.setConstraints(time.getTimeGame(), gbc);
        this.add(time.getTimeGame());
        this.gbc.gridx = 1;
        this.gbc.gridy = 8;
        this.gbc.gridwidth = 1;
        this.gbl.setConstraints(time.getTime4Game(), gbc);
        this.add(time.getTime4Game());
        this.gbc.gridx = 1;
        this.gbc.gridy = 9;
        this.gbc.gridwidth = 0;
        this.gbl.setConstraints(okButton, gbc);
        this.add(okButton);
        this.oponentComp.setEnabled(false);//for now, becouse not implemented!

    }

    /**
     * Method responsible for triming white symbols from strings
     * @param txt Where is capt value to equal
     * @param length How long is the string
     * @return result trimmed String
     */
    public String trimString(JTextField txt, int length)
    {
        String result = new String();
        try
        {
            result = txt.getText(0, length);
        }
        catch (BadLocationException exc)
        {
            LOG.error("BadLocationException: Something wrong in trimString: \n" + exc);
        }
        return result;
    }
}