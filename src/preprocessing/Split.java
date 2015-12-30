package preprocessing;

import java.util.ArrayList;

import vsm.CountDoc;
import base.SingleDoc;

public class Split {
	public static ArrayList<String> Doc=new ArrayList<String>();
	public static ArrayList<SingleDoc> Huska=new ArrayList<SingleDoc>();
	//for testing
	public static void main(String[] args) {
		
		Doc.add("Microsoft's Bing may be a boy among men in the search-engine wars");
		Doc.add("where it just added a trending topics carousel that shows timely info in the same category as your query (see the above image)");
		Doc.add("Microsoft needs all the help it can get in se");
		Doc.add("and Bing also said it's now extended the article index several years back");
		Doc.add("the same category as your query (see the above image google.com)");
		SplitWord ListTerm=new SplitWord();
		Huska=ListTerm.ProcessDoc(Doc);
		
		for(int i=0;i<Huska.size();i++){
			System.out.println(Huska.get(i).ID+1);
        	for(String Printinfo:Huska.get(i).Keyword)
        	{
        		System.out.println(Printinfo);
        	}
            
		}
		
		CountDoc countDoc = new CountDoc(Huska);
		
		countDoc.countNumber();

		countDoc.computerTFIDF();
	
		///////////////////////////输出查看正确性
		countDoc.test();
		
	}
}

