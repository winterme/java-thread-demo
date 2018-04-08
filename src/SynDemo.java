/**
 * �߳�ͬ��
 * @author yue
 */
public class SynDemo implements Runnable{
    @Override
    public synchronized void run() {
    	String name = Thread.currentThread().getName();
        System.out.println(name+" ��������...");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" ����...");
    }
    public static void main(String[] args) {
    	SynDemo me =new SynDemo();
        Thread t1 = new Thread(me,"С��");
        Thread t2 = new Thread(me,"С��");
        t1.start();
        t2.start();
    }
}