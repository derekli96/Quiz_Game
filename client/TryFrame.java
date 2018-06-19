import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.NotSerializableException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

//试玩阶段

public class TryFrame extends JFrame{
	public Map QuestionInfo;
	public InfoPanel f1;
	public ScorePanel f2;
	public AnswerPanel f3;
	public ConversationPanel f4;
	public Question questionSet;
	public int questionCount=0;
	public int roundCount=1;
	public int[] roundScore=new int[2];
	public ResultFrame resultframe;
	public NextRoundFrame nextRound;
	public dataRecord data= new dataRecord();
	public UserInfo user;
	public PictureFrame pic;
	public Color xiangyabai=new Color(250,255,240);
	public Color qianlv=new Color(189,252,201);
	public Color qianhuang=new Color(189,252,201);
	public Color qianhong=new Color(255,192,203);
	public boolean ifJudge=true;
	public String ip=new String();
	
	protected void processWindowEvent(WindowEvent e) {  
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    	JOptionPane.showMessageDialog(null,"实验进行中，\n请勿退出程序！","错误",JOptionPane.ERROR_MESSAGE);
	    }//阻止默认动作，阻止窗口关闭 
	    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
	} 
	
	public TryFrame(UserInfo user,dataRecord data,Question questionSet,String ip) throws NotSerializableException{
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.data=data;
		this.user=user;
		this.ip=ip;
		this.questionSet=questionSet;
		data.TryTime=new Timestamp(System.currentTimeMillis()); 
		setSize(800,800);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		setTitle("Try frame");
		setLayout(new BorderLayout(0,0));
		QuestionInfo=questionSet.getTryQuestion();
		f1=new InfoPanel(0,user); //信息提示栏
		f2=new ScorePanel(); //得分栏
		f3=new AnswerPanel(QuestionInfo); //答题区域
		f4=new ConversationPanel(user,data,0,QuestionInfo); //聊天区域
		pic=new PictureFrame(QuestionInfo);
		add(f1,BorderLayout.WEST);
		add(f2,BorderLayout.EAST);
		add(f3,BorderLayout.SOUTH);
		add(f4,BorderLayout.CENTER);
		f4.setVisible(false);
		f3.choicepanel.options[0].addActionListener(new optionListener("A"));
		f3.choicepanel.options[1].addActionListener(new optionListener("B"));
		f3.choicepanel.options[2].addActionListener(new optionListener("C"));
		f3.choicepanel.options[3].addActionListener(new optionListener("D"));
		f3.headerpanel.clock.timer.addActionListener(new clockListener());
		f3.headerpanel.button2.setVisible(false);
	}

    
	 //计时
	 class clockListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(f3.headerpanel.clock.clockTime>60){
					f3.headerpanel.clock.timer.stop();
					setVisible(false);
					pic.setVisible(false);
	    				JOptionPane.showMessageDialog(null,"时间到！试玩结束！","提示",JOptionPane.ERROR_MESSAGE);
	    			final Timer delayTimer=new Timer();
	    			TimerTask timerTask=new TimerTask()
	    			{
	    				public void run()
	    				{
	  
	    					//error.setVisible(false);
	    					questionCount=11;
			    			updateAll();
	    					delayTimer.cancel();
	    				}
	    			};
	    			delayTimer.schedule(timerTask, 1000);
				}
			}
		}
	 
	//option
	class optionListener implements ActionListener{
		public String optionType;
		
		public optionListener(String optionType){
			this.optionType=optionType;
		}
		
		public void actionPerformed(ActionEvent e){
			if(ifJudge=true){
			switch (optionType){
			    case "A":{
			    	if(f3.choicepanel.options[0].times==0){
			    		f3.choicepanel.options[0].times++;
			    		f3.choicepanel.options[1].times=0;
			    		f3.choicepanel.options[2].times=0;
			    		f3.choicepanel.options[3].times=0;
			    		f3.choicepanel.options[0].setBackground(xiangyabai);
			    		f3.choicepanel.options[1].setBackground(Color.WHITE);
			    		f3.choicepanel.options[2].setBackground(Color.WHITE);
			    		f3.choicepanel.options[3].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[0].times==1){
			    		ifJudge=false;
			    		if(isCorrect("A")){
			    			f3.choicepanel.options[0].setBackground(qianlv);
			    			roundScore[roundCount-1]+=Integer.parseInt(f2.score[questionCount].getMessage());
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[0].setBackground(Color.WHITE);
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    		
			    		}
			    		else{
			    			f3.choicepanel.options[0].setBackground(qianhong);
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);
			    			
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    		    setVisible(false);
			    		    pic.setVisible(false);
			    			JOptionPane.showMessageDialog(null,"选择错误！试玩结束！","提示",JOptionPane.ERROR_MESSAGE);
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[0].setBackground(Color.white);
			    					f3.choicepanel.options[1].setBackground(Color.white);
			    					f3.choicepanel.options[2].setBackground(Color.white);
			    					f3.choicepanel.options[3].setBackground(Color.white);
			    					questionCount=11;
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			    }
			    			};
			    			delayT.schedule(timerTask1,1000);
			    		}
			    		
			    	}
			    	
			    	break;
			    }
			    case "B":{
			    	if(f3.choicepanel.options[1].times==0){
			    		f3.choicepanel.options[1].times++;
			    		f3.choicepanel.options[0].times=0;
			    		f3.choicepanel.options[2].times=0;
			    		f3.choicepanel.options[3].times=0;
			    		f3.choicepanel.options[1].setBackground(xiangyabai);
			    		f3.choicepanel.options[0].setBackground(Color.WHITE);
			    		f3.choicepanel.options[2].setBackground(Color.WHITE);
			    		f3.choicepanel.options[3].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[1].times==1){
			    		ifJudge=false;
			    		if(isCorrect("B")){
			    			f3.choicepanel.options[1].setBackground(qianlv);
			    			roundScore[roundCount-1]+=Integer.parseInt(f2.score[questionCount].getMessage());
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[1].setBackground(Color.WHITE);
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			
			    		}
			    		else{
			    			f3.choicepanel.options[0].setBackground(qianhong);
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    				setVisible(false);
			    				pic.setVisible(false);
				    			JOptionPane.showMessageDialog(null,"选择错误！试玩结束！","提示",JOptionPane.ERROR_MESSAGE);
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[0].setBackground(Color.white);
			    					f3.choicepanel.options[1].setBackground(Color.white);
			    					f3.choicepanel.options[2].setBackground(Color.white);
			    					f3.choicepanel.options[3].setBackground(Color.white);
			    					questionCount=11;
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			    }
			    			};
			    			delayT.schedule(timerTask1,1000);
			    		}
			    	}
			    	
			    	break;
			    }
			    case "C":{
			    	if(f3.choicepanel.options[2].times==0){
			    		f3.choicepanel.options[2].times++;
			    		f3.choicepanel.options[1].times=0;
			    		f3.choicepanel.options[0].times=0;
			    		f3.choicepanel.options[3].times=0;
			    		f3.choicepanel.options[2].setBackground(xiangyabai);
			    		f3.choicepanel.options[1].setBackground(Color.WHITE);
			    		f3.choicepanel.options[0].setBackground(Color.WHITE);
			    		f3.choicepanel.options[3].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[2].times==1){
			    		ifJudge=false;
			    		if(isCorrect("C")){
			    			f3.choicepanel.options[2].setBackground(qianlv);
			    			roundScore[roundCount-1]+=Integer.parseInt(f2.score[questionCount].getMessage());
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[2].setBackground(Color.WHITE);
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			
			    		}
			    		else{
			    			f3.choicepanel.options[0].setBackground(qianhong);
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);
			    			
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    				setVisible(false);
			    				pic.setVisible(false);
				    			JOptionPane.showMessageDialog(null,"选择错误！试玩结束！","提示",JOptionPane.ERROR_MESSAGE);
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[0].setBackground(Color.white);
			    					f3.choicepanel.options[1].setBackground(Color.white);
			    					f3.choicepanel.options[2].setBackground(Color.white);
			    					f3.choicepanel.options[3].setBackground(Color.white);
			    					questionCount=11;
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			    }
			    			};
			    			delayT.schedule(timerTask1,1000);
			    		}
			    		
			    	}
			    	
			    	break;
			    }
			    case "D":{
			    	if(f3.choicepanel.options[3].times==0){
			    		f3.choicepanel.options[3].times++;
			    		f3.choicepanel.options[1].times=0;
			    		f3.choicepanel.options[2].times=0;
			    		f3.choicepanel.options[0].times=0;
			    		f3.choicepanel.options[3].setBackground(xiangyabai);
			    		f3.choicepanel.options[1].setBackground(Color.WHITE);
			    		f3.choicepanel.options[2].setBackground(Color.WHITE);
			    		f3.choicepanel.options[0].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[3].times==1){
			    		ifJudge=false;
			    		if(isCorrect("D")){
			    			f3.choicepanel.options[3].setBackground(qianlv);
			    			roundScore[roundCount-1]+=Integer.parseInt(f2.score[questionCount].getMessage());
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[3].setBackground(Color.WHITE);
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			
			    		}
			    		else{
			    			f3.choicepanel.options[3].setBackground(qianhong);
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);
			    			
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    				setVisible(false);
			    				pic.setVisible(false);
			    			JOptionPane.showMessageDialog(null,"选择错误！试玩结束！","错误",JOptionPane.ERROR_MESSAGE);
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[0].setBackground(Color.white);
			    					f3.choicepanel.options[1].setBackground(Color.white);
			    					f3.choicepanel.options[2].setBackground(Color.white);
			    					f3.choicepanel.options[3].setBackground(Color.white);
			    					questionCount=11;
					    			updateAll();
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			    }
			    			};
			    			delayT.schedule(timerTask1,1000);
			    		}
			    		
			    	}
			    	
			    	break;
			    }
			}
		}
		}
	}
	
	//判断答案是否正确
	public boolean isCorrect(String userAnswer){
		return userAnswer.equals(QuestionInfo.get("correctAnswer").toString());
	}
	
	//更新得分
	public void updateAll(){
			questionCount++;
			if (questionCount==12) {//试玩结束
				setVisible(false);
				pic.setVisible(false);
				JOptionPane.showMessageDialog(null, "下面将进行正式游戏！"); //弹出进入正式游戏提示框
				final Timer delayTimer=new Timer();
    			TimerTask timerTask=new TimerTask()
    			{
    				public void run()
    				{
    					GameModule game=new GameModule(data,user,questionSet,ip);//进入正式游戏模式，选择游戏模式并进入对应模块
    					delayTimer.cancel();
    				}
    			};
    			delayTimer.schedule(timerTask, 5000);

			}
			else{
				QuestionInfo=questionSet.getNewQuestion();
				f3.choicepanel.options[0].update(QuestionInfo.get("options[0]").toString());
				f3.choicepanel.options[1].update(QuestionInfo.get("options[1]").toString());
				f3.choicepanel.options[2].update(QuestionInfo.get("options[2]").toString());
				f3.choicepanel.options[3].update(QuestionInfo.get("options[3]").toString());
				f3.questionPanel.update(QuestionInfo.get("Question").toString());
				f3.headerpanel.button1.update(QuestionInfo.get("correctAnswer").toString());
				System.out.println(QuestionInfo.get("correctAnswer").toString());
				f3.headerpanel.clock.reset();
				f3.headerpanel.targetScore.update(f2.score[questionCount].getMessage());
				f3.update(QuestionInfo.get("correctAnswer").toString());
				f2.update();
				pic.update(QuestionInfo);
			}
			
	}
	
}