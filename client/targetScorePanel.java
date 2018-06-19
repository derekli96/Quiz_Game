//当前正在冲击的目标金额

public class targetScorePanel extends MessagePanel{
	    

	public targetScorePanel(){}
	//显示当前分数
	public targetScorePanel(String Message){
		this.setMessage("￥"+Message);
	}
	
    public void update(String newMessage){
        this.setMessage("￥"+newMessage);
        repaint();
	}
}