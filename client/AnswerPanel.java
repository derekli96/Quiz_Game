//答题界面

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.*;

public class AnswerPanel extends JPanel{
	public ChoicePanel choicepanel;
	public HeaderPanel headerpanel;
	public QuestionPanel questionPanel;
	public dataRecord data=new dataRecord();
	public halfListener hListener;
	
	public AnswerPanel(final Map questionInfo){
		setBorder(BorderFactory.createTitledBorder("Question"));
		setPreferredSize(new Dimension(300,200));
		setLayout(new BorderLayout(0,0));
		choicepanel=new ChoicePanel(questionInfo);
		headerpanel=new HeaderPanel(questionInfo);
		questionPanel=new QuestionPanel(questionInfo.get("Question").toString());
		add(headerpanel,BorderLayout.NORTH);
		add(questionPanel,BorderLayout.CENTER);
		add(choicepanel,BorderLayout.SOUTH);
		hListener=new halfListener(questionInfo.get("correctAnswer").toString());
		headerpanel.button1.addActionListener(hListener);
	}
	
    class halfListener implements ActionListener{
	   public String answer=new String();
	   public halfListener(String answer) {
              this.answer=answer;
		} 
	   
		public void actionPerformed(ActionEvent e) {
			data.useHalf=true;
			headerpanel.button1.setVisible(false);
			int c=(int)(answer.charAt(0));
			int uc1=c+1-c/68*4;
			int uc2=c+2-(c+1)/68*4;
			switch (uc1){
			case 65:choicepanel.options[0].setVisible(false);break;
			case 66:choicepanel.options[1].setVisible(false);break;
			case 67:choicepanel.options[2].setVisible(false);break;
			case 68:choicepanel.options[3].setVisible(false);break;
			default:;
			}
			switch(uc2){
			case 65:choicepanel.options[0].setVisible(false);break;
			case 66:choicepanel.options[1].setVisible(false);break;
			case 67:choicepanel.options[2].setVisible(false);break;
			case 68:choicepanel.options[3].setVisible(false);break;
			default:;
			
			}
		}
		
		public void update(String answer){
			this.answer=answer;
		}
   }

   
   public void update(String answer){
	   hListener.update(answer);
   }
}