import java.util.Scanner;
/**
 * �߳̿��ƣ���ͣ���ָ���ֹͣ��
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
		System.out.println("��Ϸ��ʼ");
		ball.start();
		while(true){
			System.out.println("����0������Ϸ��1��ͣ��Ϸ��2������Ϸ");
			int n = sc.nextInt();
			if(n==0){
				ball.gameover();
				System.out.println("��Ϸ������");
				break;
			}else if(n==1){
				System.out.println("��Ϸ��ͣ��");
				ball.pause();
			}else{
				System.out.println("��Ϸ������");
				ball.goon();
				synchronized (ball) {
					ball.notify();
				}
			}
		}
	}
}