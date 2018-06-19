import java.awt.*;
import java.io.*;
import java.util.Random;
import javax.swing.*;

//显示对手信息(和队友信息2V2)
public class InfoPanel extends MessagePanel{
	public int mode;
	public UserInfo user;
    public DataInputStream fromServer;
    public int length;
    public int rivalScore=0; //对手得分
    public int partnerScore=0; //队友得分
    public String msg=new String();
	public MessagePanel m1 = new MessagePanel();
	public MessagePanel m2 = new MessagePanel();
    public int[] systemScore=new int[2];
    public Font infofont=new Font("",Font.PLAIN,12);//设置字体
	
	public InfoPanel(int mode,UserInfo user){
		this.mode=mode;
		this.user=user;
		this.setFont(infofont);
		try {
			fromServer = new DataInputStream(user.socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		//初始化得分信息
		if(mode==0) {//试玩
			setMessage("试玩阶段，无对手信息！");
		}
		else if(mode==1){//人机对战
			setMessage("暂无系统得分信息");
			systemScore[0]=100;
			systemScore[1]=100;
		
		}
		else if(mode==2) {//1V1
			setMessage("您的对手得分为："+0);
		}
		else if(mode==3) {//2V2
			m1.setMessage("对方阵容得分为："+0);
			m2.setMessage("您的队友得分为："+0);
			setLayout(new GridLayout(2,1));
			add(m1);
			add(m2);		
		}
		setPreferredSize(new Dimension(200,200));
		setBorder(BorderFactory.createTitledBorder("Information"));
		
	}
	
	//更新得分信息
	public void update(int questionCount,int score,int round){
		if(mode==1){
			if(questionCount==2||questionCount==5||questionCount==8||questionCount==11){
				long t=System.currentTimeMillis();
				Random r=new Random(t);	//设置当前时间为随机数种子			
				int i=r.nextInt(2); //生成0或1的随机数
				//机器对手得分规则
				if(i==0){
					setMessage("你的对手本轮得分比你高！");
					if(score==200) systemScore[round-1]=500;
					else if(score==2000) systemScore[round-1]=4000;
					else if(score==64000) systemScore[round-1]=125000;
					else if(score==500000) systemScore[round-1]=1000000;
					repaint();
				}
				else{
					setMessage("你的对手本轮得分比你低！");
					if(score==200) systemScore[round-1]=100;
					else if(score==2000) systemScore[round-1]=1000;
					else if(score==64000) systemScore[round-1]=16000;
					else if(score==500000) systemScore[round-1]=250000;
					repaint();
				}
			}
		}
		if(mode==2) {
			setMessage("您的对手得分为："+rivalScore);//显示对手得分
			repaint();
		}
		if(mode==3) {
			m1.setMessage("对方阵容得分为："+rivalScore);//显示对方阵容得分
			m2.setMessage("您的队友得分为："+partnerScore);//显示队友得分
			m1.repaint();
			m2.repaint();
		}

	}
}