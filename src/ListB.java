import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * 线程安全的List
 * @author yue
 */
public class ListB{
	private List<String> list = Collections.synchronizedList(new ArrayList<String>());
	public ListB(){
		list.add("A999999999");
		list.add("B999999999");
		list.add("C999999999");
	}
	
	public void query(){
		Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					synchronized (list) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Iterator<String> it = list.iterator();
						while(it.hasNext()){
							System.out.println(it.next());
						}
					}
				}
			}
		});
		th.start();
	}
	
	public void add(String text){
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					synchronized (list) {
						list.add(text);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		t.start();
	}
	
	public static void main(String[] args) {
		ListB t = new ListB();
		t.add("Hello");
		t.query();
	}
}
