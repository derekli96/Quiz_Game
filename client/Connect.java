import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

//IP接口，输入IP地址连接服务器

public class Connect extends JFrame {
	public JTextField IPtf=new JTextField(16);
	public String ip="";

	JButton ConnectButton = new JButton("连接");
	JButton exitButton = new JButton("退出");
	JLabel IPLabel = new JLabel("服务器IP：");
	
	public Connect(){
		super("IP地址输入");
        this.setSize(300,180);
		JPanel jp1 = new JPanel(new FlowLayout());
		jp1.add(IPLabel);
		jp1.add(IPtf);
		
		JPanel jp2 = new JPanel();
		jp2.add(ConnectButton);
		jp2.add(exitButton);
        
        JLabel noteLabel=new JLabel("请输入服务器IP     ");
        noteLabel.setFont(new Font("",Font.ITALIC,12));
        noteLabel.setForeground(Color.gray);
        noteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
		ConnectButton.addActionListener(new buttonListener());
		exitButton.addActionListener(new buttonListener());
		this.setLayout(new GridLayout(5, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		this.add(new JLabel(""));
		this.add(jp1);
        this.add(noteLabel);
		this.add(jp2);
        this.add(new JLabel(""));
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
		setVisible(true);
    }
	
	 public static void main(String[] args) 
	 {  
		new Connect();
	 } 
	 
	 class buttonListener implements ActionListener {
	    	public void actionPerformed(ActionEvent e) {  
	  
	            if(e.getActionCommand()=="退出")  //退出按钮
	            {  
	                System.exit(0);  
	            }
	            if(e.getActionCommand()=="连接") 
	            {   
	            	
	            	String ip = IPtf.getText().trim(); //打开登录界面，连接IP
	            	setVisible(false);
                    Login lgin=new Login(ip);
	    	}
	    }
	 }
}
