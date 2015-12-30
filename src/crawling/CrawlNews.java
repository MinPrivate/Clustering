package crawling;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 

public class CrawlNews {

	
	PrintWriter writer;
	public CrawlNews() throws IOException{
		writer=new PrintWriter(new BufferedWriter(new FileWriter("data.txt",true)));
	}
    public ArrayList<String> getTit(){
    	
    	@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayList<String> title = new ArrayList();

    	for(int k = 1; k < 20; k++){	   
  		  	title.addAll(getT("http://www.engadget.com/"+"page/"+ k +"/"));
  		  	title.addAll(getT("http://www.baselinescenario.com/"+"page/"+ k +"/"));
    		title.addAll(getT("http://www.globaltimes.cn/WORLD/Americas/tabid/76/lapg-1981/" + k +"/Default.aspx"));
    		title.addAll(getT("http://www.baselinescenario.com/"+"page/"+ k +"/"));
  		}
    	
    	writer.flush();
		return title;
    	
    }
	
	protected  ArrayList<String> getT(String u){
    	String url = u;
        //System.out.println(url);
        String html = getHTML(url, "utf-8");
      
        ArrayList<String> ListTitle = getTitle(html);
        
        return ListTitle;      
     }
 	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected  ArrayList<String> getTitle(String s)
	 {
	  String regex;
	  ArrayList<String>  ListTitle = new ArrayList();
      ArrayList<String>  list = new ArrayList<String>();
	  regex = "<h[2-3].*?</a>";
	  final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
	  final Matcher ma = pa.matcher(s);
	  while (ma.find())
	  {
	   list.add(ma.group());
	  }
	  for (int i = 0; i < list.size(); i++)
	  {
		  String title=outTag(list.get(i));
		  writer.write(title+"\n");
		 ListTitle.add(title);
		 System.out.println(title);
	  }
	  
	  return ListTitle;
	 }
	
    protected  String outTag(final String s)
	 {
	  return s.replaceAll("<.*?>", "");
	 }
    
     public  String getHTML(String pageURL, String encoding) {
 
       StringBuilder pageHTML = new StringBuilder();
 
        try {

            URL url = new URL(pageURL);
 
             HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            connection.setRequestProperty("User-Agent", "MSIE 7.0");
 
            BufferedReader br = new BufferedReader(new InputStreamReader(
                     connection.getInputStream(), encoding));

             String line = null; 
 
            while ((line = br.readLine()) != null) {

                pageHTML.append(line);

                pageHTML.append("rn");
 
            }

             connection.disconnect();

       } catch (Exception e) {
 
             e.printStackTrace();
 
         }
 
         return pageHTML.toString();
 
     }
       
}
