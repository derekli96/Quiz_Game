//本段代码主要参考了CSDN博客频道中博主nickwong&nickwang@ACM的开源代码，具体链接如下：
//http://blog.csdn.net/nickwong_/article/details/51502969
//在使用前请注意password变量中存储的是数据库根用户的密码，请根据Mysql的实际密码来进行修改
//在使用本程序前请注意修改每次调用DOWriteTxt函数时文件的名字和绝对路径以输出文件到正确的位置

package printReport;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

public class Report {
	 public void DOWriteTxt(String file, String txt) {
	  try {
	   FileOutputStream os = new FileOutputStream(new File(file), true);
	   os.write((txt + "\n").getBytes());
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	 }
	 
	public static void main(String[] args) {
		String driver = "com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/EXPERIMENT";
        String user = "root"; 
        String password = "sunshine";
        
        try 
        {
         Class.forName(driver);
         Connection conn = DriverManager.getConnection(url, user, password);
         
         

         Statement statement = conn.createStatement();
         String sql = "select * from experiment";
         ResultSet rs = statement.executeQuery(sql);
         
         String experiment = new String();
         String s= new String();
         String startTime= new String();
         
         while(rs.next()) {
        	 experiment=experiment+rs.getString("idplayer")+"\t"+rs.getString("welcomeTime") + "\t" +rs.getString("introTime")+"\t"+rs.getString("tryTime")+"\t"+rs.getString("mode")+"\t"+rs.getString("round1")+"\t"+ rs.getString("round2")+"\t"+rs.getString("clickA")+"\t"+rs.getString("clickB")+"\t"+rs.getString("clickC")+"\t"+rs.getString("clickD")+"\t"+rs.getString("useHalf")+"\t"+rs.getString("useHelp")+"\t"+rs.getString("playNext")+"\t"+rs.getString("totalScore")+"\r";
        	 s=rs.getString("startTime");
        	 String[] array=s.split("\\$");
        	 startTime = startTime+rs.getString("idplayer")+"\t"+array[0]+"\t"+array[1]+"\t"+array[2]+"\t"+array[3]+"\t"+array[4]+"\t"+array[5]+"\t"+array[6]+"\t"+array[7]+"\t"+array[8]+"\t"+array[9]+"\t"+array[10]+"\t"+array[11]+"\r";
         }
 		new Report().DOWriteTxt("/Users/yuanchuqiao/Downloads/experiment.txt", experiment);
 		new Report().DOWriteTxt("/Users/yuanchuqiao/Downloads/startTime.txt", startTime);
         rs.close();
         
         Statement statement2 = conn.createStatement();
         String sql2 = "select * from player";
         ResultSet rs2 = statement2.executeQuery(sql2);
         
         String player = new String();
         String playerinfo= new String();
         
         while(rs2.next()) {
        	 playerinfo = playerinfo+rs2.getString("idplayer")+"\t"+rs2.getString("1V1")+"\t"+rs2.getString("2V2")+"\t"+rs2.getString("score")+"\r";
         }
 		new Report().DOWriteTxt("/Users/yuanchuqiao/Downloads/player.txt", playerinfo);
         rs2.close();
         
         Statement statement3 = conn.createStatement();
         String sql3 = "select * from talk";
         ResultSet rs3 = statement3.executeQuery(sql3);
         
         String talk = new String();
         
         while(rs3.next()) {
        	 talk = talk +rs3.getString("idplayer")+"\t"+rs3.getString("dateTime")+"\t"+rs3.getString("content")+"\r";
         }
 		new Report().DOWriteTxt("/Users/yuanchuqiao/Downloads/talk.txt", talk);
         rs3.close();
         
         
         conn.close();
        } 

        catch(ClassNotFoundException e) {
         System.out.println("Sorry,can`t find the Driver!"); 
         e.printStackTrace();
        } catch(SQLException e) {e.printStackTrace();
        } catch(Exception e) {e.printStackTrace();}
		
    }
}
