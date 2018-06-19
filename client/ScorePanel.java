import java.awt.*;
import javax.swing.*;

//显示不同分数等级，从第一题到12题依次递增
public class ScorePanel extends JPanel{
	public MessagePanel[] level;
	public MessagePanel[] score;
	public String[] lsRelation;
	public int status;
	public Color zhengchong=new Color(255,227,132);
	public Color yichong=new Color(248,248,255);
	public Color weichong=new Color(240,240,255);
	
	public ScorePanel(){
		setPreferredSize(new Dimension(200,200));
		setBorder(BorderFactory.createTitledBorder("Score"));
		setLayout(new GridLayout(12,2));	
		
		lsRelation=new String[12];
		level=new MessagePanel[12];
		score=new MessagePanel[12];
		
		lsRelation[0]="100";
		lsRelation[1]="200";
		lsRelation[2]="500";
		lsRelation[3]="1000";
		lsRelation[4]="2000";
		lsRelation[5]="4000";
		lsRelation[6]="16000";
		lsRelation[7]="64000";
		lsRelation[8]="125000";
		lsRelation[9]="250000";
		lsRelation[10]="500000";
		lsRelation[11]="1000000";
		
		for(int i=0;i<12;i++){
			String s = String.valueOf(i+1);
			level[i]=new MessagePanel(s);
			score[i]=new MessagePanel(lsRelation[i]);
		}
		
		for(int j=11;j>-1;j--){
			add(level[j]);
			add(score[j]);
			level[j].setBackground(weichong);
			score[j].setBackground(weichong);
		}
		
		status=0;
		level[0].setBackground(zhengchong);
		score[0].setBackground(zhengchong);
	}
	
	//更新到下个分数段
	public void update(){
		level[status].setBackground(yichong);
		score[status].setBackground(yichong);
		status++;
		level[status].setBackground(zhengchong);
		score[status].setBackground(zhengchong);
	}
	
	//进行到下一轮，回到第一个分数段
	public void reset(){
		status=0;
		for(int i=0;i<12;i++){
			level[i].setBackground(weichong);
			score[i].setBackground(weichong);
		}
		level[status].setBackground(zhengchong);
		score[status].setBackground(zhengchong);
	}
	
	
}