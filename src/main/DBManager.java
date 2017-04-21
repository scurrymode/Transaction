//1. ������ �Ѱ��� �α� �����ͺ��̽� ���� ������ �ߺ��ؼ� �������� �ʱ� ����
//2. �ν��Ͻ��� ������ �Ѱ��� �α�! ���ø����̼� ���� �� �����Ǵ� Ŀ�ؼ� ��ü�� �ϳ��� �����ϱ� ����!

package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	static private DBManager instance;
	private String driver="oracle.jdbc.driver.OracleDriver";
	private String url="jdbc:oracle:thin:@localhost:1521:XE";
	private String user="bread";
	private String password="bread";
	private Connection con;
	
	private DBManager() {
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static public DBManager getInstance() {
		if(instance==null){
			instance = new DBManager();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public void disConnect(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
