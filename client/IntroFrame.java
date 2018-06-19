import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.NotSerializableException;
import java.sql.Timestamp;

//介绍游戏流程
public class IntroFrame extends JFrame {
	public static introImage newintro;
	public static dataRecord data = new dataRecord();
	public static Question questionSet;
	public static UserInfo user;
	public static int time=0,i=0;
	boolean newFinish = false;
	public String ip=new String();
	ImageIcon images = new ImageIcon();
	Timer timer = new Timer(1000, new Listener());
	
	protected void processWindowEvent(WindowEvent e) {  
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    		JOptionPane.showMessageDialog(null,"实验进行中，\n请勿退出程序！","错误",JOptionPane.ERROR_MESSAGE);
	    		return;
	    }//阻止默认动作，阻止窗口关闭 
	    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
	} 
	 
	class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			time++;
			// 时间过去20秒后，自动进入下个介绍。
			if (time >= 10) {
				try {
					update();
				} catch (NotSerializableException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public IntroFrame(UserInfo user, dataRecord data, Question questionSet,String ip) {
		this.questionSet = questionSet;
		this.ip=ip;
		IntroFrame.data = data;
		data.IntroTime = new Timestamp(System.currentTimeMillis());
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		setSize(800, 800);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		setTitle("");
		setLayout(new BorderLayout());

		this.user = user; // 接受来自Welcome的socket

		newintro = new introImage();
		newintro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 时间过去10秒后，可以通过点击鼠标进入下个介绍。
				if (time >= 1) {
					try {
						update();
					} catch (NotSerializableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		add(newintro, BorderLayout.CENTER);
		setVisible(true);

	}
	public void update() throws NotSerializableException {
		time = 0;
		if (i < 8) {
			i++;
			//显示图片
			images = new ImageIcon("introImages/" + i + ".jpg");
			Image temp = images.getImage().getScaledInstance(800, 800, images.getImage().SCALE_DEFAULT);
			images = new ImageIcon(temp);
			newintro.setIcon(images);
		} else {
			// 介绍结束，进入试玩界面。
			timer.stop();
			setVisible(false);
			newFinish = true;
			TryFrame tf;
			try {
				tf = new TryFrame(user, data, questionSet,ip);
				tf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				tf.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	//设置按钮
	class introImage extends JButton {
		public introImage() {
			timer.start();
			images = new ImageIcon("introImages/" + i + ".jpg");
			Image temp = images.getImage().getScaledInstance(800, 700, images.getImage().SCALE_DEFAULT);
			images = new ImageIcon(temp);
			setIcon(images);

		}

	}

}
