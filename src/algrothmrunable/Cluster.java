package algrothmrunable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import kmeans.Kmean;

import preprocessing.SplitWord;
import vsm.CountDoc;

import base.SingleDoc;

public class Cluster 
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//data
		BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
		ArrayList<String> titles = new ArrayList<String>();
		String title = null;
		while((title=reader.readLine()) != null)
		{
			titles.add(title);
		}
		
		//preprocessing
		ArrayList<SingleDoc> docs = new SplitWord().ProcessDoc(titles);
		
		//caculate if idf
		CountDoc countDoc=new CountDoc(docs);
		countDoc.countNumber();
		countDoc.computerTFIDF();
		
		//k-mean
		Kmean kmean = new Kmean(docs, 20);
		kmean.run();
		kmean.print();
		
		System.out.println();
		System.out.println("Done!");
		
	}

}
