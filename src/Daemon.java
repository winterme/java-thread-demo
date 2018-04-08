import java.util.Scanner;
/**
 * ��̨�غ��߳�
 * @author yue
 */
public class Daemon {
	public static void main(String[] args){
		Thread t = new Thread(
			new Runnable() {
				@Override
				public void run() {
					while(true){
						System.out.println("���������֣�");
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		);
		t.setDaemon(true);
		t.start();
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		System.out.println(n+m);
	}
}
