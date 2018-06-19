import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;
//等待其他用户
public class WaitingFrame extends JFrame {
	public JTextField titleField = new JTextField("请等待其他用户结束游戏……");
	Color bj = new Color(238, 238, 238);
	Font mainFont = new Font("Times New Roman", Font.PLAIN, 20);
	
	protected void processWindowEvent(WindowEvent e) {  
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    	JOptionPane.showMessageDialog(null,"实验进行中，\n请勿退出程序！","错误",JOptionPane.ERROR_MESSAGE);
	    	return;
	    }//阻止默认动作，阻止窗口关闭 
	    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
	} 

	public WaitingFrame() {
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setSize(300, 200);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		titleField.setEditable(false);
		titleField.setBackground(bj);
		titleField.setHorizontalAlignment(SwingConstants.CENTER);
		titleField.setFont(mainFont);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(titleField,BorderLayout.CENTER);
	}
}
