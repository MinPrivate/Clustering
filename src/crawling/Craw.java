package crawling;

import java.io.IOException;

public class Craw {

	
	
	public static void main(String[] args) throws IOException{
		
		CrawlNews gn = new CrawlNews();
		System.out.println("crawling starting...");
		gn.getTit();
		System.out.println("crawling end");
    }
}
