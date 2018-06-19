import java.awt.*;
import javax.swing.*;

//显示题干
public class QuestionPanel extends JPanel{
	public String text;
	JTextArea qarea;
	Color bj=new Color(238,238,238);
	public QuestionPanel(){
	}
	
	public QuestionPanel(String newText){
		this.text=newText;
		qarea=new JTextArea(text,3,50);
		qarea.setEditable(false);
		qarea.setMargin(new Insets(20,10,10,10));
		qarea.setLineWrap(true);
		qarea.setOpaque(true);
		qarea.setBackground(bj);
		add(qarea);
		
	}
	
	public void update(String newText){
		this.text=newText;
		qarea.setText(text);
	}
	
}