package kmeans;

import java.io.*;
import java.util.*;
import vsm.KeywordNO;
import base.SingleDoc;

/********************************************
 *
 * K-Mean算法
 * 外界只需要使用到Kmean类中的		构造函数：public Kmean(ArrayList<SingleDoc> d, int k)
 * 							执行函数：public void run()
 * 							打印函数：public void print()
 * 							设置K值：public void setKValue(int k)
 * 							查看K值：public int getKValue()
 * 							取出聚类：public ArrayList<ArrayList<SingleDoc>> getClusters()
 * 函数使用方法
 * 构造函数：public Kmean(ArrayList<SingleDoc> d, int k)
 * 		输入1：ArrayList<SingleDoc>类型的SingleDoc的ArrayList 即文档总数
 * 		输入2：算法的聚类数k
 * 执行函数：public void run()
 * 		运行K-Mean算法
 * 打印函数：public void print()
 * 		打印运行后的聚类结果
 * 			附：可以根据需要修改print()函数，输出自己需要的结果
 * 设置K值：public void setKValue(int k)
 *		重新设置k值
 * 查看K值：public int getKValue()
 * 		取出k值查看
 * 取出聚类：public ArrayList<ArrayList<SingleDoc>> getClusters()
 * 		取出聚类
 * 
 ********************************************/

public class Kmean 
{
	// k 值
	protected static int kValue;
	//数据记录的数目
	protected static int dataNum;
	//每个记录的维度
	protected static int dimension;
	//总的数据记录
	protected static ArrayList<SingleDoc> docs;
	//输入需要聚类的数据
	//double [][]clusterData;
	// k 个中心
	protected static double[][] centroids;
	// k 个簇
	protected static ArrayList<ArrayList<SingleDoc>> clusters;
	//构造函数
	public Kmean(ArrayList<SingleDoc> d, int k)
	{
		docs = d;
		kValue 		= k;
		dataNum 	= d.size();
		
		dimension 	= docs.get(0).tfidf.length;
	}
	//执行函数
	public void run()
	{
		kMean();
		
	}
	//计算两个元组间的距离  
	protected double getDistXY(double[] x, double[] y) 
	{  
		if(x.length != dimension || y.length != dimension)
			return -1;				//输入数据维数不一致
		
	    double sum = 0;  
	    for(int i = 0; i < dimension; i++)  
	    {  
	        sum += (x[i]-y[i]) * (x[i]-y[i]);  
	    }  
	    return Math.sqrt(sum);  
	}  
	//随机选择  K 个原有记录作为初始 质心
	protected double[][] getInitCentroid()
	{
		double[][] centroids = new double[kValue][dimension];
		
		//生成 k 个不同的随机数
		int[] intRan = new int[kValue]; 
        int intRd = 0; //存放随机数
        int count = 0; //记录生成的随机数个数
        int flag  = 0; //是否已经生成过标志
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
        // k 个初始质心
        for(int i = 0; i < kValue; i++)
        {
        	for(int j = 0; j < dimension; j++)
        	{
        		centroids[i][j] = docs.get(intRan[i]).tfidf[j];
        	}
        }
        return centroids;
	}
	//根据质心，决定当前元组属于哪个 质心 为中心的簇  
	protected int itemOfcentroid(double[][] centroids, double[] item){  
	    double dist=getDistXY(centroids[0],item);  
	    double tmp;  
	    int label=0;				//标示属于哪一个簇  
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
	//获得当前簇的均值（质心）  
	protected double[] getCentroid(ArrayList<SingleDoc> cluster)
	{  
		//簇中记录数
	    int num = cluster.size();  
	    //新的质心
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
	//获得给定簇集的平方误差  
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
	//KMeans算法
	protected void kMean()
	{  
		clusters = new ArrayList<ArrayList<SingleDoc>>(kValue);   	//k个簇  
		
		for(int i = 0; i < kValue; i++)
		{
			ArrayList<SingleDoc> temp = new ArrayList<SingleDoc>();
			clusters.add(temp);
		}
		centroids = new double[kValue][dimension];			//k个中心点  
	    //一开始随机选取k条记录的值作为k个簇的质心（均值）  
	    centroids = getInitCentroid();
	    int lable=0;  
	    //根据默认的质心给簇赋值  
	    for(int i = 0; i < dataNum; i++)
	    {
	        lable = itemOfcentroid(centroids, docs.get(i).tfidf);
	        //将记录加入对应的簇中
	        clusters.get(lable).add(docs.get(i));  
	    }
	    double oldVar = -1;  
	    double newVar = getVar(clusters, centroids);  
	    
	    int t = 0;  
	    //开始迭代求解
	    while(Math.abs(newVar - oldVar) >= 0.5) //当新旧函数值相差不到1即准则函数值不发生明显变化时，算法终止  
	    {  
	        System.out.println("第 "+ ++t +" 次迭代开始："); 
	        //更新每个簇的中心点  
	        for (int i = 0; i < kValue; i++) 
	        {  
	        	centroids[i] = getCentroid(clusters.get(i));  
	        }
	        //计算新的准则函数值
	        oldVar = newVar;  
	        newVar = getVar(clusters,centroids); 
	        //清空每个簇 
	        for (int i = 0; i < kValue; i++) 					 
	        {  
	            clusters.get(i).clear();  
	        }
	        //根据新的质心获得新的簇  
	        for(int i=0; i < dataNum; ++i)
	        {  
	        	lable = itemOfcentroid(centroids, docs.get(i).tfidf);
		        //将记录加入对应的簇中
		        clusters.get(lable).add(docs.get(i)); 
	        } 
	    }
	    //给聚类中记录排序
	    relevancySort();
	}
	//给每个聚类中相关度排序
	protected void relevancySort()
	{
		for(int i = 0; i < clusters.size(); i++)
		{
			ArrayList<SingleDoc> cluster = clusters.get(i);
			//对于每一个簇 排序
			double[] dis = new double[dimension];
			for (int j = 0; j< cluster.size(); j++)  
	        {  
	        	dis[j] = getDistXY(cluster.get(j).tfidf, centroids[i]);
	        } 
			for (int ii = 0; ii < cluster.size() - 1; ii++)    //从第一个位置开始
			{
				int index = ii;
				for (int jj = ii + 1; jj < cluster.size(); jj++)    //寻找最小的数据索引 
					if (dis[jj] < dis[index])
						index = jj;
				if (index != ii)    //如果最小数位置变化则交换
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
	//设置 k 值
	public void setKValue(int k)
	{
		kValue = k;
	}
	//取出 k 值
	public int getKValue()
	{
		return kValue;
	}
	//取出聚类
	public ArrayList<ArrayList<SingleDoc>> getClusters()
	{
		
		return clusters;
	}
	//输出
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