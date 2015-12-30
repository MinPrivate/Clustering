package kmeans;

import java.io.*;
import java.util.*;
import vsm.KeywordNO;
import base.SingleDoc;

/********************************************
 *
 * K-Mean�㷨
 * ���ֻ��Ҫʹ�õ�Kmean���е�		���캯����public Kmean(ArrayList<SingleDoc> d, int k)
 * 							ִ�к�����public void run()
 * 							��ӡ������public void print()
 * 							����Kֵ��public void setKValue(int k)
 * 							�鿴Kֵ��public int getKValue()
 * 							ȡ�����ࣺpublic ArrayList<ArrayList<SingleDoc>> getClusters()
 * ����ʹ�÷���
 * ���캯����public Kmean(ArrayList<SingleDoc> d, int k)
 * 		����1��ArrayList<SingleDoc>���͵�SingleDoc��ArrayList ���ĵ�����
 * 		����2���㷨�ľ�����k
 * ִ�к�����public void run()
 * 		����K-Mean�㷨
 * ��ӡ������public void print()
 * 		��ӡ���к�ľ�����
 * 			�������Ը�����Ҫ�޸�print()����������Լ���Ҫ�Ľ��
 * ����Kֵ��public void setKValue(int k)
 *		��������kֵ
 * �鿴Kֵ��public int getKValue()
 * 		ȡ��kֵ�鿴
 * ȡ�����ࣺpublic ArrayList<ArrayList<SingleDoc>> getClusters()
 * 		ȡ������
 * 
 ********************************************/

