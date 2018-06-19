import javax.swing.*;
//去掉两个错误选项，每轮只能用一次
public class HalfButton extends JButton{
	public char correctAnswer;
	public String Answer;
	
	public HalfButton(String Answer){
		this.Answer=Answer;
		if(Answer.equals("A")) correctAnswer='A';
		else if(Answer.equals("B")) correctAnswer='B';
		else if(Answer.equals("C")) correctAnswer='C';
		else if(Answer.equals("D")) correctAnswer='D';
		this.setText("50/50");
	}	
	
	public void update(String Answer){
		this.Answer=Answer;
		if(Answer.equals("A")) correctAnswer='A';
		else if(Answer.equals("B")) correctAnswer='B';
		else if(Answer.equals("C")) correctAnswer='C';
		else if(Answer.equals("D")) correctAnswer='D';
	}
	
}