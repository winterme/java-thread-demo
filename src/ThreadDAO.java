import java.sql.*;
import java.util.*;

/**
 * 多线程数据表读取
 * 
 * @author yue
 */
public class ThreadDAO {
	private Connection conn;

	// 获取数据库连接
	public void getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "orilore");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取student表记录数
	public int getRecordCount() {
		int count = 0;
		try {
			this.getConnection();
			String sql = "select count(*) from student";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	// 获取student表记录数
	public void read(final int m) {
		int max = this.getRecordCount();
		int n = 0;
		if (max % m == 0)
			n = max / m;
		else {
			n = max / m + 1;
		}
		List<String> sqls = Collections.synchronizedList(new ArrayList<String>());
		for (int i = 0; i < n; i++) {
			String sql = "select * from student limit " + (i * m) + "," + m;
			sqls.add(sql);
		}
		final Iterator<String> it = sqls.iterator();
		for (int j = 0; j < n; j++) {
			Thread t = new Thread(new Runnable() {
				public synchronized void run() {
					String sqlstr = it.next();
					try {
						Statement stmt = conn.createStatement();
						ResultSet rs = stmt.executeQuery(sqlstr);
						while (rs.next()) {
							System.out.println(rs.getString(6));
						}
						rs.close();
						stmt.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			});
			t.start();
		}
	}

	public static void main(String[] args) {
		ThreadDAO dao = new ThreadDAO();
		dao.read(1000);
	}
}