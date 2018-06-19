import java.sql.Timestamp;
import java.util.Date;
import java.io.Serializable;

//接收客户端发来的实验数据

public class dataRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	public String username;
	public Timestamp WelcomeTime=  Timestamp.valueOf("1999-12-31 23:59:59"); //欢迎界面进入时刻
	 public Timestamp IntroTime= Timestamp.valueOf("1999-12-31 23:59:59"); ; //介绍界面进入时刻
	 public Timestamp TryTime= Timestamp.valueOf("1999-12-31 23:59:59"); ; //试玩界面进入时刻
	 public int mode; //选择哪个模式
	 public int round1Score; //第一轮成绩
	 public int round2Score; //第二轮成绩
	 public Timestamp[] questionTime = new Timestamp[24];
	 public int[] clickA = new int[24]; //选项点击次数
	 public int[] clickB = new int[24];
	 public int[] clickC = new int[24];
	 public int[] clickD = new int[24];
	 public boolean useHalf=false; //是否点击按钮
	 public boolean useHelp=false;
	 public boolean playNext=false;
	 public int totalScore;
	 public String[] chat = new String[1000]; //存聊天内容
	 
	 
     public dataRecord(){
    	 	for(int k=0;k<24;k++) 
    	 	{
    	   		questionTime[k]=Timestamp.valueOf("1999-12-31 23:59:59");
    	   	}
    	 	for(int i=0;i<24;i++)
    	 	{
    	 		clickA[i]=0;	
    	 		clickB[i]=0;
    	 		clickC[i]=0;
    	 		clickD[i]=0;
    	 	}
    	 	for(int i=0;i<1000;i++)
    	 	{
    	 		chat[i]="";
    	 	}
     }
     
     public String StringRecord(dataRecord data) //dataRecord转化成String类型
     {
    	 	String string_record = "#"+data.username+"#"+data.WelcomeTime.toString()+"#"+data.IntroTime.toString()+"#"
    	 			+data.TryTime.toString()+"#"+data.mode+"#"+data.round1Score+"#"+data.round2Score+"#"; 
    		//String string_record = "#"+data.username+"#"+data.mode+"#"+data.round1Score+"#"+data.round2Score+"#";
    	 	for(int k=0;k<23;k++) {
    	 		string_record+=questionTime[k].toString()+"$";
    	 	}
	 		string_record+=questionTime[23].toString();
    	 	string_record+="#";
    	 	for(int k=0;k<24;k++) {
    	 		string_record+=clickA[k];
    	 	}
    	 	string_record+="#";
    	 	for(int k=0;k<24;k++) {
    	 		string_record+=clickB[k];
    	 	}
    	 	string_record+="#";
    	 	for(int k=0;k<24;k++) {
    	 		string_record+=clickC[k];
    	 	}
    	 	string_record+="#";
    	 	for(int k=0;k<24;k++) {
    	 		string_record+=clickD[k];
    	 	}
    	 	string_record+="#";
    	 	if (useHalf)
    	 		string_record+="1#";
    	 	else
    	 		string_record+="0#";
    	 	if (useHelp)
    	 		string_record+="1#";
    	 	else
    	 		string_record+="0#";
    		if (playNext)
    	 		string_record+="1#";
    	 	else
    	 		string_record+="0#";
    		string_record+=totalScore;
		return string_record;   	 	
     }
    
     
     
 }