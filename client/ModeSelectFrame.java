import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;

import javax.swing.*;

//选择游戏模式

public class ModeSelectFrame extends JFrame{
	public mode_1 b1;
    public mode_2 b2;
    public mode_3 b3;
    public mode_4 b4;
	
    protected void processWindowEvent(WindowEvent e) {  
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    	JOptionPane.showMessageDialog(null,"实验进行中，\n请勿退出程序！","错误",JOptionPane.ERROR_MESSAGE);
	    	return;
	    }//阻止默认动作，阻止窗口关闭 
	    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
	} 
    
	public ModeSelectFrame(int indicator){
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
         setSize(400,300);
         int windowWidth = getWidth(); // 获得窗口宽
 		 int windowHeight = getHeight(); // 获得窗口高
 		 Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
 		 Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
 		 int screenWidth = screenSize.width; // 获取屏幕的宽
 		 int screenHeight = screenSize.height; // 获取屏幕的高
 		 setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

        setLayout(new GridLayout(4,1));
         selectPanel s = new selectPanel();
         b1 = new mode_1();
         b2 = new mode_2();
         b3 = new mode_3();
         b4 = new mode_4();
         add(s);
         add(b1);
         add(b2);
         
         if(indicator==0) {
        	  add(b4);
         }
         else{
        	 add(b3);
         }
         
	}
	
	class mode_1 extends JButton { //人机对战按钮
		ImageIcon images=new ImageIcon();
		public mode_1(){
			images=new ImageIcon("interface/1V1人机.jpg");
			Image temp=images.getImage().getScaledInstance(800,150,images.getImage().SCALE_DEFAULT);  
			images=new ImageIcon(temp);
			setIcon(images);
			
		}

		}
	
	class mode_2 extends JButton { //1V1人人对战按钮
		ImageIcon images=new ImageIcon();
		public mode_2(){
			images=new ImageIcon("interface/1v1双人.jpg");
			Image temp=images.getImage().getScaledInstance(800,150,images.getImage().SCALE_DEFAULT);  
			images=new ImageIcon(temp);
			setIcon(images);
			
		}

		}
	
	class mode_3 extends JButton { //2V2按钮
		ImageIcon images=new ImageIcon();
		public mode_3(){
			images=new ImageIcon("interface/2v2.jpg");
			Image temp=images.getImage().getScaledInstance(800,150,images.getImage().SCALE_DEFAULT);  
			images=new ImageIcon(temp);
			setIcon(images);
			
		}

		}
	
	class mode_4 extends JButton {//空白按钮
		ImageIcon images=new ImageIcon();
		public mode_4(){
			images=new ImageIcon("interface/NULL.jpg");
			Image temp=images.getImage().getScaledInstance(400,75,images.getImage().SCALE_DEFAULT);  
			images=new ImageIcon(temp);
			setIcon(images);
			
		}

		}
	
	class selectPanel extends JPanel implements ImageObserver{
		public ImageIcon images=new ImageIcon();
		public Image temp;
		public selectPanel(){
			setSize(400,300);	
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			images=new ImageIcon();
			images=new ImageIcon("interface/选择游戏模式.jpg");
			Image temp=images.getImage().getScaledInstance(400,300,images.getImage().SCALE_DEFAULT);  
			images=new ImageIcon(temp);
			g.drawImage(temp,0,0,this);
		}
	}
}