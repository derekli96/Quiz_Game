//聊天窗口
//聊天功能参考了博主Sunshyfangtian的开源代码“一个简单的UDP聊天室”，具体链接如下：http://sunshyfangtian.iteye.com/blog/642334
//聊天查重功能参考了何长春博主的开源代码”java两个字符串的相似度”，具体链接如下：http://www.cnblogs.com/hnhcc39/archive/2012/09/29/2708119.html

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ConversationPanel extends JPanel{
	
	public Map QuestionInfo;
	//public Question questionSet;
	//四个选项的内容
	public String A=new String(); 
	public String B=new String();
	public String C=new String();
	public String D=new String();
 	public DataOutputStream toServer;
	public UserInfo user;
    public dataRecord data;
    public int mode;
    public String question=new String(); //题干内容
   	JTextArea allmsg = new JTextArea(); //聊天显示
   	JTextArea promptmsg = new JTextArea("聊天功能已禁用"); //提示信息
   	JButton send= new JButton("发送");  //发送聊天信息
   	JButton forbid= new JButton("禁用"); //禁用聊天功能
   	JButton recover= new JButton("聊天功能恢复"); //恢复聊天功能
   	JTextField chatmsg=new JTextField("请输入聊天内容"); 
    JScrollPane js = new JScrollPane(allmsg);
    Font promptFont=new Font("宋体",Font.BOLD,36); //设置字体    
    
    //将日期转化为字符串
    public  String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	} 

    //比较两个字符串，返回相同字符的数量
    public int compare(String str, String target)
    {
        int d[][];             
        int n = str.length();
        int m = target.length();
        int i,j, temp;               
        char ch1, ch2;              
        if (n == 0) { return m; }
        if (m == 0) { return n; }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++)
        {           System.out.println("chu shi hua di yi lie");            // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++)
        {            System.out.println("chu shi hua di yi hang");           // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++)
        {            System.out.println("bian li 1");           // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++)
            {   System.out.println("bian li 2");
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2)                
                    temp = 0;
                else                
                    temp = 1;                
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    public int min(int one, int two, int three) //比较3个数的大小
    {
    	System.out.println("min");    
    	return (one = one < two ? one : two) < three ? one : three;
        
    }
   
    //获取两字符串的相似度   
    public float Similarity(String str, String target)
    {
    	    System.out.println("similarity1");
    	    int same=Math.max(str.length(), target.length())-compare(str,target);
    	    System.out.println("similarity2");
        return (float) same/target.length();
    }
    
	public ConversationPanel(UserInfo user,dataRecord data,int mode,Map QuestionInfo) throws NotSerializableException
	{
		this.user=user; 
		this.data=data;
		this.mode=mode;
		this.question=QuestionInfo.get("Question").toString();
		this.A=QuestionInfo.get("options[0]").toString();
		this.B=QuestionInfo.get("options[1]").toString();
		this.C=QuestionInfo.get("options[2]").toString();
		this.D=QuestionInfo.get("options[3]").toString();
		setSize(400,600);
		//setBorder(BorderFactory.createTitledBorder("Conversation"));
		setLayout(null);
		
		add(js);
		add(chatmsg);
		add(send);
		add(forbid);
		add(promptmsg);
		add(recover);
		
		allmsg.setEditable(false);
		promptmsg.setEditable(false);
		promptmsg.setVisible(false);
		promptmsg.setFont(promptFont);
		recover.setVisible(false);
		
		js.setBounds(5, 5, 380, 400);
		chatmsg.setBounds(3,420,305,35);
		send.setBounds(310,420,40,40);
	    forbid.setBounds(350,420,40,40);
	    promptmsg.setBounds(5, 5, 380, 400);
	    recover.setBounds(5,420,330,40);	    
	    
		chatmsg.addActionListener(new listen());
		send.addActionListener(new listen()); 
		forbid.addActionListener(new listen()); 
    		recover.addActionListener(new listen()); 
    
   		try 
   		{    
   			toServer = new DataOutputStream(user.socket.getOutputStream());   //数据输出流
   		} 
   		catch (IOException e) 
   		{
   			JOptionPane.showMessageDialog(null, "系统异常","错误",JOptionPane.OK_CANCEL_OPTION);
   		}

    }
	
	//更新题目内容
	public void update(Map QuestionInfo) {
		this.question=QuestionInfo.get("Question").toString();
		this.A=QuestionInfo.get("options[0]").toString();
		this.B=QuestionInfo.get("options[1]").toString();
		this.C=QuestionInfo.get("options[2]").toString();
		this.D=QuestionInfo.get("options[3]").toString();
	}

	//事件触发器
    class listen implements ActionListener 
    {   	
    		public void actionPerformed(ActionEvent e) 
    		{
    			if(e.getSource()==send||e.getSource()==chatmsg)
    			{
    				String msg = chatmsg.getText().trim(); //获取聊天文字
    				if("".equals(msg)) {
    					JOptionPane.showMessageDialog(null,"发送信息不能为空!","错误",JOptionPane.OK_OPTION); //提示错误
    				}
    				else 
    				{  
    					chatmsg.setText(""); //清空发送框
    					data.chat+=DateToStr(new Date())+" : "+msg+"$"; //把聊天数据存到dataRecord
    					try {
    						if(mode==3)
						{
    							/*
    							 *2V2时进行聊天内容查重
    							 *如果和题干重合率大于等于0.6或者与选项完全一致，则查重不通过，显示非法聊天
    							 */
							if(Similarity(msg,question)>=0.6 || msg.equals(A) || msg.equals(B) || msg.equals(C) || msg.equals(D)) 
							{
								msg="****非法聊天****";
			    					JOptionPane.showMessageDialog(null,"聊天内容与题目重合率太高!","错误",JOptionPane.OK_OPTION);//错误提示
							}													
							}
    						toServer.writeUTF("03#"+mode+"#"+DateToStr(new Date())+"#"+user.username+"#"+msg); //将聊天内容传给服务器
						} catch (IOException e1) {
							e1.printStackTrace();
						}
    				}
    			}
    			if(e.getSource()==forbid)
    			{
    				js.setVisible(false); //禁用聊天功能
    				send.setVisible(false);
    				chatmsg.setVisible(false);
    				forbid.setVisible(false);
    				promptmsg.setVisible(true);
    				recover.setVisible(true);
    			}
    			if(e.getSource()==recover) {
    				js.setVisible(true); //恢复聊天功能
    				send.setVisible(true);
    				chatmsg.setVisible(true);
    				forbid.setVisible(true);
    				promptmsg.setVisible(false);
    				recover.setVisible(false);
    			}
    		} 
    }	
    
} 