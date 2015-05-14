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

package jchess;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import java.awt.PopupMenu;
import java.awt.Window;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

import jchess.core.Game;
import jchess.core.Player;
import jchess.display.windows.DrawLocalSettings;
import jchess.utils.Settings;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The main class of the application.
 */
public class JChessApp extends SingleFrameApplication {
    
    protected static JChessView javaChessView; 
     
    private static final Logger LOG = Logger.getLogger(DrawLocalSettings.class);
    public final static String LOG_FILE = "log4j.properties"; 
    
    public final static String MAIN_PACKAGE_NAME = "jchess";

    /**
     * @return the jcv
     */
    public static JChessView getJavaChessView()
    {
        return javaChessView;
    }
     
    /**
     * At startup create and show the main frame of the application.
     */
    @Override 
    protected void startup() 
    {
        javaChessView = new JChessView(this);
        show(getJavaChessView());
        
        String nomPl1="";
    	String nomPl2="";
    	
		Random r = new Random();

        String alphabet = "123456789abcdefghijkxyz";
        for (int i = 0; i < 5; i++) {
           nomPl1+=alphabet.charAt(r.nextInt(alphabet.length()));
           nomPl2+=alphabet.charAt(r.nextInt(alphabet.length()));
        } // prints 50 random characters from alphabet
        
        nomPl1+="_White";
        nomPl2+="_Black";     
        
       
       demarrerJeu(nomPl1, nomPl2, true, false, false, false,"","",null);

    }

    /**
     * Méthode pour démarrer un jeu automatique avec 2 joueur en local
     */
    public static void demarrerJeu(String name1, String name2, Boolean color, Boolean oponentComp, 
    		Boolean upsideDown, Boolean timeGame,String valeur, String time4Game , JDialog parent){
    	
    	Game newGUI = JChessApp.getJavaChessView().addNewTab(name1 + " vs " + name2);
        //newGUI.getChat().setEnabled(false);
        
        Settings sett = newGUI.getSettings();//sett local settings variable
        Player pl1 = sett.getPlayerWhite();//set local player variable
        Player pl2 = sett.getPlayerBlack();//set local player variable
        sett.setGameMode(Settings.gameModes.newGame);
        
        //if(this.firstName.getText().length() >9 ) this.firstName.setText(this.firstName.getText(0,8));
        
        //TODO: investigate and refactor
        if (color) //if first player is white
        {
            pl1.setName(name1);//set name of player
            pl2.setName(name2);//set name of player
        }
        else //else change names
        {
            pl2.setName(name1);//set name of player
            pl1.setName(name2);//set name of player
        }
        pl1.setType(Player.playerTypes.localUser);//set type of player
        pl2.setType(Player.playerTypes.localUser);//set type of player
        sett.setGameType(Settings.gameTypes.local);
        if (oponentComp) //if computer oponent is checked
        {
            pl2.setType(Player.playerTypes.computer);
        }
        sett.setUpsideDown(upsideDown);
                
        if (timeGame) //if timeGame is checked
        {
            String value = valeur;//set time for game
            Integer val = new Integer(value);
            sett.setTimeForGame((int) val * 60);//set time for game and mult it to seconds
            newGUI.getGameClock().setTimes(sett.getTimeForGame(), sett.getTimeForGame());
            newGUI.getGameClock().start();
        }
        LOG.debug("this.time4Game.getActionCommand(): " + time4Game);
        //this.time4Game.getComponent(this.time4Game.getSelectedIndex());
        LOG.debug("****************\nStarting new game: " + pl1.getName() + " vs. " + pl2.getName()
                + "\ntime 4 game: " + sett.getTimeForGame() + "\ntime limit set: " + sett.isTimeLimitSet()
                + "\nwhite on top?: " + sett.isUpsideDown() + "\n****************");//4test
        
       newGUI.newGame();//start new Game
        

        if(parent != null)
        	parent.setVisible(false);//hide parent
        JChessApp.getJavaChessView().getActiveTabGame().repaint();
        JChessApp.getJavaChessView().setActiveTabGame(JChessApp.getJavaChessView().getNumberOfOpenedTabs()-1);
    	

    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override 
    protected void configureWindow(Window root) {}

    /**
     * A convenient static getter for the application instance.
     * @return the instance of JChessApp
     */
    public static JChessApp getApplication() 
    {
        return Application.getInstance(JChessApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) 
    {
        launch(JChessApp.class, args);
        Properties logProp = new Properties();
        try
        {   
            logProp.load(JChessApp.class.getClassLoader().getResourceAsStream(LOG_FILE)); 
            PropertyConfigurator.configure(logProp);
        }
        catch (NullPointerException | IOException e)
        {
            System.err.println("Logging not enabled : "+e.getMessage());
        } 
    }
}
