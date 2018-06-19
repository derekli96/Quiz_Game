import java.io.Serializable;
import java.net.Socket;
//存用户信息/socket和indicator
public class UserInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String username;
	public Socket socket;
	public int indicator=2;
	
	public UserInfo(String username,Socket socket)
	{
		this.username=username;
		this.socket=socket;
	}
	
	public UserInfo() {}
	
	
}
