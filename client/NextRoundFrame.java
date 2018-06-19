import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

//询问是否再来一局
public class NextRoundFrame extends JFrame{
	public MessagePanel next;
	public NextButtonPanel buttons;
	
	 protected void processWindowEvent(WindowEvent e) {  
		    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
		    	JOptionPane.showMessageDialog(null,"结束实验请点击“No”按钮","错误",JOptionPane.ERROR_MESSAGE);
		    	return;
		    }//阻止默认动作，阻止窗口关闭 
		    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
		} 
	
	public NextRoundFrame(){
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setSize(300,200);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		setLayout(new GridLayout(2,1));
		next=new MessagePanel("你希望进行下一局游戏吗？");
        buttons=new NextButtonPanel();
		add(next);
		add(buttons);
	}
	
	class NextButtonPanel extends JPanel{
		public NextButton Yes;
		public NextButton No;
		public NextButtonPanel(){
			setLayout(new GridLayout(1,2));
			Yes =new NextButton(true);
			No=new NextButton(false);
			add(Yes);
			add(No);
		}
	}
	
	class NextButton extends JButton{
		public NextButton(boolean type){
			if(type==true)
			    this.setText("Yes");
			else
				this.setText("No");
	}
	}
}
