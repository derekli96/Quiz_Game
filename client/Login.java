import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
//登录界面
public class Login extends JFrame {
	public JTextField IDtf=new JTextField(16);
	public JPasswordField pwpf= new JPasswordField(16);
	public String ip = "127.0.0.1"; //服务器IP地址
	public int indicator=0; //0表示用户名不存在 1密码错误 2登录成功
	public Question questionSet;
	public UserInfo user;
	public User u;
	public dataRecord data =new dataRecord();
	public int connectTime=0;
	public Timer timer=new Timer(1000,new clockListener());

	JButton LoginButton = new JButton("登录");
	JButton exitButton = new JButton("退出");
	
	JLabel IDLabel = new JLabel("用户名：");
	JLabel pwLabel = new JLabel("密    码：");
	

	
	public Login(String ip)
	{
		
		super("登录界面");
		JPanel jp1 = new JPanel(new FlowLayout());
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		
		jp1.add(IDLabel);
		jp1.add(IDtf);
		
		
		jp2.add(pwLabel);
		jp2.add(pwpf);
		
		jp3.add(LoginButton);
		jp3.add(exitButton);
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(LoginButton);
		bg.add(exitButton);
		LoginButton.setSelected(true);
		LoginButton.setToolTipText("用户名第一次登录时，会自动注册");
		
		LoginButton.addActionListener(new buttonListener());
		exitButton.addActionListener(new buttonListener());
		this.ip=ip;
		JLabel tip=new JLabel("提示：新用户名第一次登录时，会自动注册");
		tip.setHorizontalAlignment(SwingConstants.CENTER);
		tip.setFont(new Font("",Font.PLAIN,12));
		
		this.add(tip);
		this.add(jp1);
		this.add(new noteLabel("请输入16位以内用户名      "));
		this.add(jp2);
		this.add(new noteLabel("请输入16位以内拉丁字符或数字      "));
		this.add(jp3);
		this.add(new JLabel(""));
		
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(new GridLayout(7, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300,225);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

	}
	
	class clockListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			connectTime++;
			System.out.println(connectTime);
			if(connectTime>=5) {
				JOptionPane.showMessageDialog(null,"登录超时，无法连接服务器\n请检查并重新输入服务器IP","提示",JOptionPane.WARNING_MESSAGE);
				new Connect();
				setVisible(false);
				timer.stop();
			}
		}
	}
	
	 class buttonListener implements ActionListener {
	    	public void actionPerformed(ActionEvent e) {  
	  
	            if(e.getActionCommand()=="退出")  
	            {  
	                System.exit(0);  
	            }
	            if(e.getActionCommand()=="登录") 
	            {   
	            		String username = IDtf.getText().trim(); //用户名字符串
	            		String password = String.valueOf(pwpf.getPassword()); //密码字符串
	            		//错误提示
	            		//timer.start();
	            		if("".equals(username)) {
		    				JOptionPane.showMessageDialog(null,"用户名不能为空!","错误",JOptionPane.OK_OPTION);
		    			}
	            		else if(password.equals("")) {
		    				JOptionPane.showMessageDialog(null,"密码不能为空!","错误",JOptionPane.OK_OPTION);
		    			}
	            		else if(username.length()>16){
	            			JOptionPane.showMessageDialog(null,"用户名长度不得大于16位!","错误",JOptionPane.OK_OPTION);
	            		}
	             		else if(password.length()>16){
	            			JOptionPane.showMessageDialog(null,"密码长度不得大于16位!","错误",JOptionPane.OK_OPTION);
	            		}
	            		else
	            		{
	            		try {
            		    // 连接到服务器
	            			Socket socket = new Socket(ip, 8000);
	            			ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
	            			DataInputStream fromServer = new DataInputStream(socket.getInputStream());
	            			user=new UserInfo(username,socket);
	            			u=new User(username,password);
	            			toServer.writeObject(u);//将用户类传到服务器
	            			toServer.flush();
	            			String test=new String();  
	            			while(true)//读题目与indicator
	            			{

	            			test= fromServer.readUTF(); 	//读取服务器传送的信息
	            			System.out.println(test);
	            			if(test.charAt(1)=='1'){ 
	            				questionSet=new Question(test); //接收题库数据
	            			}
	            			else if(test.charAt(1)=='P'){ //接收图片
	            				questionSet.insertPicture(test);
	            			}
	            			else if (test.charAt(1)=='2') //退出循环
	            			{	            				
	            				timer.stop();
	            				break;
	            			}	            				
	            			}	            			 
	            			indicator=test.charAt(2)-'0'; //接收indicator
	            			//根据indicator进行相应操作
            				if (indicator==2)
                 	       {
                 	        		//提示登录成功
     	            				JOptionPane.showMessageDialog(null,"登录成功！","提示",JOptionPane.WARNING_MESSAGE);            	                       
     	                        	//打开游戏模式选择界面
     	                        	GameModule msf=new GameModule(data,user,questionSet,ip); //传socket到GameModule                      
     	        	        			setVisible(false);
     	                        	dispose(); //关闭登录界面
                 	      }
                 	        else if (indicator == 1)      
         	    					JOptionPane.showMessageDialog(null,"密码错误！","错误",JOptionPane.OK_OPTION);            	       
                 	        else if(indicator==0)//indicator=0
                 	        {
         	    					JOptionPane.showMessageDialog(null,"用户未注册，系统自动注册","提示",JOptionPane.WARNING_MESSAGE);           
         	    				     //服务器自动注册 
         	    					user.indicator=0;
         	    					WelcomeFrame welcomeframe=new WelcomeFrame(user,data,questionSet,ip); //打开欢迎界面
                 	        		welcomeframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 	        		welcomeframe.setVisible(true);	     
                 	        		setVisible(false);
                 	        		dispose();
                 	        }
            		}
            		catch (IOException ex)
            		{
            			timer.stop();
            		 	JOptionPane.showMessageDialog(null,"未检测到该服务器IP\n请检查后重新输入服务器IP","错误",JOptionPane.ERROR_MESSAGE);
            		 	new Connect();
        				setVisible(false);
            			System.err.println(ex);
            		}	            			             	
	            } 
	    	}
	    }
	 }
	 
	 //标签类
	 class noteLabel extends JLabel{
		 public noteLabel(){
			 
		 }
		 public noteLabel(String text){
			 this.setText(text);
			 this.setFont(new Font("",Font.ITALIC,12));
			 this.setForeground(Color.gray);
			 this.setHorizontalAlignment(SwingConstants.RIGHT);
		 }
	 }


}