import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
/**
 * ���߳��ļ���д
 * @author yue
 */
class DownLoadThread extends Thread {  
    private final int BUFF_LEN = 32; // �����ֽ�����ĳ��� 
    private long start; // �������ص���ʼ�� 
    private long end; // �������صĽ�����
    private InputStream is; // ������Դ��Ӧ��������  
    private RandomAccessFile raf; // �����ص����ֽ������raf��      
    // ���������������������������������ʼ�㡢������  
    public DownLoadThread(long start, long end, InputStream is, RandomAccessFile raf) {    
        System.out.println(start + "---->" + end); // ������̸߳������ص��ֽ�λ��
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
    		byte[] buff = new byte[BUFF_LEN];//������������������
    		long contentLen = end - start;//���̸߳���������Դ�Ĵ�С
    		long times = contentLen / BUFF_LEN + 4;//�����ȡ����
    		int hasRead = 0;//ʵ�ʶ�ȡ���ֽ���
    		for (int i = 0; i < times; i++) {
    			hasRead = is.read(buff);
    			if(hasRead < 0) break;//�����ȡ���ֽ���С��0�����˳�ѭ����
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
 * ���߳�������Դ�ļ�����
 * @author yue
 */
public class DownLoadUtil{
	/**
	 * �����ȡ������Դ�ļ���С�ķ���
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
	 * �����ļ����صķ���
	 * @param href
	 * @param file_name
	 * @param num
	 */
	public static void download(String href,String file_name,int num){
		//���弸���߳�ȥ����
		final int THREAD_NUM = num;
		//���������ļ�����
		final String OUT_FILE_NAME = file_name;
		//�����߳�����������������
		InputStream[] isArr = new InputStream[THREAD_NUM];
		//�����߳�����������ļ���д������
		RandomAccessFile[] outArr = new RandomAccessFile[THREAD_NUM];
		try{
			//ʵ����URL������Դ����
			URL url = new URL(href);
			isArr[0] = url.openStream(); //�򿪵�һ��������
			long fileLen = getFileLength(url);//��ȡ��վ��Դ��С
			System.out.println("������Դ�Ĵ�С" + fileLen);
			outArr[0] = new RandomAccessFile(OUT_FILE_NAME, "rw");
			//����һ����������Դ��ͬ��С�Ŀ��ļ�
			for(int i = 0; i < fileLen; i++) {
			      outArr[0].write(0);
			}
			
			long size = fileLen / THREAD_NUM; //ÿ�߳�Ӧ�����ص��ֽ���
			long left = fileLen % THREAD_NUM; //����������Դ������ʣ�µ�����ȡģ
			for (int i = 0; i < THREAD_NUM; i++) {
				if (i != 0) {
					isArr[i] = url.openStream();// ��URL�򿪶��������
					outArr[i] = new RandomAccessFile(OUT_FILE_NAME, "rw");
				}
				//�ֱ���������߳�������������Դ
				if (i == THREAD_NUM - 1) {
					//���һ���߳�����ָ��size+left���ֽ�
					new DownLoadThread(i * size, (i + 1) * size + left, isArr[i], outArr[i]).start();
				} else {
					//ÿ���̸߳�������һ����size���ֽ�
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
