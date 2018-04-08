import java.util.Scanner;
/**
 * 后台守候线程
 * @author yue
 */
public class Daemon {
	public static void main(String[] args){
		Thread t = new Thread(
			new Runnable() {
				@Override
				public void run() {
					while(true){
						System.out.println("请输入数字：");
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
