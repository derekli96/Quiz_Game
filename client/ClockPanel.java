//倒计时

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClockPanel extends JPanel
{
	int clockTime=0;
	Timer timer=new Timer(1000,new clockListener());
	
	public ClockPanel(){
		timer.start();
	}
	
	public void paintComponent(Graphics g)//画出倒计时UI
	{
		super.paintComponent(g);
		g.drawArc(5,5,23,23,90+clockTime*6,360-clockTime*6);
		String s=""+(60-clockTime);
		g.drawString(s, 9, 22);
		clockTime++;
	}
    
	public void reset(){
		timer.stop();
		clockTime=0;
		repaint();
		timer.start();
	}
	
	class clockListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(clockTime<=60){
				repaint();
			}
			else {
				timer.stop(); //时间到
			}
			
		}
	}

}
