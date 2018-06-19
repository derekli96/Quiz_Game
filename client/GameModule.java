import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;


public class GameModule{
    String ip = "127.0.0.1";//服务器IP地址
	public ModeSelectFrame msf;//模式选择界面
	public dataRecord data;//游戏数据记录
	public MainFrame mf;//游戏主界面
	public UserInfo user;//用户数据
	public User u;
	public Question questionSet;//问题
	public int delayCount=0;
	public boolean isMatch;//是否匹配
	public MatchFrame match;//等待匹配窗口
	public boolean waiting=true;
	public boolean cancel=false;
	public DataInputStream fromServer;
	public String getResult=new String();
	public String msg=new String();
	public dataReceive dReceive;
	public String teammate=new String();
	public String rival1=new String();
	public String rival2=new String();
	public int Cancelmode=0;
	
	public GameModule(dataRecord data,UserInfo user,Question questionSet,String ip){
		this.data=data;
		this.user=user;
		this.questionSet=questionSet;
		this.ip=ip;
		try {
			fromServer = new DataInputStream(user.socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		msf=new ModeSelectFrame(user.indicator);//打开游戏模式选择界面
		msf.b1.addActionListener(new modeListener(1));
		msf.b2.addActionListener(new modeListener(2));
		msf.b3.addActionListener(new modeListener(3));
		msf.setVisible(true);
		
	}
	
	class modeListener implements ActionListener{//模式接收
		public int mode=0;
		
		public modeListener(int mode){
			this.mode=mode;			
			
		}
		
		public void actionPerformed(ActionEvent e) {
			data.mode=mode;
			msf.setVisible(false);
			if(mode==1){
				try {
					mf=new MainFrame(data,user,1,questionSet); //人机对战
					mf.f4.setVisible(false); //关掉聊天窗口
					mf.f3.headerpanel.button2.setVisible(false);//关掉ask for help
					mf.nextRound.buttons.Yes.addActionListener(new nextRoundListener(true,mode));
					mf.nextRound.buttons.No.addActionListener(new nextRoundListener(false,mode));
				    mf.setVisible(true);
				} catch (NotSerializableException e1) {
					e1.printStackTrace();
				}
			
			}
			else if(mode==2){//1V1人人对战
				Cancelmode=2;
				try {
	        			DataOutputStream toServer1 = new DataOutputStream(user.socket.getOutputStream());
	        			toServer1.writeUTF("07#"+user.username);//发送对战请求
	        			toServer1.flush();
	        			waiting=true;
	        			match=new MatchFrame();
	        			match.b1.addActionListener(new cancelMatchListener());
	        			match.setVisible(true);
	        			final Timer delayTimer=new Timer();//设置等待时机
	        			TimerTask timerTask=new TimerTask()
	        			{
	        				public void run()
	        				{
	        					while(waiting==true) {
	        					
	    	        				boolean canWrite=true;
	    	        				try {
	    	        					getResult=fromServer.readUTF();
	    	        				}
	    	        				catch(EOFException e1) {}
	    	        				catch(NullPointerException e2) {
	    	        					canWrite=false;
	    	        				} catch (IOException e) {
									e.printStackTrace();
							}	    	        				
	    	        				if(canWrite) {
	    	        					System.out.println(getResult);
	    		        				if(getResult.charAt(1)=='9') {//匹配成功
	    		        					isMatch=true;
	    		        					String[] array=getResult.split("#");//分拆服务器传来的字符串
	    		        					if(user.username.equals(array[1])){//获取对手用户名
	    		        					   rival1=array[2];
	    		        					   }
	    		        					else if(user.username.equals(array[2])){
		    		        					   rival1=array[1];
		    		        				}
	    		        					break;
	    		        				}
	    		        				if(getResult.equals("10")) {//匹配失败
	    		        					isMatch=false;
	    		        					break;
	    		        				}
	    	        				}
	    	        				
	    	        				if(cancel==true) {//取消匹配
	    	    	        			isMatch=false;
	    	        					break;
	    	        				}
	    	        			}
	    	        			if(isMatch==true&&cancel==false) {//匹配成功
	    	        				match.setVisible(false);
	    	        				
	    	        				JOptionPane.showMessageDialog(null,"匹配成功，您将与一名真人对手对战","提示",JOptionPane.WARNING_MESSAGE);
	    	        				JOptionPane.showMessageDialog(null,"您的对手为"+rival1+"\n游戏即将开始！","提示",JOptionPane.WARNING_MESSAGE);

	    	        				try {
									mf=new MainFrame(data,user,2,questionSet);
								} catch (NotSerializableException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} //1V1对战
	    						dReceive=new dataReceive(mode,user,mf);
	    						mf.f3.headerpanel.button2.setVisible(false);//关掉ask for help
	    						mf.nextRound.buttons.Yes.addActionListener(new nextRoundListener(true,mode));
	    						mf.nextRound.buttons.No.addActionListener(new nextRoundListener(false,mode));
	    						mf.setVisible(true);
	    	        			}
	    	        			else if(isMatch==false&&cancel==false) {//匹配失败
	    	        				JOptionPane.showMessageDialog(null,"匹配失败，您将与系统对战","提示",JOptionPane.WARNING_MESSAGE);
	    	        			    try {
										mf=new MainFrame(data,user,1,questionSet);
									} catch (NotSerializableException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} //人机对战
	    	        				mf.f4.setVisible(false); //关掉聊天窗口
	    	        				mf.f3.headerpanel.button2.setVisible(false);//关掉ask for help
	    	        				mf.nextRound.buttons.Yes.addActionListener(new nextRoundListener(true,mode));
	    	        				mf.nextRound.buttons.No.addActionListener(new nextRoundListener(false,mode));
	    	        				mf.setVisible(true);
	    	        			}
	    	        			else if(cancel==true){
	    	        				cancel=false;
	    	        			}
	        				}
	        			};
	        			
	        			delayTimer.schedule(timerTask, 4000);
	     

				} catch (NotSerializableException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(mode==3){//2V2
				Cancelmode=3;
				try {
        			DataOutputStream toServer1 = new DataOutputStream(user.socket.getOutputStream());
        			toServer1.writeUTF("0C#"+user.username);//发送对战请求
        			toServer1.flush();
        			waiting=true;
        			match=new MatchFrame();
        			match.b1.addActionListener(new cancelMatchListener());
        			match.setVisible(true);
        			final Timer delayTimer=new Timer();
        			TimerTask timerTask=new TimerTask()
        			{
        				public void run()
        				{
        					while(waiting==true) {
        					
    	        				boolean canWrite=true;
    	        				try {
    	        					getResult=fromServer.readUTF();//接受服务器传回的信息
    	        				}
    	        				catch(EOFException e1)
    	        				{
    	        					
    	        				}
    	        				catch(NullPointerException e2) {
    	        					canWrite=false;
    	        				} catch (IOException e) {
									e.printStackTrace();
								}
    	        				
    	        				if(canWrite) {
    	        					System.out.println(getResult);
    		        				if(getResult.charAt(1)=='D') {//匹配成功
    		        					String[] array=getResult.split("#");
    		        					isMatch=true;
    		        					if(user.username.equals(array[1])){//获取对手与队友用户名
    		        					   teammate=array[2];
    		        					   rival1=array[3];
    		        					   rival2=array[4];
    		        					}
    		        					else if(user.username.equals(array[2])){
     		        					   teammate=array[1];
     		        					   rival1=array[3];
     		        					   rival2=array[4];
     		        					}
    		        					else if(user.username.equals(array[3])){
     		        					   teammate=array[4];
     		        					   rival1=array[1];
     		        					   rival2=array[2];
     		        					}
    		        					else if(user.username.equals(array[4])){
     		        					   teammate=array[3];
     		        					   rival1=array[1];
     		        					   rival2=array[2];
     		        					}
    		        				    break;
    		        				}
    		        				if(getResult.equals("10")) {//匹配失败
    		        					isMatch=false;
    		        					break;
    		        				}
    	        				}
    	        				
    	        				if(cancel==true) {
    	    	        			isMatch=false;
    	        					break;
    	        				}
    	        			}
    	        			if(isMatch==true&&cancel==false) {//匹配成功
    	        				match.setVisible(false);
    	        				JOptionPane.showMessageDialog(null,"匹配成功，您将和一名队友，与另外一组队伍进行对战","提示",JOptionPane.WARNING_MESSAGE);
    	        				JOptionPane.showMessageDialog(null,"您的队友为"+teammate+"\n对方队伍成员为："+rival1+"与"+rival2+"\n游戏即将开始！","提示",JOptionPane.WARNING_MESSAGE);
    	        				try {
								mf=new MainFrame(data,user,3,questionSet);
							} catch (NotSerializableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} //1V1对战
    						dReceive=new dataReceive(mode,user,mf);
    						mf.f3.headerpanel.button2.addActionListener(new helpListener());
    						mf.f3.headerpanel.button2.setVisible(true);//开启ask for help
    						mf.nextRound.buttons.Yes.addActionListener(new nextRoundListener(true,mode));
    						mf.nextRound.buttons.No.addActionListener(new nextRoundListener(false,mode));
    						mf.setVisible(true);
    	        			}
    	        			else if(isMatch==false&&cancel==false) {//匹配失败
    	        				JOptionPane.showMessageDialog(null,"匹配失败，您将与系统对战","提示",JOptionPane.WARNING_MESSAGE);
    	        			    try {
									mf=new MainFrame(data,user,1,questionSet);
								} catch (NotSerializableException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} //人机对战
    	        				mf.f4.setVisible(false); //关掉聊天窗口
    	        				mf.f3.headerpanel.button2.setVisible(false);//关掉ask for help
    	        				mf.nextRound.buttons.Yes.addActionListener(new nextRoundListener(true,mode));
    	        				mf.nextRound.buttons.No.addActionListener(new nextRoundListener(false,mode));
    	        				mf.setVisible(true);
    	        			}
    	        			else if(cancel==true){
    	        				cancel=false;
    	        			}
        				}
        			};
        			
        			delayTimer.schedule(timerTask, 4000);

			} catch (NotSerializableException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		}
		
	}
	
	class cancelMatchListener implements ActionListener{//取消匹配并告知服务器
		public cancelMatchListener(){
		}
		public void actionPerformed(ActionEvent e) {
			
			try {
				DataOutputStream toServer2 = new DataOutputStream(user.socket.getOutputStream());
				toServer2.writeUTF("0B#"+Cancelmode+"#"+user.username+"#");
				toServer2.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//发送对战请求
			
			msf.setVisible(true);
			match.setVisible(false);
			waiting=false;
			cancel=true;
		}
	}
	
	class nextRoundListener implements ActionListener{//判断是否进行下一轮
		public boolean listenerType;
		public int mode;
		public nextRoundListener(boolean nextType,int mode){
			this.listenerType=nextType;	
			this.mode=mode;
		}
		public void actionPerformed(ActionEvent e) {
			if(listenerType==true){ //进行下一轮
				data=mf.data;
				data.playNext=true;//之后向后台传递data	
				try
				{
        				data.username=user.username;
	        			System.out.println(data.StringRecord(data)); //测试
        	        		DataOutputStream toServer = new DataOutputStream(user.socket.getOutputStream());
        	        		toServer.writeUTF(data.StringRecord(data)); //把data传给server        	        
        	        		toServer.flush();
				}
				catch (IOException ex)
				{
        				System.err.println(ex);
				}
				mf.nextRound.setVisible(false);
				dataRecord nextData=new dataRecord();//创建记录下一轮的data
				GameModule next=new GameModule(nextData,user,questionSet,ip);
			}
			else{
				data=mf.data;
				data.playNext=false;//游戏结束，向后台传递data和用户下线信息
				try
				{
	        			data.username=user.username;
	        			System.out.println(data.StringRecord(data)); //测试
        	        		DataOutputStream toServer = new DataOutputStream(user.socket.getOutputStream());
        	        		toServer.writeUTF(data.StringRecord(data)); //把data传给server        	        
        	        		toServer.flush();
				}
				catch (IOException ex)
				{
        				System.err.println(ex);
				}
				mf.nextRound.setVisible(false);
			}
			
		}
	}
	
	   
	class helpListener implements ActionListener{//求助队友

		  public helpListener() {}
		   
		  public void actionPerformed(ActionEvent e) {
			  try {
      			DataOutputStream toServer4 = new DataOutputStream(user.socket.getOutputStream());
      			toServer4.writeUTF("06#"+user.username+"#"+mf.f3.questionPanel.text+"#"+mf.f3.choicepanel.options[0].getText()+"#"+mf.f3.choicepanel.options[1].getText()+"#"+mf.f3.choicepanel.options[2].getText()+"#"+mf.f3.choicepanel.options[3].getText()+"#");//发送求助信息，将题目和选项发送到聊天框
      			toServer4.flush();


			} catch (NotSerializableException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			  mf.f3.headerpanel.button2.setVisible(false);
			}
			
		}

	class dataReceive{//用于接受服务器返回的信息
		public DataInputStream fromServer;
		public int mode;
		public UserInfo user;
		public String msg=new String();
		public MainFrame mf;
		
		public dataReceive(int mode,UserInfo user,MainFrame mf) {
			this.mode=mode;
			this.user=user;
			this.mf=mf;
			try {
				fromServer = new DataInputStream(user.socket.getInputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			new Thread(new linread()).start();
		}
		
        class linread implements Runnable
	 	{  
	 		 
			 public void run()
	 		{
	 			read();
	 		}

	 		public void read()
	 		{
	 			//while(isConnected)
	 			//{
	 				try 
	 				{
	 					while(true)
	 					{
	 						String msg = fromServer.readUTF();
	 						if("SYSTEM_CLOSED".equals(msg))
	 						{
	 							JOptionPane.showMessageDialog(null,"读取消息失败(服务器关闭/网络故障)！","错误",JOptionPane.OK_OPTION);
	 							
	 						}
	 						else if(msg!=null)
	 						{
	 							if(msg.charAt(1)=='3'){
	 								mf.f4.allmsg.append(msg.substring(2,msg.length())+"\n");   //打印聊天内容到聊天框  
	 							}
	 							if(msg.charAt(1)=='5'){//实时更新得分
	 							    if(mode==3) {//2v2更新队友（01）和对手（02）得分
	 							    	     String[] array=msg.split("#");
	 							    	     if(array[1].equals("01")) {
	 							    	    	     mf.f1.partnerScore=Integer.parseInt(array[2]);
	 							    	    	     mf.f1.update(0,0,0);
	 							    	     }
	 							    	     else if(array[1].equals("02")){
	 							    	    	     mf.f1.rivalScore=Integer.parseInt(array[2]);
	 							    	    	     mf.f1.update(0,0,0);
	 							    	     }
	 							    }
		 							else {//1v1更新对手得分
		 						          String[] array=msg.split("#");
		 		    			              System.out.println("thisis msg"+msg);
		 		    			              if(!array[2].equals(user.username)) {
		 		    			               mf.f1.rivalScore=Integer.parseInt(array[3]);
		 		    			               mf.f1.update(0,0,0);
		 		    			              }
	 		    			           }
	 							}
	 							if(msg.charAt(1)=='6'){//打印求助信息到聊天窗口
	 								mf.f4.allmsg.append(msg.substring(3,msg.length())+"\n");
	 							}
								if(msg.charAt(1)=='A') {//接受服务器返回的1v1双人对战对战结果信息
								    mf.higherScore=Integer.parseInt(msg.split("#")[1]);
								    System.out.println(mf.higherScore);
								    if(Integer.parseInt(msg.split("#")[1])==data.totalScore) {
								    	mf.rivalScore=Integer.parseInt(msg.split("#")[2]);
								    	System.out.println(mf.rivalScore);
								    }
								    else {
								    	mf.rivalScore=Integer.parseInt(msg.split("#")[1]);
								    	System.out.println(mf.rivalScore);
								    }
								    mf.result=true;
								}
								if(msg.charAt(1)=='E') {//接受服务器返回的2v2对战对战结果信息
									System.out.println(msg);
									String[] array=msg.split("#");
									if(user.username.equals(array[1])||user.username.equals(array[4])) {//用户名位于返回消息中前两个的用户位置的用户，为胜利队伍的用户
										mf.isWon=true;
										System.out.println("iswon"+mf.isWon);
										mf.teamRound1=Integer.parseInt(array[2])+Integer.parseInt(array[5]);
										System.out.println("iswon"+mf.teamRound1);
										mf.teamRound2=Integer.parseInt(array[3])+Integer.parseInt(array[6]);
										System.out.println("iswon"+mf.teamRound2);
										mf.rivalScore=Integer.parseInt(array[8])+Integer.parseInt(array[9])+Integer.parseInt(array[11])+Integer.parseInt(array[12]);
										System.out.println("iswon"+mf.rivalScore);
									}
									else {//用户名处于返回信息中后两个用户名位置的用户，为失败队伍的用户 
										mf.isWon=false;
										mf.teamRound1=Integer.parseInt(array[8])+Integer.parseInt(array[11]);
										mf.teamRound2=Integer.parseInt(array[9])+Integer.parseInt(array[12]);
										mf.rivalScore=Integer.parseInt(array[2])+Integer.parseInt(array[3])+Integer.parseInt(array[5])+Integer.parseInt(array[6]);
									}
									mf.result=true;
								}
	 						}
	 				}
	 				} 
	 				catch (IOException e) 
	 				{     
	 				}                  
	 			}		
	 		}
	}
}