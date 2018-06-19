import java.awt.*;
import javax.swing.*;

//在窗口内显示字符串

public class MessagePanel extends JPanel{
	private int xCoordinate=20;
	private int yCoordinate=20;
	private String message="";

	public MessagePanel(){
	}
	

	public MessagePanel(String message){
		this.message=message;
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(new Font("",Font.PLAIN,15));
		FontMetrics fm = g.getFontMetrics();
		//在窗口内居中显示字符串
		int stringWidth = fm.stringWidth(message);
		int stringAscent = fm.getAscent();
		
		xCoordinate = getWidth()/2 - stringWidth/2;
		yCoordinate = getHeight()/2 +stringAscent/2;

		g.drawString(message,xCoordinate,yCoordinate-10);
	}
	
	public void setMessage(String newMessage){
		this.message=newMessage;
	}
	
	public String getMessage(){
		return message;
	}
	
}