public class Kmean 
{
	// k ֵ
	protected static int kValue;
	//���ݼ�¼����Ŀ
	protected static int dataNum;
	//ÿ����¼��ά��
	protected static int dimension;
	//�ܵ����ݼ�¼
	protected static ArrayList<SingleDoc> docs;
	//������Ҫ���������
	//double [][]clusterData;
	// k ������
	protected static double[][] centroids;
	// k ����
	protected static ArrayList<ArrayList<SingleDoc>> clusters;
	//���캯��
	public Kmean(ArrayList<SingleDoc> d, int k)
	{
		docs = d;
		kValue 		= k;
		dataNum 	= d.size();
		
		dimension 	= docs.get(0).tfidf.length;
	}
	//ִ�к���
	public void run()
	{
		kMean();
		
	}
	//��������Ԫ���ľ���  
	protected double getDistXY(double[] x, double[] y) 
	{  
		if(x.length != dimension || y.length != dimension)
			return -1;				//��������ά����һ��
		
	    double sum = 0;  
	    for(int i = 0; i < dimension; i++)  
	    {  
	        sum += (x[i]-y[i]) * (x[i]-y[i]);  
	    }  
	    return Math.sqrt(sum);  
	}  
	//���ѡ��  K ��ԭ�м�¼��Ϊ��ʼ ����
	protected double[][] getInitCentroid()
	{
		double[][] centroids = new double[kValue][dimension];
		
		//���� k ����ͬ�������
		int[] intRan = new int[kValue]; 
        int intRd = 0; //��������
        int count = 0; //��¼���ɵ����������
        int flag  = 0; //�Ƿ��Ѿ����ɹ���־
        while(count < kValue){
             Random rdm = new Random();
             intRd = Math.abs(rdm.nextInt()) % dataNum;
             for(int i = 0; i < count; i++){
                 if(intRan[i] == intRd){
                     flag = 1;
                     break;
                 }else{
                     flag = 0;
                 }
             }
             if(flag==0){
                 intRan[count] = intRd;
                 count++;
             }
        } 
        // k ����ʼ����
        for(int i = 0; i < kValue; i++)
        {
        	for(int j = 0; j < dimension; j++)
        	{
        		centroids[i][j] = docs.get(intRan[i]).tfidf[j];
        	}
        }
        return centroids;
	}
	//�������ģ�������ǰԪ�������ĸ� ���� Ϊ���ĵĴ�  
	protected int itemOfcentroid(double[][] centroids, double[] item){  
	    double dist=getDistXY(centroids[0],item);  
	    double tmp;  
	    int label=0;				//��ʾ������һ����  
	    for(int i = 1; i < kValue; i++){  
	        tmp = getDistXY(centroids[i],item);  
	        if(tmp < dist) 
	        {
	        	dist = tmp;
	        	label = i;
	        }  
	    }  
	    return label;     
	}  
	//��õ�ǰ�صľ�ֵ�����ģ�  
	protected double[] getCentroid(ArrayList<SingleDoc> cluster)
	{  
		//���м�¼��
	    int num = cluster.size();  
	    //�µ�����
	    double[] newCentroid = new double[dimension];
	    for(int i = 0; i < dimension; i++)
	    {
	    	newCentroid[i] = 0;
	    }
	    
	    for (int i = 0; i < dimension; i++)  
	    {  
	        for(int j = 0; j < num; j++)  
	        {  
	        	newCentroid[i] += cluster.get(j).tfidf[i];
	        }  
	    }  
	    for(int i = 0; i < dimension; i++)  
	    {
	    	if(num != 0)
	    	{
	    		newCentroid[i] /= num;  
	    	}
	    }
	    
	    return newCentroid;   
	} 
	//��ø����ؼ���ƽ�����  
	protected double getVar(ArrayList<ArrayList<SingleDoc>> clusters,double[][] centroids)
	{  
	    double var = 0;  
	    for (int i = 0; i < kValue; i++)  
	    {  
	    	ArrayList<SingleDoc> cluster = clusters.get(i);  
	        for (int j = 0; j< cluster.size(); j++)  
	        {  
	        	double dis = getDistXY(cluster.get(j).tfidf, centroids[i]);
	            var +=   dis * dis;
	        }  
	    }  
	    return var;  
	}  
	//KMeans�㷨
	protected void kMean()
	{  
		clusters = new ArrayList<ArrayList<SingleDoc>>(kValue);   	//k����  
		
		for(int i = 0; i < kValue; i++)
		{
			ArrayList<SingleDoc> temp = new ArrayList<SingleDoc>();
			clusters.add(temp);
		}
		centroids = new double[kValue][dimension];			//k�����ĵ�  
	    //һ��ʼ���ѡȡk����¼��ֵ��Ϊk���ص����ģ���ֵ��  
	    centroids = getInitCentroid();
	    int lable=0;  
	    //����Ĭ�ϵ����ĸ��ظ�ֵ  
	    for(int i = 0; i < dataNum; i++)
	    {
	        lable = itemOfcentroid(centroids, docs.get(i).tfidf);
	        //����¼�����Ӧ�Ĵ���
	        clusters.get(lable).add(docs.get(i));  
	    }
	    double oldVar = -1;  
	    double newVar = getVar(clusters, centroids);  
	    
	    int t = 0;  
	    //��ʼ�������
	    while(Math.abs(newVar - oldVar) >= 0.5) //���¾ɺ���ֵ����1��׼����ֵ���������Ա仯ʱ���㷨��ֹ  
	    {  
	        System.out.println("�� "+ ++t +" �ε�����ʼ��"); 
	        //����ÿ���ص����ĵ�  
	        for (int i = 0; i < kValue; i++) 
	        {  
	        	centroids[i] = getCentroid(clusters.get(i));  
	        }
	        //�����µ�׼����ֵ
	        oldVar = newVar;  
	        newVar = getVar(clusters,centroids); 
	        //���ÿ���� 
	        for (int i = 0; i < kValue; i++) 					 
	        {  
	            clusters.get(i).clear();  
	        }
	        //�����µ����Ļ���µĴ�  
	        for(int i=0; i < dataNum; ++i)
	        {  
	        	lable = itemOfcentroid(centroids, docs.get(i).tfidf);
		        //����¼�����Ӧ�Ĵ���
		        clusters.get(lable).add(docs.get(i)); 
	        } 
	    }
	    //�������м�¼����
	    relevancySort();
	}
	//��ÿ����������ض�����
	protected void relevancySort()
	{
		for(int i = 0; i < clusters.size(); i++)
		{
			ArrayList<SingleDoc> cluster = clusters.get(i);
			//����ÿһ���� ����
			double[] dis = new double[dimension];
			for (int j = 0; j< cluster.size(); j++)  
	        {  
	        	dis[j] = getDistXY(cluster.get(j).tfidf, centroids[i]);
	        } 
			for (int ii = 0; ii < cluster.size() - 1; ii++)    //�ӵ�һ��λ�ÿ�ʼ
			{
				int index = ii;
				for (int jj = ii + 1; jj < cluster.size(); jj++)    //Ѱ����С���������� 
					if (dis[jj] < dis[index])
						index = jj;
				if (index != ii)    //�����С��λ�ñ仯�򽻻�
				{
					//DataSwap(&pDataArray[index], &pDataArray[ii]);
					double temp = dis[index];
					dis[index] = dis[ii];
					dis[ii] = temp;
					
					SingleDoc tempdoc = cluster.get(index);
					cluster.set(index, cluster.get(ii));
					cluster.set(ii, tempdoc);
				}
			}
		}
	}
	//���� k ֵ
	public void setKValue(int k)
	{
		kValue = k;
	}
	//ȡ�� k ֵ
	public int getKValue()
	{
		return kValue;
	}
	//ȡ������
	public ArrayList<ArrayList<SingleDoc>> getClusters()
	{
		
		return clusters;
	}
	//���
	public void print() throws IOException
	{
		
		PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter("out.txt")));
		for(int i = 0; i < clusters.size(); i++)
		{
			ArrayList<SingleDoc> cluster = clusters.get(i);
			for(int j = 0; j < cluster.size(); j++)
			{
				String info = (j+1) + ". ( ";
				ArrayList<KeywordNO> keywords=cluster.get(j).afterKeyword;
				for(int k = 0; k < dimension; k++)
				{	
					if(keywords.get(k).num!=0)
						info += keywords.get(k).keyword + " ";
				}
				info += " )";
				out.println(info);
			}
		}
		out.flush();
	}
}