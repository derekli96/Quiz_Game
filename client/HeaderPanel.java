//标题

import java.awt.*;
import javax.swing.*;
import java.util.Map;

public class HeaderPanel extends JPanel{
	public HalfButton button1;
	public HelpButton button2=new HelpButton();
	public targetScorePanel targetScore=new targetScorePanel("100");
	public ClockPanel clock=new ClockPanel();
	
	public HeaderPanel(Map questionInfo){
		button1=new HalfButton(questionInfo.get("correctAnswer").toString());
		setLayout(new GridLayout(1,4,0,0));
		add(targetScore);
		add(clock);
		add(button1);
		add(button2);
		
	}
}