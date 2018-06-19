//client-server模式的搭建主要参考了ITye博客中Sshyfantian的开源代码“一个简单的UDP聊天室”，链接如下:
//http://sunshyfangtian.iteye.com/blog/642334
//Server端的主程序，用于存放并显示所有连接到该服务器的客户端的信息，包括客户端传递到服务器的信息，向客户端传递信息，以及与后台数据库的连接

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.*;

//存放连接此服务器的用户的信息————————————————START
public class ServerUserInfo extends JFrame{
	private ObjectInputStream inputFromClient;
	private DataOutputStream outputToClient;
	private JTextArea allmsg;
	private JTextField currnum,totalnum;
	private JScrollPane js;
	int num1,num2,num3=0,num4=0,num5=0; 
	//num1:当前在线人数  num2:总上线人数 num3:1v1上线人数 num4: 已结束游戏人数
	int indicator=0; //0 用户名不存在 注册  1 密码错误  2 登录成功
	private ServerSocket serverSocket;
	ArrayList<UserInfo> lists;//存放所有在线用户
	//ArrayList<String> list2v2=new ArrayList<String>();//2v2用户名
	ArrayList<UserInfo> list2v2Info=new ArrayList<UserInfo>();//存放2v2用户
	ArrayList<String> list1v1=new ArrayList<String>();//1v1用户名
	ArrayList<UserInfo> list1v1Info=new ArrayList<UserInfo>();//存放1v1用户
	public dataRecord data=new dataRecord();
	public boolean isBattle;
	int[] totalscore=new int[2]; //存放对战双方总成绩
	public userScore[] userscore=new userScore[4];
	int higherScore=0,lowerScore=0;//双人对战中的最高分
	public String winner1=new String();
	public String winner2=new String();
//存放连接此服务器的用户的信息——————————————————END
	
	//从登陆读入用户信息开始接入此客户
	public static void main(String[] args) throws ClassNotFoundException {      
        new ServerUserInfo();
	}
  
