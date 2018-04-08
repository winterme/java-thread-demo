/**
 * 多线程分段计算
 * @author yue
 */
public class Calc extends Thread {
	private int start;
	private int end;
	public Calc(int start,int end){
		this.start = start;
		this.end = end;
	}
	@Override
	public  void run(){
		int sum = 0;
		for(int i=start;i<=end;i++){
			sum+=i;
		}
		System.out.println(sum);
	}
	
	public static void main(String[] args){
		Calc c1 = new Calc(1,10000);
		Calc c2 = new Calc(10001,20000);
		Calc c3 = new Calc(20001,30000);
		//c1.setPriority(10);
		c1.start();
		//c2.setPriority(5);
		c2.start();
		//c3.setPriority(1);
		c3.start();
	}
}
