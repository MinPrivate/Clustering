package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import base.SingleDoc;


public class SplitWord 
{	public static ArrayList<String> StopWords=new ArrayList<String>();//定义垃圾列表
    public static ArrayList<String> CurrentList=new ArrayList<String>();
    public static int CurrentNum;
   //实例初始化，将无用词读取到内存中
	 public SplitWord(){
				 StopWords=readLog("StopWords.txt");
			}
//读取本地文件
public ArrayList<String> readLog(String s)
{   ArrayList<String> temp=new ArrayList<String>();
	try {
        File file=new File(s);
        if(file.isFile() && file.exists()){ 
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
            	  temp.add(lineTxt);
            }
            read.close();
            
        }else{
        		System.out.println(s+"List Not Found!");
        		System.exit(1);
        }
		} catch (Exception e) {
				System.out.println("Error!");
				e.printStackTrace();
				System.exit(2);
		}
	      return temp; 
}
//拆分单词并删除无用单词	
     private void MakeTerm(String input){  
    	 ArrayList<String> Templist=new ArrayList<String>();//历史列表
    	 String data=input.toLowerCase();
    	 String[] s1 = data.split("[^a-zA-Z0-9@.com.cn.net.org.edu-]+");//分割文字
    	 CurrentNum=s1.length;//统计初始字数
    	 for(int k=0;k<s1.length;k++)
    	 	{
    		 CurrentList.add(s1[k]);
    	 	}
    	 for(int i=0;i<CurrentList.size();i++)
    	 	{
    		 	for(int j=0;j<StopWords.size();j++)
    		 		{
    		 			if(CurrentList.get(i).equals(StopWords.get(j)))
    		 		{
    		 				Templist.add(CurrentList.get(i));
    		 		}
    		 		}
    	 	}//获取文中的无用词
		for(int i=0;i<Templist.size();i++)
			{
			CurrentList.remove(Templist.get(i));
			}//删除无用词
	}
 
//处理爬来的数据
	 public ArrayList<SingleDoc> ProcessDoc(ArrayList<String> File)
		{  
		ArrayList<SingleDoc> Outputlist=new ArrayList<SingleDoc>();
		SingleDoc Document[] = new SingleDoc[File.size()];
		for(int i=0;i<Document.length;i++){
			Document[i]=new SingleDoc();
			Document[i].ID=i;
			Stemmer stemmingClass=new Stemmer();
			MakeTerm(File.get(i));
			Document[i].Keyword=stemmingClass.stemming(CurrentList);
			CurrentList.clear();
			Document[i].totalNum=CurrentNum;
			Outputlist.add(Document[i]);
		}
		
		return Outputlist;
	
		}
	
}
