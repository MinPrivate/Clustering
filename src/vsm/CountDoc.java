package vsm;

import java.text.DecimalFormat;
import java.util.ArrayList;

import base.SingleDoc;


public class CountDoc {
	
	public int totalNum;
	
	
	private ArrayList<SingleDoc> allDoc ; 
	
	//保存所有的关键词
	private ArrayList<KeywordNO> allKeyword = new ArrayList<KeywordNO>();;
	
	public CountDoc(ArrayList<SingleDoc> inputdoc){
		allDoc = inputdoc;
		totalNum = allDoc.size();
	}
	
	public void countNumber(){
		
		int docNum = allDoc.size();
		
		SingleDoc tmpSingleDoc;
		
		for(int i = 0; i < docNum; i ++){
			
			tmpSingleDoc = allDoc.get(i);
			
			allKeyword = tmpSingleDoc.setNumInDot(allKeyword);
			
		}
		
		for(int i = 0; i < docNum; i ++){
			
			tmpSingleDoc = allDoc.get(i);
			
			tmpSingleDoc.setNewKeyword(allKeyword);
			
		}
	}
	
	
	/**
	 * 获取所有
	 */
	public void computerTFIDF(){
		int docNum = allDoc.size();
		
		SingleDoc tmpSingleDoc;
		
		for(int i = 0; i < docNum; i ++){
			
			tmpSingleDoc = allDoc.get(i);
			
			tmpSingleDoc.computerTFIDF(totalNum);
			
		}
		
	}
	
	public void test(){
		int docNum = allDoc.size();
		
		SingleDoc tmpSingleDoc;
		
		for(int i = 0; i < allKeyword.size(); i ++){
			System.out.print(allKeyword.get(i).keyword);
		}
		System.out.println();
		
		DecimalFormat df=new DecimalFormat("#0.00");
		
		for(int i = 0; i < docNum; i ++){
			
			tmpSingleDoc = allDoc.get(i);
			
			for(int j = 0; j < tmpSingleDoc.tfidf.length; j ++){
				System.out.print(df.format(tmpSingleDoc.tfidf[j])+"\t");
			}
			System.out.println();
			
		}
	}
	
	
}
