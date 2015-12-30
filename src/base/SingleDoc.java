package base;

import java.util.ArrayList;

import vsm.KeywordNO;

public class SingleDoc 
{
   public int ID;
   public ArrayList<String> Keyword = new ArrayList<String>();
   public ArrayList<KeywordNO> afterKeyword = new ArrayList<KeywordNO>();
   
   public int totalNum;
   
   public double[] tfidf;
   
   public void setTotalNum(int totalNum){
	   this.totalNum = totalNum;
   }
	/**
   * �������йؼ��ʵĸ���
   * �������㣬�����еĵ��ʼ���
   */
   public ArrayList<KeywordNO> setNumInDot(ArrayList<KeywordNO> fromtop){
	   
	   int size = Keyword.size();
	   //Collections.sort(Keyword);
	   
	   //�����µļ���
	   for(int i = 0; i < fromtop.size();i ++){
		   afterKeyword.add(new KeywordNO(0,fromtop.get(i).keyword));
	   }
	   
	   
	   
	   int tmpSize;
	   boolean found = false;
	   
	   
	   for(int i = 0; i < size; i ++){
		   tmpSize = afterKeyword.size();
		   
		   //�½�϶Աȣ�ͬ������Ŀ��һ ��������뼯��
		   for(int j = 0; j < tmpSize; j ++){
			   if(afterKeyword.get(j).keyword.equals(Keyword.get(i))){
				   
				   afterKeyword.get(j).num ++;
				   found = true;
				   break;
			   }else{
				   
				   found = false;
			   }
		   }
		   if(!found){
			   afterKeyword.add(new KeywordNO(1,Keyword.get(i)));
		   }
		   
	   }
	   
	   return afterKeyword;
  }
   
   
   /**
    * �����µĹؼ��ʱ�
    * 
    */
   public void setNewKeyword(ArrayList<KeywordNO> newKeyword){
	   int size = newKeyword.size();
	   tfidf = new double[size];
	   //���ԭ�б�
	   Keyword.clear();
	   //afterKeyword.clear();
	   
	   for(int i = afterKeyword.size(); i < size; i ++){
		   afterKeyword.add(new KeywordNO(0,newKeyword.get(i).keyword));
	   }
   }
   
   public void computerTFIDF(int numberDoc){
	   
	   
	   double tf;
	   double idf;
	   int tmpnum;
	   for(int i = 0; i < afterKeyword.size(); i ++){
		   tmpnum = afterKeyword.get(i).num;
		   if(tmpnum == 0){
			   tfidf[i] = 0.0;
		   }else{
			   tf = (double)tmpnum/(double)totalNum;
			   idf =  Math.log(numberDoc/(double)tmpnum);
			   tfidf[i] = tf*idf;
		   }
	   }
   }
}
