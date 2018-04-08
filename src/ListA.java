import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 非线程安全的List
 * @author yue
 */
public class ListA{
	private List<String> list = new ArrayList<String>();
	public ListA(){
		list.add("A999999999");
		list.add("B999999999");
		list.add("C999999999");
	}
	
	public void query(){
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					Iterator<String> it = list.iterator();
					while(it.hasNext()){
						System.out.println(it.next());
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	public void add(String text){
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					list.add(text);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	public static void main(String[] args) {
		ListA a = new ListA();
		a.add("Hello");
		a.query();
	}
}

