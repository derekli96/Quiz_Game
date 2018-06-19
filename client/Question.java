import java.util.Map;
import java.util.HashMap;

//把题库中读到的题目放进题板

public class Question{
	
	public Map[][] questionSet;
	public int questionNo=-1;
	public int setCount=0;
	public int setLength=12;
	public String preQ;
	
	public  Question(String preQ){
		this.questionSet=new HashMap[5][12];
        this.preQ=preQ;
        for(int i=0;i<5;i++){
        	int j=0;
        	for(j=0;j<12;j++){
        		questionSet[i][j]=new HashMap();
        	}
        }
        parsePreQ();
	}
	
	public Map getNewQuestion(){
		questionNo=questionNo+1;
		Map nextQuestion=questionSet[setCount][questionNo];
		if(questionNo==11){
			questionNo=-1;
		}
		return nextQuestion;
	}
	
	public Map getNewSet(){
		setCount+=1;
		questionNo=0;
		if(setCount>4) setCount=1;
		return questionSet[setCount][questionNo];
	}
	
	public Map getTryQuestion(){
		questionNo=0;
		return questionSet[0][0];
	}
	
	public void parsePreQ(){
		String[] array=preQ.split("#");
		for(int j=0;j<5;j++) {
			for(int i=1;i<=12;i++){
				String[] question=array[i+j*12].split("\\$");
				questionSet[j][i-1].put("id",question[0]);
				questionSet[j][i-1].put("Question",question[1]);
				questionSet[j][i-1].put("options[0]",question[2]);
				questionSet[j][i-1].put("options[1]",question[3]);
				questionSet[j][i-1].put("options[2]",question[4]);
				questionSet[j][i-1].put("options[3]",question[5]);
				questionSet[j][i-1].put("correctAnswer",question[6]);
				questionSet[j][i-1].put("picture","");
			}
		}
	
		
	}
	
	public void insertPicture(String picture){
		String[] array=picture.split("#");
		for(int j=0;j<5;j++){
			for(int i=0;i<12;i++){
				if(array[1].split("\\$")[0].equals(questionSet[j][i].get("id").toString())){
					questionSet[j][i].put("picture",array[1].split("\\$")[1]);
					System.out.println(array[1].split("\\$")[1]);
				}
			}
		}
	}
}