/**
 * 线程同步
 * @author yue
 */
public class SynDemo implements Runnable{
    @Override
    public synchronized void run() {
    	String name = Thread.currentThread().getName();
        System.out.println(name+" 正在运行...");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" 结束...");
    }
    public static void main(String[] args) {
    	SynDemo me =new SynDemo();
        Thread t1 = new Thread(me,"小白");
        Thread t2 = new Thread(me,"小灰");
        t1.start();
        t2.start();
    }
}