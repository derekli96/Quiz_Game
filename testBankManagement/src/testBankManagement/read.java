//本段代码参考了花粉俱乐部中的开源代码，具体链接如下：java读取本地txt文件并插入数据库
//http://cn.ui.vmall.com/thread-5128392-1-1.html
//(出处:花粉俱乐部)
//请注意在运行程序前根据Mysql根用户的实际密码修改pwd中储存的密码


package tikuguanli;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;

public class read {
	
	 //读取文本的操作
    public static String[] writeToDat(String path) {
              File file = new File(path);
              List<String> list = new ArrayList<String>();
              String []strings = null;
              try {
               BufferedReader bw = new BufferedReader(new FileReader(file));
               String line = null;
               //因为不知道有几行数据，所以先存入list集合中
               while((line = bw.readLine()) != null){
                list.add(line);
               }
               bw.close();
              } catch (IOException e) {
               e.printStackTrace();
              }
              //确定数组长度
              strings = new String[list.size()];
              for(int i=0;i<list.size();i++){
               String s = (String) list.get(i);
               strings[i] = s;
              }
              return strings;
             }
    
    
    //连接数据库
    
    public static Connection getConnection(){
            String data ="Experiment";
            String user ="root";
            String pwd = "sunshine";
            Connection conn = null;
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ data +"?characterEncoding=UTF-8",user , pwd);
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return conn;
    }
    
    //插入数据库
    public static boolean insertInto(String []str){
            try {
            		String field = "idProblem, statement, A, B, C, D, answer, picture";
                    Connection conn = getConnection();
                    conn.setAutoCommit(false);
          
                    String sql = "INSERT INTO problemset" + "("+ field +") VALUES (?,?,?,?,?,?,?,?);";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    for (int i = 0; i < str.length; i++) {
                            pstmt.setString(1, str[i]);
                            i++;
                            pstmt.setString(2, str[i]);
                            i++;
                            pstmt.setString(3, str[i]);
                            i++;
                            pstmt.setString(4, str[i]);
                            i++;
                            pstmt.setString(5, str[i]);
                            i++;
                            pstmt.setString(6, str[i]);
                            i++;
                            pstmt.setString(7, str[i]);
                            i++;
                            pstmt.setString(8, str[i]);
                            pstmt.executeUpdate();
                    }
                    conn.commit();
                    return true;
            } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
            }
            
    }

    
    public  static void main(String[] args){
            String []str = null;
            boolean flage = true;
            
            System.out.println("请输入目标文件绝对路径:");
            Scanner sc = new Scanner(System.in);
            String path = sc.next();
            while(flage){
                    if (path!=null&&!path.equals("")) {
                            flage = false;
                            str = writeToDat(path);
                    }else {
                            System.out.println("输入不能为空");
                    }
            }
            
            
            boolean b = insertInto(str);
            
            if(b){
                    System.out.println("插入成功!");
            }else {
                    System.out.println("插入失败!");
            }
            
    }

	
}
