//选项按钮

import java.awt.*;
import javax.swing.*;
import java.util.Map;

public class ChoicePanel extends JPanel{
	public OptionButton[] options;


	public ChoicePanel(Map questionMap){
		setLayout(new GridLayout(2,2,10,10));
		options=new OptionButton[4];
		options[0]=new OptionButton("A",questionMap.get("options[0]").toString());//选项A按钮
		options[1]=new OptionButton("B",questionMap.get("options[1]").toString());//选项B按钮
		options[2]=new OptionButton("C",questionMap.get("options[2]").toString());//选项C按钮
		options[3]=new OptionButton("D",questionMap.get("options[3]").toString());//选项D按钮
		
		
		add(options[0]);
		add(options[1]);
		add(options[2]);
		add(options[3]);
	}
}
