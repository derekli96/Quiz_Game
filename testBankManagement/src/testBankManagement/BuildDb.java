package tikuguanli;

import java.sql.*;

//运行此程序前请将password中储存的密码修改为Mysql实际根用户的密码


public class BuildDb {
	public static void main(String[] args)
	{
		String driver = "com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/mysql";
        String user = "root"; 
        String password = "sunshine";
        
        try 
        {
         Class.forName(driver);
         Connection conn = DriverManager.getConnection(url, user, password);
       
         

         Statement statement = conn.createStatement();
         String sql = "CREATE DATABASE EXPERIMENT";
         statement.executeUpdate(sql);
         statement.close();
         conn.close();
         
         Connection conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/test4", user, password);
         
         Statement stat = conn2.createStatement();
         
         String sql3 = "CREATE TABLE player (idplayer VARCHAR(30) PRIMARY KEY, password VARCHAR(16), score VARCHAR(10), 1V1 VARCHAR(10), 2V2 VARCHAR(10))ENGINE = InnoDB DEFAULT CHARSET=utf8";         
         stat.executeUpdate(sql3);
         
         String sql4 = "CREATE TABLE problemset (idproblem VARCHAR(10) PRIMARY KEY, statement VARCHAR(300),A VARCHAR(100),B VARCHAR(100),C VARCHAR(100),D VARCHAR(100),answer VARCHAR(10),picture VARCHAR(20400))ENGINE = InnoDB DEFAULT CHARSET=utf8";
         stat.executeUpdate(sql4);
         
         String sql5 = "CREATE TABLE experiment (idplayer VARCHAR(30), welcomeTime VARCHAR(45), introTime VARCHAR(45),tryTime VARCHAR(45),mode VARCHAR(10),startTime VARCHAR(600), round1 VARCHAR(10), round2 VARCHAR(10), clickA VARCHAR(30), clickB VARCHAR(30), clickC VARCHAR(30), clickD VARCHAR(30), useHalf VARCHAR(10), useHelp VARCHAR(10), playNext VARCHAR(10), totalScore VARCHAR(10), PRIMARY KEY(idplayer, startTime))ENGINE = InnoDB DEFAULT CHARSET=utf8";
         stat.executeUpdate(sql5);

         String sql6 = "CREATE TABLE talk (idplayer VARCHAR(30),dateTime VARCHAR(45), content VARCHAR(1000), PRIMARY KEY(idplayer,dateTime))ENGINE = InnoDB DEFAULT CHARSET=utf8";
         stat.executeUpdate(sql6);
         
         stat.close();
         conn2.close();
         
         System.out.println("数据库搭建成功");
        } 

        catch(ClassNotFoundException e) {
         System.out.println("Sorry,can`t find the Driver!"); 
         e.printStackTrace();
        } catch(SQLException e) {e.printStackTrace();
        } catch(Exception e) {e.printStackTrace();}
	}
}
