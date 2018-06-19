import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MainFrame extends JFrame{
	public Map QuestionInfo;
	public InfoPanel f1;
	public ScorePanel f2;
	public AnswerPanel f3;
	public ConversationPanel f4;
	public Question questionSet;//问题集
	public int questionCount=0;//记录已回答问题数
	public int roundCount=1;//记录轮数，共进行两轮
	public int[] roundScore=new int[2];//记录每轮得分
	public ResultFrame resultframe;
	public NextRoundFrame nextRound=new NextRoundFrame();
	public int mode;//1:1v1人机   2:1v1双人   3:2v2
	public int higherScore;
	public int rivalScore;
	public boolean isWon=true;
	public dataRecord data =new dataRecord();
	public static UserInfo user;
	public Color xiangyabai=new Color(250,255,240);//设置颜色
	public Color qianlv=new Color(189,252,201);
	public Color qianhuang=new Color(189,252,201);
	public Color qianhong=new Color(255,192,203);
 	public DataOutputStream toServer;
 	public DataInputStream fromServer; //接收对手的最终成绩
 	public String realTimeScore=new String();
 	public boolean result=false;
 	public int teamRound1=0,teamRound2;
 	public PictureFrame pic;
 	public WaitingFrame waiting=new WaitingFrame();//等待游戏结束界面
 	public boolean ifJudge=true;
	
 	//日期转字符串
    public  String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	} 
    
    //防止强行关闭窗口导致游戏中断
    protected void processWindowEvent(WindowEvent e) {  
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    	JOptionPane.showMessageDialog(null,"实验进行中，\n请勿退出程序！","错误",JOptionPane.ERROR_MESSAGE);
	    	return;
	    }//阻止默认动作，阻止窗口关闭 
	    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
	} 
    
	public MainFrame(dataRecord data, UserInfo user, int mode, Question questionSet) throws NotSerializableException{
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.mode=mode;
		this.data=data;
		this.user=user;
		this.questionSet=questionSet;
		try {
			fromServer = new DataInputStream(user.socket.getInputStream()); //接收服务器传来的数据
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.questionTime[(roundCount-1)*12+questionCount]=new Timestamp(System.currentTimeMillis()); 
		setSize(800,800);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
        roundScore[0]=0;
        roundScore[1]=0;
		
		setTitle("Main frame");
		setLayout(new BorderLayout(5,5));
		QuestionInfo=questionSet.getNewSet();
		f1=new InfoPanel(mode,user); //信息提示栏
		f2=new ScorePanel(); //得分栏
		f3=new AnswerPanel(QuestionInfo); //答题区域
		f4=new ConversationPanel(user,data,mode,QuestionInfo); //聊天区域
		pic=new PictureFrame(QuestionInfo);
		add(f1,BorderLayout.WEST);
		add(f2,BorderLayout.EAST);
		add(f3,BorderLayout.SOUTH);
		add(f4,BorderLayout.CENTER);
		f3.choicepanel.options[0].addActionListener(new optionListener("A"));
		f3.choicepanel.options[1].addActionListener(new optionListener("B"));
		f3.choicepanel.options[2].addActionListener(new optionListener("C"));
		f3.choicepanel.options[3].addActionListener(new optionListener("D"));
		f3.headerpanel.clock.timer.addActionListener(new clockListener());
		
		try 
   		{    
   			toServer = new DataOutputStream(user.socket.getOutputStream());   //向服务器传递数据
   		} 
   		catch (IOException e) 
   		{
   			JOptionPane.showMessageDialog(null, "系统异常","错误",JOptionPane.OK_CANCEL_OPTION);
   		}
    	
	}

    //再来一局，该部分迁移至GameModule，此部分代码无效
	 class nextRoundListener implements ActionListener{
		public boolean listenerType;
		public nextRoundListener(boolean nextType){
			this.listenerType=nextType;	
		}
		public void actionPerformed(ActionEvent e) {
			if(listenerType==true){
				data.playNext=true;
				resultframe.setVisible(false);
				roundCount=1;
 				questionCount=0;
 				QuestionInfo=questionSet.getNewSet();		
 				f3.choicepanel.options[0].update(QuestionInfo.get("options[0]").toString());
 				f3.choicepanel.options[1].update(QuestionInfo.get("options[1]").toString());
 				f3.choicepanel.options[2].update(QuestionInfo.get("options[2]").toString());
 				f3.choicepanel.options[3].update(QuestionInfo.get("options[3]").toString());
 				f3.questionPanel.update(QuestionInfo.get("Question").toString());
 				f3.headerpanel.clock.reset();
 				f3.headerpanel.targetScore.update(f2.score[0].getMessage());
 				f3.headerpanel.button1.setVisible(true);
 				f2.reset();
 				setVisible(true);
 				nextRound.setVisible(false);
			}
			else{
				resultframe.setVisible(false);
				nextRound.setVisible(false);
			}
			
		}
	}
	
	 //计时，每道题1分钟
	 class clockListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(f3.headerpanel.clock.clockTime>60){
					f3.headerpanel.clock.timer.stop();
					setVisible(false);
					pic.setVisible(false);
	    				JOptionPane.showMessageDialog(null,"时间到！","提示",JOptionPane.WARNING_MESSAGE);
	    			final Timer delayTimer=new Timer();
	    			TimerTask timerTask=new TimerTask()
	    			{
	    				public void run()
	    				{	  
	    					questionCount=11;
			    			updateAll();
	    					delayTimer.cancel();
	    				}
	    			};
	    			delayTimer.schedule(timerTask, 1000);
				}
			}
		}
	 
	//选项
	class optionListener implements ActionListener{
		public String optionType;
		
		public optionListener(String optionType){
			this.optionType=optionType;
		}
		
		public void actionPerformed(ActionEvent e){
			if (ifJudge==true){
			switch (optionType){
			    case "A":{
			    	if(f3.choicepanel.options[0].times==0){//第一次选择A，预选
			    		data.clickA[(roundCount-1)*12+questionCount]++;//记录选项A被点击次数
			    		f3.choicepanel.options[0].times++;
			    		f3.choicepanel.options[1].times=0;//将其他选项设定为未选状态
			    		f3.choicepanel.options[2].times=0;
			    		f3.choicepanel.options[3].times=0;
			    		f3.choicepanel.options[0].setBackground(xiangyabai);
			    		f3.choicepanel.options[1].setBackground(Color.WHITE);
			    		f3.choicepanel.options[2].setBackground(Color.WHITE);
			    		f3.choicepanel.options[3].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[0].times==1){//第二次选择A，最终确认
			    		ifJudge=false;//防止用户在显示结果的过程中，重复点击正确选项导致分数激增
			    		data.clickA[(roundCount-1)*12+questionCount]++;
			    		if(isCorrect("A")){//结果正确
			    			f3.choicepanel.options[0].setBackground(qianlv);//正确答案绿色标注
			    			roundScore[roundCount-1]=Integer.parseInt(f2.score[questionCount].getMessage()); //更新该轮得分
			    			try {
		    					if(mode==1) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#01#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    					}
		    					else if (mode==2) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#02#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    					}
		    					else if(mode==3) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#03#"+user.username+"#"+total+"#"); //向服务器传递当前总成绩
		    					}
		    					toServer.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
			    			if(roundCount==1){//记录当前得分
			    				data.round1Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			else{
			    				data.round2Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[0].setBackground(Color.WHITE);
					    			updateAll();//更新题目
			    					delayTimer.cancel();
			    					ifJudge=true;//重新允许点击
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);//选择结果持续显示1秒，之后跳入下一题（选择正确）或进入选择错误界面
			    		
			    		}
			    		else{//选择错误
			    			
			    			f3.choicepanel.options[0].setBackground(qianhong);//错误答案浅红色标注
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);//正确答案浅黄色标注
			    			
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    		    setVisible(false);
			    		    pic.setVisible(false);
			    			JOptionPane.showMessageDialog(null,"选择错误！","错误",JOptionPane.ERROR_MESSAGE);//弹出提示选择错误
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[0].setBackground(Color.white);
			    					f3.choicepanel.options[1].setBackground(Color.white);
			    					f3.choicepanel.options[2].setBackground(Color.white);
			    					f3.choicepanel.options[3].setBackground(Color.white);
			    					questionCount=11;//直接更新已回答题目数量为11，updateALL函数接受后可直接进入下一轮或结束本局
					    			updateAll();//更新题目（进入下一轮或者结束本局）
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
			    	if(f3.choicepanel.options[1].times==0){//第一次选择B，预选
			    		data.clickB[(roundCount-1)*12+questionCount]++;//选择B的次数增加
			    		f3.choicepanel.options[1].times++;
			    		f3.choicepanel.options[0].times=0;//其他选项设置为未选
			    		f3.choicepanel.options[2].times=0;
			    		f3.choicepanel.options[3].times=0;
			    		f3.choicepanel.options[1].setBackground(xiangyabai);
			    		f3.choicepanel.options[0].setBackground(Color.WHITE);
			    		f3.choicepanel.options[2].setBackground(Color.WHITE);
			    		f3.choicepanel.options[3].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[1].times==1){//第二次选择B，最终确认
			    		ifJudge=false;
			    		data.clickB[(roundCount-1)*12+questionCount]++;//选择B的次数增加
			    		if(isCorrect("B")){//结果正确
			    			f3.choicepanel.options[1].setBackground(qianlv);//正确答案绿色标注
			    			roundScore[roundCount-1]=Integer.parseInt(f2.score[questionCount].getMessage());//更新该轮得分
			    			try {
		    					if(mode==1) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#01#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    						}
		    					else if (mode==2) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#02#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    						}
		    					else if(mode==3) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#03#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    					}
		    					toServer.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
			    			if(roundCount==1){//记录当前得分
			    				data.round1Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			else{
			    				data.round2Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[1].setBackground(Color.WHITE);
					    			updateAll();//更新题目
			    					delayTimer.cancel();
			    					ifJudge=true;//重新允许点击
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			
			    		}
			    		else{//结果错误
			    			f3.choicepanel.options[1].setBackground(qianhong);//错误答案浅红色标注
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);//正确答案浅黄色标注
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    				setVisible(false);
			    				pic.setVisible(false);
				    		JOptionPane.showMessageDialog(null,"选择错误！","错误",JOptionPane.ERROR_MESSAGE);//弹出提示选择错误
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
					    			updateAll();//更新题目（进入下一轮或者结束本局）
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
			    	if(f3.choicepanel.options[2].times==0){//第一次选择C，预选
			    		data.clickC[(roundCount-1)*12+questionCount]++;//选择C的次数增加
			    		f3.choicepanel.options[2].times++;
			    		f3.choicepanel.options[1].times=0;//其他选项设置为未选
			    		f3.choicepanel.options[0].times=0;
			    		f3.choicepanel.options[3].times=0;
			    		f3.choicepanel.options[2].setBackground(xiangyabai);
			    		f3.choicepanel.options[1].setBackground(Color.WHITE);
			    		f3.choicepanel.options[0].setBackground(Color.WHITE);
			    		f3.choicepanel.options[3].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[2].times==1){//第二次选择C，最终确认
			    		ifJudge=false;
			    		data.clickC[(roundCount-1)*12+questionCount]++;//选择C的次数增加
			    		if(isCorrect("C")){
			    			f3.choicepanel.options[2].setBackground(qianlv);
			    			roundScore[roundCount-1]=Integer.parseInt(f2.score[questionCount].getMessage());
			    			try {
			    				//打上String标签和用户名标签（方便判断是本机还是对手）
		    					if(mode==1) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#01#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    						}
		    					else if (mode==2) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#02#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    						}
		    					else if(mode==3) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#03#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    					}
		    					toServer.flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			    			if(roundCount==1){//记录每轮得分
			    				data.round1Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			else{
			    				data.round2Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[2].setBackground(Color.WHITE);
					    			updateAll();//更新题目
			    					delayTimer.cancel();
			    					ifJudge=true;
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			
			    		}
			    		else{//结果错误
			    			f3.choicepanel.options[2].setBackground(qianhong);//错误答案浅红色标注
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);//正确答案浅黄色标注
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    				setVisible(false);
			    				pic.setVisible(false);
				    			JOptionPane.showMessageDialog(null,"选择错误！","错误",JOptionPane.ERROR_MESSAGE);//弹出提示选择错误
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
					    			updateAll();//更新题目（进入下一轮或者结束本局）
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
			    	if(f3.choicepanel.options[3].times==0){//第一次选择D，预选
			    		data.clickD[(roundCount-1)*12+questionCount]++;//选择D的次数增加
			    		f3.choicepanel.options[3].times++;
			    		f3.choicepanel.options[1].times=0;//其他选项设置为未选
			    		f3.choicepanel.options[2].times=0;
			    		f3.choicepanel.options[0].times=0;
			    		f3.choicepanel.options[3].setBackground(xiangyabai);
			    		f3.choicepanel.options[1].setBackground(Color.WHITE);
			    		f3.choicepanel.options[2].setBackground(Color.WHITE);
			    		f3.choicepanel.options[0].setBackground(Color.WHITE);
			    	}
			    	else if(f3.choicepanel.options[3].times==1){//第二次选择D，最终确认
			    		ifJudge=false;
			    		data.clickD[(roundCount-1)*12+questionCount]++;//选择D的次数增加
			    		if(isCorrect("D")){//结果正确
			    			f3.choicepanel.options[3].setBackground(qianlv);//正确答案绿色标注
			    			roundScore[roundCount-1]=Integer.parseInt(f2.score[questionCount].getMessage());//更新该轮得分
			    			try {
		    					if(mode==1) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#01#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    						}
		    					else if (mode==2) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#02#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    						}
		    					else if(mode==3) {
		    						int total=roundScore[0]+roundScore[1];
		    						toServer.writeUTF("05#03#"+user.username+"#"+total+"#");//向服务器传递当前总成绩
		    					}
		    					toServer.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
			    			if(roundCount==1){//更新得分
			    				data.round1Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			else{
			    				data.round2Score=roundScore[roundCount-1];
			    				data.totalScore=data.round1Score+data.round2Score;
			    			}
			    			f3.headerpanel.clock.timer.stop();
			    			final Timer delayTimer=new Timer();
			    			TimerTask timerTask=new TimerTask()
			    			{
			    				public void run()
			    				{
			    					f3.choicepanel.options[3].setBackground(Color.WHITE);
					    			updateAll();//更新题目
			    					delayTimer.cancel();
			    					ifJudge=true;//重新允许点击
			    				}
			    			};
			    			delayTimer.schedule(timerTask, 1000);
			    			
			    		}
			    		else{//结果错误
			    			f3.choicepanel.options[3].setBackground(qianhong);//错误答案浅红色标注
			    			f3.headerpanel.clock.timer.stop();			    			
			    			f3.choicepanel.options[(int)(QuestionInfo.get("correctAnswer").toString().charAt(0))-65].setBackground(qianhuang);//正确答案浅黄色标注
			    			
			    			final Timer delayT=new Timer();
			    			TimerTask timerTask1=new TimerTask(){
			    			public void run(){
			    				setVisible(false);
			    				pic.setVisible(false);
				    		JOptionPane.showMessageDialog(null,"选择错误！","错误",JOptionPane.ERROR_MESSAGE);//弹出提示选择错误
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
					    			updateAll();//更新题目（进入下一轮或者结束本局）
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
			data.useHalf=f3.data.useHalf;
			data.questionTime[(roundCount-1)*12+questionCount-1]=new Timestamp(System.currentTimeMillis()); ;
			if (questionCount==12&&roundCount==2) {//两轮结束，询问是否进入下一局
				pic.setVisible(false);
				data.totalScore=data.round1Score+data.round2Score;
				setVisible(false);
				if(mode==1) { // 判断输赢
						if((data.round1Score+data.round2Score)>=(f1.systemScore[0]+f1.systemScore[1])){
							isWon=true;
						}
						else{
							isWon=false;
						}
					resultframe=new ResultFrame(isWon,user,data,f1.systemScore[0]+f1.systemScore[1]);
				}
				else if(mode==2) {//传送本局游戏结束信息				
					try {
						toServer.writeUTF("08#"+data.totalScore+"#");
						toServer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					while(result==false) {//显示等待其他玩家结束游戏
						waiting.setVisible(true);
					}
					waiting.setVisible(false);
					result=false;
					if(data.totalScore>=higherScore) {//判断输赢
						isWon=true;
					}
					else{
						isWon=false;
					}
					resultframe=new ResultFrame(isWon,user,data,rivalScore);
				}
				else if(mode==3) {//传送本局游戏结束信息				
					try {
						toServer.writeUTF("0F#"+user.username+"#"+roundScore[0]+"#"+roundScore[1]+"#"+data.totalScore+"#");
						toServer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					while(result==false) {//显示等待其他玩家结束游戏
						waiting.setVisible(true);
					}
					result=false;
					waiting.setVisible(false);
					resultframe=new ResultFrame(isWon,user,data,teamRound1,teamRound2,rivalScore);
				}
			
				resultframe.setVisible(true);//显示结果
				final Timer delayTimer=new Timer();
    			TimerTask timerTask=new TimerTask()
    			{
    				public void run()
    				{
    					nextRound.setVisible(true);//询问是否进入下一局
    					resultframe.setVisible(false);
    				}
    			};
    			delayTimer.schedule(timerTask, 5000);
		
			}
			else if(questionCount==12&&roundCount==1){//一轮结束，进入下一轮
				pic.setVisible(false);
				JOptionPane.showMessageDialog(null,"进入下一轮游戏","提示",JOptionPane.WARNING_MESSAGE);//弹出提示，进入第二轮
				setVisible(false);

    			final Timer delayTimer=new Timer();
    			TimerTask timerTask=new TimerTask()
    			{
    				public void run()
    				{//将各面板恢复到第一题的状态，并更新问题集
                        setVisible(true);
                        roundCount++;
         				questionCount=0;
         				QuestionInfo=questionSet.getNewSet();		
         				f3.choicepanel.options[0].update(QuestionInfo.get("options[0]").toString());
         				f3.choicepanel.options[1].update(QuestionInfo.get("options[1]").toString());
         				f3.choicepanel.options[2].update(QuestionInfo.get("options[2]").toString());
         				f3.choicepanel.options[3].update(QuestionInfo.get("options[3]").toString());
         				f3.questionPanel.update(QuestionInfo.get("Question").toString());
         				f3.headerpanel.clock.reset();
         				f3.headerpanel.targetScore.update(f2.score[0].getMessage());
         				f3.headerpanel.button1.setVisible(true);
         				pic.update(QuestionInfo);
         				if(mode==3){
         					f3.headerpanel.button2.setVisible(true);
         				}
         				f2.reset();
    				}
    			};
    			delayTimer.schedule(timerTask, 5000);
			}
			else{//回答正确后，将各面板更新信息，显示下一题题干和选项，时钟恢复到60s状态
 				QuestionInfo=questionSet.getNewQuestion();		
				f3.choicepanel.options[0].update(QuestionInfo.get("options[0]").toString());
				f3.choicepanel.options[1].update(QuestionInfo.get("options[1]").toString());
				f3.choicepanel.options[2].update(QuestionInfo.get("options[2]").toString());
				f3.choicepanel.options[3].update(QuestionInfo.get("options[3]").toString());
				f3.questionPanel.update(QuestionInfo.get("Question").toString());
				f3.headerpanel.clock.reset();
				f3.headerpanel.button1.update(QuestionInfo.get("correctAnswer").toString());
				f3.headerpanel.targetScore.update(f2.score[questionCount].getMessage());
				f2.update();
				f1.update(questionCount,data.totalScore,roundCount);
				f3.update(QuestionInfo.get("correctAnswer").toString());
				f4.update(QuestionInfo);
				pic.update(QuestionInfo);
				
			}
			
	}
	
	
}