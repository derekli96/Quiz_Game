import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

//欢迎界面
public class WelcomeFrame extends JFrame{
	Font titleFont=new Font("Times New Roman",Font.BOLD,40);
	Font introFont=new Font("Times New Roman",Font.PLAIN,24);
	
	public UserInfo user;
	public dataRecord data =new dataRecord();
	public Question questionSet;
	public welcomePanel welcome=new welcomePanel();
	public String ip = new String();
	
	protected void processWindowEvent(WindowEvent e) {  
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    	JOptionPane.showMessageDialog(null,"实验进行中，\n请勿退出程序！","错误",JOptionPane.ERROR_MESSAGE);
	    	return;
	    }//阻止默认动作，阻止窗口关闭 
	    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
	} 
	
	public WelcomeFrame (UserInfo user,dataRecord data,Question questionSet,String ip){
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		welcome=new welcomePanel(); 
		this.questionSet=questionSet;
		this.ip=ip;
		setSize(800,600);
		setLayout(new BorderLayout(0,0));
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		setTitle("WelcomeFrame");
		this.data=data;
		data.WelcomeTime=new Timestamp(System.currentTimeMillis());
		this.user=user;  //接收Register传来的user

		//无法显示PK和双引号
		JTextField titleField=new JTextField("欢迎进入数据库知识对战游戏");
		titleField.setPreferredSize(new Dimension(800,150));
		titleField.setEditable(false);
		titleField.setFont(titleFont);
		titleField.setHorizontalAlignment(SwingConstants.CENTER);
		titleField.setOpaque(true);

		//欢迎内容
		JTextArea introArea=new JTextArea();
		introArea.setText("        在游戏中，您将与一名随机匹配的对手就上个模块所学习过的数据库"
				+ "知识进行答题比拼。在题目规定时间内作答，答对可得到题目对应的奖金分数，"+"答错则停止答题。"
						+ "游戏分为一个试玩环节和两个正式环节。"
						+ "试玩环节无对手且不计分，两个正式环节的累计得分作为您的最终得分。根据最终得分判定您的胜负。"
						+"\n\n"+"        在游戏开始前，我们将对游戏界面中各版块及其功能进行介绍。");
		introArea.setFont(introFont);
		introArea.setMargin(new Insets(20,10,10,10));
		introArea.setEditable(false);
		introArea.setLineWrap(true); //自动换行
		introArea.setOpaque(true);
		
		
		final beginIntro introButton=new beginIntro();
		introButton.setVisible(false);
		introButton.setPreferredSize(new Dimension(80,118));
		introButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newframe(); //开启游戏介绍界面
			}
		});

		add(welcome,BorderLayout.CENTER);
		add(introButton,BorderLayout.SOUTH);
		
	
		final Timer delayTimer=new Timer();
		TimerTask timerTask=new TimerTask()
		{
			public void run()
			{
				introButton.setVisible(true);
				delayTimer.cancel();
			}
		};
		
		delayTimer.schedule(timerTask, 3000);
		
	}
	
	public void newframe() {
		setVisible(false);
		 //把user和data传给介绍界面
		IntroFrame introframe=new IntroFrame(user,data,questionSet,ip);
		introframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		introframe.setVisible(true); 
	}
	
	//欢迎介绍图片
	class welcomePanel extends JPanel implements ImageObserver{
		public ImageIcon images=new ImageIcon();
		public Image temp;
		public welcomePanel(){
			setSize(800,600);	
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			images=new ImageIcon();
			images=new ImageIcon("interface/欢迎界面.png");
			Image temp=images.getImage().getScaledInstance(800,600,images.getImage().SCALE_DEFAULT);  
			images=new ImageIcon(temp);
			g.drawImage(temp,0,0,this);
		}
	}
	
	//开始介绍按钮
	class beginIntro extends JButton {
		ImageIcon images=new ImageIcon();
		public beginIntro(){
			images=new ImageIcon("interface/开始介绍_button.png");
			Image temp=images.getImage().getScaledInstance(800,118,images.getImage().SCALE_DEFAULT);  
			images=new ImageIcon(temp);
			setIcon(images);
			
		}

		}

}

