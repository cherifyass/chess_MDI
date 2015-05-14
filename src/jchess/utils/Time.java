package jchess.utils;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class Time {
	
    JCheckBox timeGame;
   

	JComboBox time4Game;
    
    String times[] =
    {
        "1", "3", "5", "8", "10", "15", "20", "25", "30", "60", "120"
    };
    
    public Time(){
        this.timeGame = new JCheckBox(Settings.lang("Temps"));
        this.time4Game = new JComboBox(times);
    }

	public JCheckBox getTimeGame() {
		return timeGame;
	}

	public JComboBox getTime4Game() {
		return time4Game;
	}
	
	public String[] getTimes() {
			return times;
	}

}
