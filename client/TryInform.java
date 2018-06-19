import java.awt.*;
import javax.swing.*;
import java.sql.Timestamp;

//试玩结束后的提醒

public class TryInform extends JFrame{
	public dataRecord data=new dataRecord();
	
	public TryInform(){

		setSize(200,200);
		JOptionPane.showMessageDialog(null, "试玩结束!下面将进行正式游戏！"); 
		MessagePanel m=new MessagePanel("试玩结束!下面将进行正式游戏！");
		add(m);
	}
}