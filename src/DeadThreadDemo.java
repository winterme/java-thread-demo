//顾客类
class Customer{
    public synchronized void say(Waiter w){
        System.out.println("顾客:说先服务，再付费");
        w.doService();
    }
    public synchronized void doService(){
        System.out.println("同意先付款再享受服务");
    }
}
//服务生类
class Waiter{
    public synchronized void say(Customer c){
        System.out.println("服务员说：先付费，再服务");
        c.doService();
    }
    public synchronized void doService(){
        System.out.println("同意先服务再收费");
    }
}
//死锁线程
class DeadThread implements Runnable{
	Customer c =new Customer();
	Waiter w = new Waiter();
	public DeadThread(){
		new Thread(this).start();
		w.say(c);
	}
	@Override
	public void run() {
		c.say(w);
	}
}
public class DeadThreadDemo {
	public static void main(String[] args) {
		new DeadThread();
	}
}
/*******************死锁产生的原因***************************
 * 死锁原因：A对象在持有锁的时候，对B对象调用方法 
 * new Thread(this).start(); //执行会调用 c.say(w)，然后调用 w.say(c) 
 * 如果 c.say(w) 先执行，对象c会被上锁，并会调用 w.doService 
 * 由于该方法加了同步锁，因而对象 w 会被上锁
 * 当 w.say(c)  被调用的时候，由于对象 w被上锁，故而等待
 * 如果两者同时执行，c.say(w)先锁住 c ，w.say(c) 先锁住 w
 * 因而对象 c 无法使用 say(w) 中的w，对象w无法使用say(c) 中的c对象，从而产生死锁 
 */