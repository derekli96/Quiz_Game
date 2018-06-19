import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

//当题目中有图片时弹出窗口显示图片


public class PictureFrame extends JFrame{
	
	public PicturePanel pic;
	public String picString=new String();
	public Convert conver=new Convert();
	
	protected void processWindowEvent(WindowEvent e) {  
		    if (e.getID() == WindowEvent.WINDOW_CLOSING) { 
		    	JOptionPane.showMessageDialog(null,"本题内容包含图片信息，\n请勿关闭图片显示窗口！","错误",JOptionPane.ERROR_MESSAGE);
		    return;
		    }//阻止默认动作，阻止窗口关闭 
		    super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
		} 
	
	public PictureFrame(Map QuestionInfo){
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		ImageIcon im=new ImageIcon("interface/1V1人机.jpg");
		Image temp=im.getImage().getScaledInstance(400,75,im.getImage().SCALE_DEFAULT);  
		pic=new PicturePanel(temp);
		setSize(200, 200);
		setResizable(false);
		int windowWidth = getWidth(); // 获得窗口宽
		int windowHeight = getHeight(); // 获得窗口高
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		setLocation(screenWidth / 2+400, screenHeight / 2 - windowHeight / 2);// 设置窗口在主窗口右侧显示
		setTitle("");
		setLayout(new BorderLayout());
		picString=QuestionInfo.get("picture").toString();
		if(picString.length()!=0){
			byte[] image=Convert.StringToImageFile(picString);
			ByteArrayInputStream in = new ByteArrayInputStream(image);  
			BufferedImage images=new BufferedImage(1,1,1);
			try {
				images = ImageIO.read(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pic=new PicturePanel(images);
			add(pic);
			setVisible(true);
		}
		else{
			add(pic);
			setVisible(false);
		}
		
		
	}
	
	public void update(Map QuestionInfo){
		picString=QuestionInfo.get("picture").toString();
		if(picString.length()==0){
			setVisible(false);
			}
		else{
		byte[] image=Convert.StringToImageFile(picString);
		ByteArrayInputStream in = new ByteArrayInputStream(image);  
		BufferedImage images=new BufferedImage(1,1,1);
		try {
			images = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pic.update(images);
		setVisible(true);
		}
		
	}
	
}

class PicturePanel extends JPanel implements ImageObserver{
	Image img;
	
	public PicturePanel(Image image){
		setSize(200,200);
		this.img=image;
		
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this);
		
	}
	
	public void update(Image image){
		this.img=image;
		repaint();
	}
	
}

//将字符串转换成图片
    class Convert {
	private static String ImageFileFormat = "jpg";
	private static StringBuffer sb1 = null;
	private static BufferedImage buffImage = null;
	private static ByteArrayOutputStream baos = null;
	private static byte[] byteImage = null;
	private static String ImageFileNameFormat = "yyyy-MM-dd_HH-mm-ss";
	private static String splitSymbol = ",";
	private static String[] imageStringArr = null;

	// picturePath为图片文件的在磁盘上的保存路径,返回值为文件转换成字符串之后的字符串,转换过程以逗号分隔（此方法理论上也可以将非图片格式的文件转换成字符串）
	public static String ImageFileToString(String picturePath) {
		sb1 = new StringBuffer();
		try {
			buffImage = ImageIO.read(new File(picturePath));
			baos = new ByteArrayOutputStream();
			ImageIO.write(buffImage, ImageFileFormat, baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byteImage = baos.toByteArray();
		for (int i = 0; i < byteImage.length; i++) {
			if (sb1.length() == 0) {
				sb1.append(byteImage[i]);
			} else {
				sb1.append(splitSymbol + byteImage[i]);
			}
		}
		return sb1.toString();
	}

	// pictureString为需要转换成图片文件的字符串，pictureToPath是字符串转换为图片文件之后的图片文件的保存路径
	public static byte[] StringToImageFile(String pictureString) {
			imageStringArr = split(pictureString, splitSymbol);
			byte[] image = new byte[imageStringArr.length];
			for (int i = 0; i < imageStringArr.length; i++) {
				image[i] = Byte.parseByte(imageStringArr[i]);
			}
			return image;
	}

	//分割字符串
	private static String[] split(String s, String token) {
		if (s == null)
			return null;
		if (token == null || s.length() == 0)
			return new String[] { s };
		int size = 0;
		String[] result = new String[4];
		while (s.length() > 0) {
			int index = s.indexOf(token);
			String splitOne = s;
			if (index > -1) {
				splitOne = s.substring(0, index);
				s = s.substring(index + token.length());
			} else {
				s = "";
			}
			if (size >= result.length) {
				String[] tmp = new String[result.length * 2];
				System.arraycopy(result, 0, tmp, 0, result.length);
				result = tmp;
			}
			if (splitOne.length() > 0) {
				result[size++] = splitOne;
			}
		}
		String[] tmp = result;
		result = new String[size];
		System.arraycopy(tmp, 0, result, 0, size);
		return result;
	}

	private static BufferedImage readImage(byte[] bytes) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return ImageIO.read(bais);
	}

	//保存图片
	private static String saveImage(byte[] imgages, final String saveDir) {
		try {
			BufferedImage bis = readImage(imgages);
			DateFormat sdf = new SimpleDateFormat(ImageFileNameFormat);
			String fileTime = sdf.format(new Date());
			final String name = fileTime + "_" + "." + ImageFileFormat;
			File f = new File(saveDir + name);
			boolean istrue = false;
			if (f.exists()) {
				istrue = ImageIO.write(bis, ImageFileFormat, f);
			} else {
				f.mkdirs();
				istrue = ImageIO.write(bis, ImageFileFormat, f);
			}
			if (istrue) {
				return name;
			}
		} catch (Exception e) {
		}
		return null;
	}
}
