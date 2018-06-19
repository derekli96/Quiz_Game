import java.sql.Timestamp;
import java.io.Serializable;

//采集实验数据并传递给服务器端

public class dataRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	public String username="";
	public Timestamp WelcomeTime=  Timestamp.valueOf("1999-12-31 23:59:59"); //欢迎界面进入时刻，默认为"1999-12-31 23:59:59"
	 public Timestamp IntroTime= Timestamp.valueOf("1999-12-31 23:59:59"); ; //介绍界面进入时刻
	 public Timestamp TryTime= Timestamp.valueOf("1999-12-31 23:59:59"); ; //试玩界面进入时刻
	 public int mode; //选择哪个模式
	 public int round1Score=0; //第一轮成绩
	 public int round2Score=0; //第二轮成绩
	 public Timestamp[] questionTime = new Timestamp[24];
	 public int[] clickA = new int[24]; //选项点击次数
	 public int[] clickB = new int[24];
	 public int[] clickC = new int[24];
	 public int[] clickD = new int[24];
	 public boolean useHalf=false; //是否点击按钮
	 public boolean useHelp=false;
	 public boolean playNext=false;
	 public int totalScore=0;
	 public String chat="chatting content"; //存聊天内容
	 
	 //初始化
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
    	 	
     }
     
     //将dataRecord类中全部变量连成一个字符串并返回
     public String StringRecord(dataRecord data) //dataRecord转化成String类型
     {
    	 	String string_record = "04#"+data.username+"#"+data.WelcomeTime.toString()+"#"+data.IntroTime.toString()+"#"
    	 			+data.TryTime.toString()+"#"+data.mode+"#"+data.round1Score+"#"+data.round2Score+"#"; //打上04标签
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
    		string_record+=totalScore+"#"+chat+"#";
		return string_record;   	 	
     }    
     
 }