	public ServerUserInfo() throws ClassNotFoundException 
	{
		super("服务器端");
		this.setSize(310,660);
		this.setLocation(200,50);
		lists = new ArrayList<UserInfo>();
		num1 = num2 =0;
		currnum = new JTextField(" 当前在线人数： "+num1);
		currnum.setEnabled(false);
  
		totalnum = new JTextField(" 上线总人数： "+num2);
		totalnum.setEnabled(false);
  
		allmsg = new JTextArea();
		allmsg.setEditable(false);
		allmsg.setLineWrap(true); //允许自动换行
		
		js = new JScrollPane(allmsg);//为JTextArea添加滚动条  
		
		for(int i=0;i<4;i++){
			userscore[i]=new userScore();
		}

		addcomponettocontainer(); //添加窗口小组件
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we)
			{
				sendmsg("SYSTEM_CLOSED",lists); /*  -- 向客户端发送服务器关闭信息 --  */
				destory();
			}
		});
		
		start(); /*  -- 启动连接服务 --  */   
		
 	}

	
	public void addcomponettocontainer()
	{
		Container c = this.getContentPane();
		c.setLayout(null);
  
		currnum.setBounds(20,15,130,20);
		totalnum.setBounds(155,15,125,20);
		js.setBounds(10,50,280,500);
  
		c.add(currnum);
		c.add(totalnum);
		c.add(js);
  
		this.setVisible(true);
		this.setResizable(false);
	}

	//将时间转化成固定形式
	public static String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	} 
	
	//创建socket
	public void start() throws ClassNotFoundException
	{
		boolean isStarted = false; /*  -- 用于标记服务器是否已经正常启动 --  */
		try {
			// Create a server socket
			this.serverSocket = new ServerSocket(8000);
			isStarted = true;
			this.allmsg.append(DateToStr(new Date())+"    服务器启动\n");
			while (isStarted) 
			{
				Socket socket = serverSocket.accept(); //client socket
				inputFromClient = new ObjectInputStream(socket.getInputStream());
				outputToClient = new DataOutputStream(socket.getOutputStream());

				User u=(User)inputFromClient.readObject(); //接收用户信息				
				
				UserInfo user = new UserInfo(); //新建用户
					
				user.username=u.username;
				user.socket=socket;
				lists.add(user); //将该用户加到列表中去
				num1++;
				num2++;
				currnum.setText(" 当前在线人数： "+num1);
				totalnum.setText(" 上线总人数： "+num2);
				this.allmsg.append(DateToStr(new Date())+" : "+u.username+" 登录 \n");
				this.allmsg.append(u.username+"的IP地址是"+socket.getInetAddress().getHostAddress() + '\n');
			
				new Thread(new ClientThread(user)).start(); //为该用户启动一个通信线程 

				//读取用户名和密码
				String username=u.username;
				String key=String.valueOf(u.password);
				System.out.println("用户名: "+username);
				System.out.println("密码: "+key);
         
				
				
				
				
				// Connect to the database————————————————START
				
				String driver = "com.mysql.jdbc.Driver";
		        String url="jdbc:mysql://localhost:3306/EXPERIMENT";
		        String user1 = "root"; 
		        String password2 = "sunshine";
		        String name=username;
		        String psw=key;
		        
		      //Get problems from the database————————————START
		          
		          try 
		          {
		           Class.forName(driver);
		           Connection conn = DriverManager.getConnection(url, user1, password2);

		           Statement statement = conn.createStatement();
		           String sql = "select * from problemset";
		           ResultSet rs = statement.executeQuery(sql);
		           
		           String problemset2 = new String();
		           String picture = new String();
		           problemset2="01#";
		           picture ="0P#";
		           while(rs.next()) {
		        	  problemset2=problemset2+rs.getString("idproblem")+"$"+rs.getString("statement") + "$" +rs.getString("A")+"$"+rs.getString("B")+"$"+rs.getString("C")+"$"+rs.getString("D")+"$"+ rs.getString("answer")+"#";		 
		           }
		           outputToClient.writeUTF(problemset2); //将problemset传到客户端		         
			       outputToClient.flush();
			       
			       ResultSet rs2 = statement.executeQuery(sql);
		           while(rs2.next()) {
		        	  if(!rs2.getString("picture").equals(""))
		        	  {
		        		  picture = picture+rs2.getString("idproblem")+"$"+rs2.getString("picture")+"#";
		        		  outputToClient.writeUTF(picture);
		        		  outputToClient.flush();
		        		  System.out.println(picture);
		        		  picture = "0P#";
		        	  }
		 
		           }
		           rs.close();
		           conn.close();
		          } 

		          catch(ClassNotFoundException e) {
		           System.out.println("Sorry,can`t find the Driver!"); 
		           e.printStackTrace();
		          } catch(SQLException e) {e.printStackTrace();
		          } catch(Exception e) {e.printStackTrace();}   
		          
		          
		          
		          
		        //Get problems from the database————————————END
		          
	
		         // outputToClient.writeUTF("02"+indicator);
		        //  outputToClient.flush(); //将indicator传到客户端
		        
		        
		          try { 

		        		Class.forName(driver);
		        		Connection conn = DriverManager.getConnection(url, user1, password2);

		         
		       //Connect to the database—————————————————————END
		        				        		
		       //Search the database——————————————————————START
		       
		        		//查看用户是否存在		         		         
		        		Statement statement = conn.createStatement();
		        		String sql = "select * from player";
		        		ResultSet rs = statement.executeQuery(sql);
		        		while(rs.next())
		        		{
		        			if(rs.getString("idplayer").equals(username))
		        			{
		        				//该用户存在，再查看用户密码是否正确		        				
		        				if(rs.getString("password").equals(key))
		        				{
		        					//密码正确,向用户发送成功登录的indicator
		        					indicator=2;
		        					System.out.println("成功登陆");
		        				}
		        				else
		        				{
		        					//如果密码错误，给client提示密码错误
		        					indicator=1;
		        					System.out.println("密码错误");
		        				}				        		
		        			}
		        			
		        		}		 		        		
		        		if (indicator==0)
		        			System.out.println("该用户不存在");
		        //Search the database——————————————————————END
		       
		        			        	
		        		outputToClient.writeUTF("02"+indicator); //将indicator传到客户端
		        		outputToClient.flush();		            							       
		                indicator=0;
		        		// Write to the database————————————————START
		        		String insert = "INSERT INTO player(idplayer, password, score, 1V1, 2V2)"+
		                         "VALUES"+
		                         "(?,?,'0','0','0')";
		   
		        		PreparedStatement ptmt =conn.prepareStatement(insert);
		         
		        		ptmt.setString(1,name);
		        		ptmt.setString(2,psw);
		         
		        		ptmt.execute();
		       
		        		conn.close();
		        } 
		        catch(ClassNotFoundException e) 
		        {
		        		System.out.println("Sorry,can't find the Driver!"); 
		        		e.printStackTrace();
		        } 
		        catch(SQLException e) 
		        {
		        		e.printStackTrace();
		        	}		        
		        catch(Exception e) 
		        {
		        		e.printStackTrace();
		        }
	        		//Write to the database————————————————END

				}
			}
			catch(ClassNotFoundException ex) 
			{
				ex.printStackTrace();
			}
			catch(IOException ex) 
			{
				ex.printStackTrace();
			}
				
		
	}
	//通信线程，监听客户端输入的信息，并且根据接收到的信息把反应传递给相关用户

	 	class ClientThread implements Runnable
	 	{
	 		UserInfo user = null;
	 		boolean isConnected = true;
	 		DataInputStream dis = null;
	  
	 		public ClientThread(UserInfo u)
	 		{
	 			this.user = u; 
	 			try {
	 				this.dis = new DataInputStream(this.user.socket.getInputStream());
	 			} 
	 			catch (IOException e) {
	 				e.printStackTrace();
	 			}
	 		}
	  
	 		public void run()
	 		{
	 			try 
	 			{
					readmsg();	 						
				} 
	 			catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 			
	 		}
	  
	 		  //读取客户的聊天信息、得分信息、实验数据、1v1用户名
	 		public void readmsg() throws ClassNotFoundException
	 		{
	 			while(isConnected)
	 			{
	 				try {         
	 					String msg=new String(); 
	 					msg = dis.readUTF(); //读客户端数据			
	 					if("quit&logout".equals(msg))//当用户关闭客户端窗口时，发送quit字符串 表示用户已经退出
	 					{
	 						num1--;       
	 						try{
	 							this.dis.close();
	 							this.user.socket.close();
	 							this.isConnected = false;
	 						}
	 						catch(IOException ioe)
	 						{
	 							ioe.printStackTrace();
	 						}
	 						finally{
	 							this.isConnected = false;
	 							if(dis!=null) this.dis.close();
	 							if(this.user.socket!=null) 
	 								this.user.socket.close();
	 						}
	 						lists.remove(this.user);//从列表中删除该用户
	 						currnum.setText(" 当前在线人数： "+num1);
	 						allmsg.append(DateToStr(new Date())+"  : "+this.user.username+"  退出\n");
	 					}
	 					else
	 					{
	 						//用户选择模式，服务器自动匹配
	 						if(msg.charAt(1)=='3')
		 					{
		 						allmsg.append(msg.substring(5,msg.length())); //打印到服务器端
		 						String[] getCon=msg.split("#");
		 						allmsg.append("\n");	
		 						if(msg.charAt(3)=='2') {
		 							msg="03"+getCon[2]+"    "+getCon[3]+":"+getCon[4];
		 							sendmsg(msg,list1v1Info); //传回客户端
		 						}
		 						else if(msg.charAt(3)=='3') {
		 							if(msg.split("#")[3].equals(list2v2Info.get(0).username))
		 							{
		 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
		 							    needToSend.add(list2v2Info.get(1));
		 							    needToSend.add(list2v2Info.get(0));
		 							    msg="03"+getCon[2]+"    "+getCon[3]+":"+getCon[4];
		 							    sendmsg(msg,needToSend);
		 							}
		 							else if(msg.split("#")[3].equals(list2v2Info.get(1).username))
		 							{
		 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
		 							    needToSend.add(list2v2Info.get(0));
		 							    needToSend.add(list2v2Info.get(1));
		 			  				    msg="03"+getCon[2]+"    "+getCon[3]+":"+getCon[4];
		 							    sendmsg(msg,needToSend);
		 							}
		 							else if(msg.split("#")[3].equals(list2v2Info.get(2).username))
		 							{
		 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
		 							    needToSend.add(list2v2Info.get(3));
		 							   needToSend.add(list2v2Info.get(2));
		 							    msg="03"+getCon[2]+"    "+getCon[3]+":"+getCon[4];
		 							    sendmsg(msg,needToSend);
		 							}
		 							else if(msg.split("#")[3].equals(list2v2Info.get(3).username))
		 							{
		 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
		 							    needToSend.add(list2v2Info.get(2));
		 							   needToSend.add(list2v2Info.get(3));
		 							    msg="03"+getCon[2]+"    "+getCon[3]+":"+getCon[4];
		 							    sendmsg(msg,needToSend);
		 							}
		 						}
		 					}
	 						//实验数据，对实验数据进行分割
	 						if(msg.charAt(1)=='4') //对实验数据进行分割
	 						{
	 							separate(msg.substring(2,msg.length()));
	 							System.out.println(msg); //测试
	 						}
	 						if(msg.charAt(1)=='5') //得分
	 						{
                                String[] array=msg.split("#");
                                
	 							if(array[1].equals("02")) {
	 								//System.out.println(msg);
	 								sendmsg(msg,lists);
	 							}
	 							else if(array[1].equals("03")){
	 								int player=0;
	 								for(int i=0;i<4;i++){
	 									if(array[2].equals(userscore[i].userName)){
	 										player=i;
	 										userscore[i].totalScore=Integer.parseInt(array[3]);
	 									}
	 								}
	 								if(player==0||player==1){
	 									ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
	 									msg="05#01#"+array[3];
		 							    needToSend.add(list2v2Info.get(1-player));
		 							    sendmsg(msg,needToSend);
		 							    
		 							    needToSend=new ArrayList<UserInfo>();
		 							    int total=userscore[0].totalScore+userscore[1].totalScore;
	 									msg="05#02#"+total;
		 							    needToSend.add(list2v2Info.get(2));
		 							    needToSend.add(list2v2Info.get(3));
		 							    sendmsg(msg,needToSend);
	 								}
	 								else if(player==2||player==3){
	 									ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
	 									msg="05#01#"+array[3];
		 							    needToSend.add(list2v2Info.get(5-player));
		 							    sendmsg(msg,needToSend);
		 							    
		 							    needToSend=new ArrayList<UserInfo>();
		 							    int total=userscore[2].totalScore+userscore[3].totalScore;
	 									msg="05#02#"+total;
		 							    needToSend.add(list2v2Info.get(0));
		 							    needToSend.add(list2v2Info.get(1));
		 							    sendmsg(msg,needToSend);
	 								}
	 								
	 							}
	 							else sendmsg(msg,lists); //传回客户端
	 						}
	 						//求助队友信息，向聊天窗口输入目前所在题的内容
	 						if(msg.charAt(1)=='6'){
	 							String[] getHelp=msg.split("#");
	 							if(msg.split("#")[1].equals(list2v2Info.get(0).username))
	 							{
	 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
	 							    needToSend.add(list2v2Info.get(1));
	 							    needToSend.add(list2v2Info.get(0));
	 							    msg="06#【"+getHelp[1]+"向队友发起求助】问题为："+getHelp[2]+"\n选项是：\n"+getHelp[3]+"\n"+getHelp[4]+"\n"+getHelp[5]+"\n"+getHelp[6];
	 							    sendmsg(msg,needToSend);
	 							}
	 							else if(msg.split("#")[1].equals(list2v2Info.get(1).username))
	 							{
	 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
	 							    needToSend.add(list2v2Info.get(0));
	 							    needToSend.add(list2v2Info.get(1));
	 							    msg="06#【"+getHelp[1]+" 向队友发起求助】问题为："+getHelp[2]+"\n选项是：\n"+getHelp[3]+"\n"+getHelp[4]+"\n"+getHelp[5]+"\n"+getHelp[6];
	 							    sendmsg(msg,needToSend);
	 							}
	 							else if(msg.split("#")[1].equals(list2v2Info.get(2).username))
	 							{
	 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
	 							    needToSend.add(list2v2Info.get(3));
	 							    needToSend.add(list2v2Info.get(2));
	 							    msg="06#【"+getHelp[1]+" 向队友发起求助】问题为："+getHelp[2]+"\n选项是：\n"+getHelp[3]+"\n"+getHelp[4]+"\n"+getHelp[5]+"\n"+getHelp[6];
	 							    sendmsg(msg,needToSend);
	 							}
	 							else if(msg.split("#")[1].equals(list2v2Info.get(3).username))
	 							{
	 								ArrayList<UserInfo> needToSend=new ArrayList<UserInfo>();
	 							    needToSend.add(list2v2Info.get(2));
	 							    needToSend.add(list2v2Info.get(3));
	 							    msg="06#【"+getHelp[1]+" 向队友发起求助】问题为："+getHelp[2]+"\n选项是：\n"+getHelp[3]+"\n"+getHelp[4]+"\n"+getHelp[5]+"\n"+getHelp[6];
	 							    sendmsg(msg,needToSend);
	 							}
	 						}
	 						//是否匹配成功
	 						if(msg.charAt(1)=='7') 
	 						{
	 							System.out.println(msg);
	 							if(isBattle==false) 
	 							{
		 							list1v1.add(msg.split("#")[1]);
		 							num3++;
		 							for(int i=0;i<lists.size();i++) {
		 								System.out.println(i);
		 								if( ((UserInfo)lists.get(i)).username.equals(msg.split("#")[1])) {
		 									list1v1Info.add((UserInfo)lists.get(i));
		 								}
		 							}
		 							if(num3==2) 
		 							{
		 								num3=0;
		 							    list1v1=new ArrayList<String>();
		 							    isBattle=true;
		 							    sendmsg("09#"+list1v1Info.get(0).username+"#"+list1v1Info.get(1).username+"#",list1v1Info);//发送匹配成功信息
		 							    System.out.println("match succeed");
		 							}
	 						    }
	 							else
	 							{
	 								sendmsg("10",lists); 
	 								//发送目前无法进行对战，自动进入人机对战
	 							}
	 						}
	 						if(msg.charAt(1)=='C') 
	 						{
	 							System.out.println(msg);
	 							if(isBattle==false) 
	 							{
		 							userscore[num5].userName=msg.split("#")[1];
		 							System.out.println(userscore[num5].userName);		 							
		 							num5++;
		 							for(int i=0;i<lists.size();i++) {
		 								System.out.println(i);
		 								if( ((UserInfo)lists.get(i)).username.equals(msg.split("#")[1])) {
		 									list2v2Info.add((UserInfo)lists.get(i));
		 								}
		 							}
		 							if(num5==4) 
		 							{
		 								num5=0;
		 							    isBattle=true;
		 							    sendmsg("0D#"+list2v2Info.get(0).username+"#"+list2v2Info.get(1).username+"#"+list2v2Info.get(2).username+"#"+list2v2Info.get(3).username+"#",list2v2Info);//发送匹配成功信息
		 							    System.out.println("2v2 match succeed");
		 							}
	 						    }
	 							else
	 							{
	 								sendmsg("10",lists); 
	 								//发送目前无法进行对战，自动进入人机对战
	 							}
	 						}
	 						//对战结果，比较得分，把对战结果反馈给用户
	 						if(msg.charAt(1)=='8') 
	 						{
	 							System.out.println(msg);
	 							totalscore[num4]+=Integer.parseInt(msg.split("#")[1]);
	 							System.out.println(totalscore[num4]);
	 							num4++;
	 							if(num4==2) {
	 								isBattle=false;
	 								if(totalscore[0]>=totalscore[1]) {
	 									higherScore=totalscore[0];
	 									lowerScore=totalscore[1];
	 								}
	 								else {
	 									higherScore=totalscore[1];
	 									lowerScore=totalscore[0];
	 								}
	 								System.out.println("0A#"+higherScore+"#"+lowerScore);
	 								sendmsg("0A#"+higherScore+"#"+lowerScore,lists); 
	 								//把分高者的总分返回给两个对战用户，客户端自行比较
	 							}
	 							list1v1Info=new ArrayList<UserInfo>();
	 							if(num4==2){
	 							  num4=0;
	 							}
	 						}
	 						
	 						if(msg.charAt(1)=='F') 
	 						{
	 							System.out.println(msg);
	 							for(int j=0;j<4;j++) {
	 								System.out.println("userscore "+userscore[j].userName);
	 								System.out.println("msg"+msg.split("#")[1]);
		 							if(userscore[j].userName.equals(msg.split("#")[1])) {
		 								System.out.println("true");
			 							userscore[j].round1Score=Integer.parseInt(msg.split("#")[2]);
			 							System.out.println("msg"+msg.split("#")[2]);
			 							System.out.println(userscore[j].round1Score);
			 							userscore[j].round2Score=Integer.parseInt(msg.split("#")[3]);
			 							System.out.println("msg"+msg.split("#")[3]);
			 							System.out.println(userscore[j].round2Score);
			 							userscore[j].totalScore=Integer.parseInt(msg.split("#")[4]);
			 							System.out.println(userscore[j].totalScore);
		 							}
		 							
	 							}
	 							num4++;
	 							if(num4==4) {
	 								isBattle=false;
	 								if(userscore[0].totalScore+userscore[1].totalScore>=userscore[2].totalScore+userscore[3].totalScore) {
	 									System.out.println("0E#"+userscore[0].userName+"#"+userscore[0].round1Score+"#"+userscore[0].round2Score+"#"+userscore[1].userName+"#"+userscore[1].round1Score+"#"+userscore[1].round2Score+"#"+userscore[2].userName+"#"+userscore[2].round1Score+"#"+userscore[2].round2Score+"#"+userscore[3].userName+"#"+userscore[3].round1Score+"#"+userscore[3].round2Score+"#");
	 									sendmsg("0E#"+userscore[0].userName+"#"+userscore[0].round1Score+"#"+userscore[0].round2Score+"#"+userscore[1].userName+"#"+userscore[1].round1Score+"#"+userscore[1].round2Score+"#"+userscore[2].userName+"#"+userscore[2].round1Score+"#"+userscore[2].round2Score+"#"+userscore[3].userName+"#"+userscore[3].round1Score+"#"+userscore[3].round2Score+"#",list2v2Info); 
	 								}
	 								else {
	 									System.out.println("0E#"+userscore[0].userName+"#"+userscore[0].round1Score+"#"+userscore[0].round2Score+"#"+userscore[1].userName+"#"+userscore[1].round1Score+"#"+userscore[1].round2Score+"#"+userscore[2].userName+"#"+userscore[2].round1Score+"#"+userscore[2].round2Score+"#"+userscore[3].userName+"#"+userscore[3].round1Score+"#"+userscore[3].round2Score+"#");
	 									
	 									sendmsg("0E#"+userscore[2].userName+"#"+userscore[2].round1Score+"#"+userscore[2].round2Score+"#"+userscore[3].userName+"#"+userscore[3].round1Score+"#"+userscore[3].round2Score+"#"+userscore[0].userName+"#"+userscore[0].round1Score+"#"+userscore[0].round2Score+"#"+userscore[1].userName+"#"+userscore[1].round1Score+"#"+userscore[1].round2Score+"#",list2v2Info); 
	 								}	 					 							
	 								//把分高者的总分返回给两个对战用户，客户端自行比较
	 								userscore=new userScore[4];
	 							}
	 							
	 							if(num4==4)
	 							num4=0;
	 						}
	 						if(msg.charAt(1)=='B') 
	 						{
	 							
	 							if(msg.charAt(3)=='2'){
	 								list1v1.remove(0);
	 								list1v1Info.remove(0);
	 							    num3--;
	 							    System.out.println("num3is:"+num3);
	 							}
	 							else if(msg.charAt(3)=='3'){
	 								String userName=msg.split("#")[2];
	 								for(int i=0;i<num5;i++){
	 									if(userscore[i].userName.equals(userName)){
	 										if(i<num5-1){
	 											for(int j=i;j<num5-1;j++){
	 												userscore[j].userName=userscore[j+1].userName;
	 											}
	 											userscore[num5-1].userName="nobody";
	 										}
	 										else if(i==num5-1){
	 											userscore[num5-1].userName="nobody";
	 										}
	 										break;
	 									}
	 									
	 								}
	 								for(int i=0;i<list2v2Info.size();i++){
	 									if(list2v2Info.get(i).username.equals(userName)){
	 										list2v2Info.remove(i);
	 										break;
	 									}
	 								}
	 								num5--;
	 							}
	 						}

	 					}
	 				} 
	 				catch (EOFException ex) {}
	 				catch (IOException e) {
	 					e.printStackTrace();
	 				}
	 			}
	 		}    
	     	 	 
	 	
	 	}
	   
 		//将信息进行转发
		public void sendmsg(String msg,ArrayList<UserInfo> needToSend)
	 	{
	 		UserInfo user = new UserInfo();
	 		DataOutputStream os = null;
	 		System.out.println("before iteration");
	 		if(needToSend.size()>0)
	 		{
	 			for(int i=0;i<needToSend.size();i++)
	 			{
	 				System.out.println("iteration"+i);
	 				user = (UserInfo)needToSend.get(i);
	 				System.out.println(msg+"is sent to"+user.username);
	 				try {   
	 					os = new DataOutputStream(user.socket.getOutputStream());
	 					os.writeUTF(msg);
	 					
	 				} catch (IOException e) {
	 					System.out.println("Wrong!");
	 					e.printStackTrace();
	 				}
	 			}
	 		} 
	 		//else
	 			//JOptionPane.showMessageDialog(null, "当前无用户在线。发送消息失败","失败",JOptionPane.OK_OPTION);
	 	} 
		
		//用户下线，把该socket关掉
		public void destory()
	    {
	 		try {
	 			this.serverSocket.close();
	 		} 
	 		catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		this.dispose();
	    }
	    
		//把分割后的实验数据写入数据库
		public String separate(String s) 
		{
			String[] array=s.split("#");
			data.username=array[1];
			data.WelcomeTime=Timestamp.valueOf(array[2]);
			data.IntroTime=Timestamp.valueOf(array[3]);
			data.TryTime=Timestamp.valueOf(array[4]);
			data.mode=Integer.parseInt(array[5]);
			data.round1Score=Integer.parseInt(array[6]);
			data.round2Score=Integer.parseInt(array[7]);
			String question_time=array[8];
			String[] question=question_time.split("\\$");
			String clickA=array[9];
			String clickB=array[10];
			String clickC=array[11];
			String clickD=array[12];		
			if(Integer.parseInt(array[13])==1)
				data.useHalf=true;
			else
				data.useHalf=false;
			if(Integer.parseInt(array[14])==1)
				data.useHelp=true;
			else
				data.useHelp=false;
			if(Integer.parseInt(array[15])==1)
				data.playNext=true;
			else
				data.playNext=false;
			data.totalScore=Integer.parseInt(array[16]);
			String chat_message = new String("no chatting");
			chat_message=array[17];	
			System.out.println(chat_message);
			
		      //Write experiment data to the database————————————START
			
						String driver = "com.mysql.jdbc.Driver";
				        String url="jdbc:mysql://localhost:3306/EXPERIMENT?useUnicode=true&characterEncoding=UTF-8";
				        String user1 = "root"; 
				        String password2 = "sunshine";
				        
				          
				          try 
				          {
				           Class.forName(driver);
				           Connection conn = DriverManager.getConnection(url, user1, password2);
				           String insert = "INSERT INTO experiment(idplayer, welcomeTime, introTime, tryTime, mode, startTime, round1, round2, clickA, clickB, clickC, clickD, useHalf, useHelp, playNext, totalScore)"+
			                       "VALUES"+
			                       "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			 
			      		PreparedStatement ptmt =conn.prepareStatement(insert);
			       
			      		ptmt.setString(1,array[1]);
			      		ptmt.setString(2,array[2]);
			      		ptmt.setString(3,array[3]);
			      		ptmt.setString(4,array[4]);
			      		ptmt.setString(5,array[5]);
			      		ptmt.setString(6,array[8]);
			      		ptmt.setString(7,array[6]);
			      		ptmt.setString(8,array[7]);
			      		ptmt.setString(9,array[9]);
			      		ptmt.setString(10,array[10]);
			      		ptmt.setString(11,array[11]);
			      		ptmt.setString(12,array[12]);
			      		ptmt.setString(13,array[13]);
			      		ptmt.setString(14,array[14]);
			      		ptmt.setString(15,array[15]);
			      		ptmt.setString(16, array[16]);
			       
			      		ptmt.execute();
			      		
			      		String chat = new String(chat_message.getBytes(),"utf-8");
			      		System.out.println(chat);
			      		
			      		String insert2 = "INSERT INTO talk (idplayer, dateTime, content)"+"VALUES"+"(?,?,?)";
			      		PreparedStatement ptmt2 = conn.prepareStatement(insert2);
			      		
			      		ptmt2.setString(1, array[1]);
			      		ptmt2.setString(2, question[0]);
			      		ptmt2.setString(3, chat);
			      		ptmt2.execute();
			      		
		        		Statement statement = conn.createStatement();
		        		String sql = "select * from player";
		        		ResultSet rs = statement.executeQuery(sql);
		        		while(rs.next())
		        		{
		        			if(rs.getString("idplayer").equals(data.username))
		        			{
		        					int a = Integer.parseInt(rs.getString("score"));
		        					int b = Integer.parseInt(rs.getString("1V1"));
		        					int c = Integer.parseInt(rs.getString("2V2"));
		        					a = a + data.totalScore;
		        					
		        					if(data.mode==3)
		        					{
		        						c = c+1;
		        					}
		        					else
		        					{
		        						b=b+1;
		        					}
		        					
		        					String total = ""+a;
		        					String onevone = ""+b;
		        					String twovtwo = ""+c;
		        					
		        					System.out.println(total);
		        					String update1 = "UPDATE player SET score ="+"(?)" +" WHERE idplayer = "+"(?)";
		        					PreparedStatement ptmt3 = conn.prepareStatement(update1);
		        					ptmt3.setString(1, total);
		        					ptmt3.setString(2, data.username);
		        					ptmt3.execute();
		        					System.out.println("已插入"+total);
		        					
		        					System.out.println(onevone);
		        					String update2 = "UPDATE player SET 1V1 ="+"(?)"+"WHERE idplayer = "+"(?)";
		        					PreparedStatement ptmt4 = conn.prepareStatement(update2);
		        					ptmt4.setString(1,onevone);
		        					ptmt4.setString(2, data.username);
		        					ptmt4.execute();
		        					System.out.println("已插入"+onevone);
		        					
		        					System.out.println(twovtwo);
		        					String update3 = "UPDATE player SET 2V2 ="+"(?)"+"WHERE idplayer = "+"(?)";
		        					PreparedStatement ptmt5 = conn.prepareStatement(update3);
		        					ptmt5.setString(1, twovtwo);
		        					ptmt5.setString(2, data.username);
		        					ptmt5.execute();
		        					System.out.println("已插入"+twovtwo);
		        			}		        			
		        		}
				           
				           conn.close();
				          } 

				          catch(ClassNotFoundException e) {
				           System.out.println("Sorry,can`t find the Driver!"); 
				           e.printStackTrace();
				          } catch(SQLException e) {e.printStackTrace();
				          } catch(Exception e) {e.printStackTrace();}   
				          
				          
				          
				          
				        //Write experiment to the database————————————END
			
			return chat_message;
		}
		
		//存放用户最终得分
		class userScore{
			public int round1Score=0;
			public int round2Score=0;
			public int totalScore=0;
			public String userName=new String();
			
			public userScore() {
				round1Score=0;
				round2Score=0;
				totalScore=0;
				userName="Nobody";
			}
			
			
		}
}
