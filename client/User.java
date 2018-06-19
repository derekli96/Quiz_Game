import java.io.Serializable;
//用户类(存储用户名和密码)
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String username;
	public String password;
	
	public User(String username,String password)
	{
		this.username=username;
		this.password=password;
	}
	
	public User() {}
	
	
	
}
