import java.util.Scanner;
/**
 * 线程控制（暂停、恢复、停止）
 * @author yue
 */
class Ball extends Thread{
	private boolean flag = true;
	private boolean pa = false;
	public void gameover(){
		this.flag = false;
	}
	public void pause(){
		this.pa = true;
	}
	public void goon(){
		this.pa = false;
	}
	@Override
	public  void run(){
		while(flag){
			System.out.println("moving...");
			try {
				Thread.sleep(1000);
				if(pa){
					synchronized(this){
						this.wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

public class Controller{
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Ball ball = new Ball();
		System.out.println("游戏开始");
		ball.start();
		while(true){
			System.out.println("输入0结束游戏，1暂停游戏，2继续游戏");
			int n = sc.nextInt();
			if(n==0){
				ball.gameover();
				System.out.println("游戏结束！");
				break;
			}else if(n==1){
				System.out.println("游戏暂停！");
				ball.pause();
			}else{
				System.out.println("游戏继续！");
				ball.goon();
				synchronized (ball) {
					ball.notify();
				}
			}
		}
	}
}