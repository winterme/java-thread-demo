import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
/**
 * 多线程文件读写
 * @author yue
 */
class DownLoadThread extends Thread {  
    private final int BUFF_LEN = 32; // 定义字节数组的长度 
    private long start; // 定义下载的起始点 
    private long end; // 定义下载的结束点
    private InputStream is; // 下载资源对应的输入流  
    private RandomAccessFile raf; // 将下载到的字节输出到raf中      
    // 构造器，传入输入流，输出流和下载起始点、结束点  
    public DownLoadThread(long start, long end, InputStream is, RandomAccessFile raf) {    
        System.out.println(start + "---->" + end); // 输出该线程负责下载的字节位置
        this.start = start;  
        this.end = end;  
        this.is = is;  
        this.raf = raf;  
    }

    @Override
    public void run(){
    	try{
    		is.skip(start);
    		raf.seek(start);
    		byte[] buff = new byte[BUFF_LEN];//定义输入流缓存数组
    		long contentLen = end - start;//本线程负责下载资源的大小
    		long times = contentLen / BUFF_LEN + 4;//定义读取次数
    		int hasRead = 0;//实际读取的字节数
    		for (int i = 0; i < times; i++) {
    			hasRead = is.read(buff);
    			if(hasRead < 0) break;//如果读取的字节数小于0，则退出循环！
    			raf.write(buff,0,hasRead);
    		}
    	} catch (IOException e) {
			e.printStackTrace();
		}finally{
    		if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		if(raf != null)
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    	}
    }
}
/**
 * 多线程网络资源文件下载
 * @author yue
 */
public class DownLoadUtil{
	/**
	 * 定义获取网络资源文件大小的方法
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static long getFileLength(URL url) throws Exception{
		long length = 0;
		URLConnection con = url.openConnection();
		long size = con.getContentLength();
		length = size;
		return length;
	}
	/**
	 * 定义文件下载的方法
	 * @param href
	 * @param file_name
	 * @param num
	 */
	public static void download(String href,String file_name,int num){
		//定义几个线程去下载
		final int THREAD_NUM = num;
		//定义下载文件名称
		final String OUT_FILE_NAME = file_name;
		//按照线程数定义输入流数组
		InputStream[] isArr = new InputStream[THREAD_NUM];
		//按照线程数定义随机文件读写器数组
		RandomAccessFile[] outArr = new RandomAccessFile[THREAD_NUM];
		try{
			//实例化URL网络资源对象
			URL url = new URL(href);
			isArr[0] = url.openStream(); //打开第一个输入流
			long fileLen = getFileLength(url);//获取网站资源大小
			System.out.println("网络资源的大小" + fileLen);
			outArr[0] = new RandomAccessFile(OUT_FILE_NAME, "rw");
			//创建一个与下载资源相同大小的空文件
			for(int i = 0; i < fileLen; i++) {
			      outArr[0].write(0);
			}
			
			long size = fileLen / THREAD_NUM; //每线程应该下载的字节数
			long left = fileLen % THREAD_NUM; //整个下载资源整除后剩下的余数取模
			for (int i = 0; i < THREAD_NUM; i++) {
				if (i != 0) {
					isArr[i] = url.openStream();// 以URL打开多个输入流
					outArr[i] = new RandomAccessFile(OUT_FILE_NAME, "rw");
				}
				//分别启动多个线程来下载网络资源
				if (i == THREAD_NUM - 1) {
					//最后一个线程下载指定size+left个字节
					new DownLoadThread(i * size, (i + 1) * size + left, isArr[i], outArr[i]).start();
				} else {
					//每个线程负责下载一定的size个字节
					new DownLoadThread(i * size, (i + 1) * size, isArr[i], outArr[i]).start();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String href = "http://download.redis.io/releases/redis-4.0.8.tar.gz";
		String name = "redis.tar.gz";
		DownLoadUtil.download(href,name,4);
	}
}			
