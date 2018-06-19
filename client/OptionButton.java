import java.awt.*;
import javax.swing.*;

//选项按钮

public class OptionButton extends JButton{
	private String optionType;
	public int times;
	Color bj=new Color(238,238,238);
	//设置选项按钮格式
	public OptionButton(String optionType,String newText){
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setHorizontalAlignment(LEFT);
		this.setBackground(Color.WHITE);
		this.setOptionType(optionType);
		this.setText("    "+optionType+" "+newText);
	}
	
	public void setOptionType(String optionType){
		this.optionType=optionType;
	}
	
	public String getOptionType(){
		return this.optionType;
	}
	
	public void update(String newText){
		times=0;
		this.setText(optionType+" "+newText);
		this.setVisible(true);
	}
	
}
