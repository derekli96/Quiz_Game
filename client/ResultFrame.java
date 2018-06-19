import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowEvent;

//显示游戏结果
public class ResultFrame extends JFrame{
	public dataRecord data;
	public UserInfo user;
	Font titleFont=new Font("Times New Roman",Font.BOLD,40);
	public static Font midFont=new Font("Times New Roman",Font.PLAIN,24);
	
	protected void processWindowEvent(WindowEvent e) {  
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
	    	JOptionPane.showMessageDialog(null,"该窗口将自动关闭\n请勿强制退出！","错误",JOptionPane.ERROR_MESSAGE);
	    	return;
	    }//阻止默认动作，阻止窗口关闭 
	    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
	} 
	
	//人机对战和1V1时的结果显示
	public ResultFrame(boolean haswon,UserInfo user,dataRecord data,int teamRound1,int teamRound2,int rivalScore){	
		this.data=data;
		this.user=user;
		setSize(600,450);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		int teamTotal=teamRound1+teamRound2;
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		setTitle("Result frame");
		setLayout(new BorderLayout(0,0));
		
		JLabel titleLabel=new JLabel();
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(titleFont);
		if(haswon) titleLabel.setText("恭喜您获得了胜利！");
		else titleLabel.setText("很遗憾，您输了！");
		
		
		JPanel textPanel=new JPanel();
		JPanel dataPanel=new JPanel();
		JPanel opponentPanel=new JPanel();
		JPanel midPanel=new JPanel();
		
		midPanel.setLayout(new GridLayout(11,2));
		
		//textPanel.setLayout(new GridLayout(3,1));
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel(""));
		
		midPanel.add(new centerLabel("您第一轮的得分："));
		midPanel.add(new centerLabel(""+data.round1Score));
		midPanel.add(new centerLabel("您第二轮的得分："));
		midPanel.add(new centerLabel(""+data.round2Score));
		midPanel.add(new centerLabel("      您的总得分："));
		midPanel.add(new centerLabel(""+data.totalScore));
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel(""));
		
		midPanel.add(new centerLabel("您的队伍第一轮的得分："));
		midPanel.add(new centerLabel(""+teamRound1));
		midPanel.add(new centerLabel("您的队伍第二轮的得分："));
		midPanel.add(new centerLabel(""+teamRound2));
		midPanel.add(new centerLabel("您的队伍的总得分："));
		midPanel.add(new centerLabel(""+teamTotal));
		
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel("对手阵营总得分： "));
		midPanel.add(new centerLabel(""+rivalScore));
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel(""));
		
		add(titleLabel,BorderLayout.NORTH);
		add(midPanel,BorderLayout.CENTER);
		add(opponentPanel,BorderLayout.SOUTH);			
	}
	
	//2V2时的结果显示
	public ResultFrame(boolean haswon,UserInfo user,dataRecord data,int rivalScore){
		this.data=data;
		this.user=user;
		setSize(600,350);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示

		setTitle("Result frame");
		setLayout(new BorderLayout(0,0));
		
		JLabel titleLabel=new JLabel();
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(titleFont);
		titleLabel.setText("niyingle!");
		if(haswon) titleLabel.setText("恭喜您获得了胜利！");
		else titleLabel.setText("很遗憾，您输了！");
		
		
		JPanel textPanel=new JPanel();
		JPanel dataPanel=new JPanel();
		JPanel opponentPanel=new JPanel();
		JPanel midPanel=new JPanel();
		
		midPanel.setLayout(new GridLayout(7,2));
		
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel(""));
		
		midPanel.add(new centerLabel("您第一轮的得分："));
		midPanel.add(new centerLabel(""+data.round1Score));
		midPanel.add(new centerLabel("您第二轮的得分："));
		midPanel.add(new centerLabel(""+data.round2Score));
		midPanel.add(new centerLabel("      您的总得分："));
		midPanel.add(new centerLabel(""+data.totalScore));
		
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel("您的对手总得分： "));
		midPanel.add(new centerLabel(""+rivalScore));
		midPanel.add(new centerLabel(""));
		midPanel.add(new centerLabel(""));
		
		add(titleLabel,BorderLayout.NORTH);
		add(midPanel,BorderLayout.CENTER);
		add(opponentPanel,BorderLayout.SOUTH);		
		
	}

}

class centerLabel extends JLabel{
	public centerLabel(String text){
		setHorizontalAlignment(SwingConstants.CENTER);
		setFont(ResultFrame.midFont);
		setText(text);
	}
}
