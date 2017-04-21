package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AppMain extends JFrame{
	JTextField t_name, t_age, t_weight, t_height;
	JButton bt;
	
	DBManager manager=DBManager.getInstance();
	Connection con=manager.getConnection();
	
	public AppMain() {
		setLayout(new FlowLayout());
		
		t_name = new JTextField(13);
		t_age = new JTextField(13);
		t_weight = new JTextField(13);
		t_height = new JTextField(13);
		
		bt = new JButton("등록");
		
		
		add(t_name);
		add(t_age);
		add(t_weight);
		add(t_height);
		add(bt);
		
		bt.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				regist();
				
			}
		});
		
		setSize(200, 180);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	//사원 등록시 두개의 테이블로 분리되어 있으므로 만일의 사태(데이터 무결성 깨짐)에 대비 트랜젝션을 적용한 프로그램을 작성해보자!
	//사원 등록이란 업무는 2개 세부업무로 이뤄진 트랜젝션이다!! 
	public void regist(){
		PreparedStatement pstmt=null;
		StringBuffer sql = new StringBuffer();
		//둘중의 하나라도 입력 실패하면 처음부터 없었던 일로 되돌려놓자! (rollback)
		//staff 테이블에 insert
		try {
			//Connection 객체에는 setAutoCommit(); 이 메서드가 디폴트로 true 되 있기 때문에, jdbc를 이용한 dml은 개발자가 별도로 commit을 하지 않아도 되었었다.
			con.setAutoCommit(false); //트랜잭션의 시작!
			
			sql.append("insert into staff(STAFF_ID, STAFF_NAME, AGE)");
			sql.append(" values(seq_staff.nextval, ?,?)");
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, t_name.getText());
			pstmt.setInt(2, Integer.parseInt(t_age.getText()));
			pstmt.executeUpdate();
			
			//bio 테이블에 insert
			sql.delete(0, sql.length());
			sql.append("insert into bio(STAFF_ID, WEIGHT, HEIGHT)");
			sql.append(" values(seq_staff.currval,?,?)");
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setInt(1, Integer.parseInt(t_weight.getText()));
			pstmt.setInt(2, Integer.parseInt(t_height.getText()));
			pstmt.executeUpdate();
			con.commit();
			
		} catch (SQLException e) {
			try {
				con.rollback();
				e.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
//		} finally {
			//Connection 객체의 autoCommit 속성을 다시 돌려놓자!
//			try {
//				con.commit();
//				JOptionPane.showMessageDialog(this, "등록완료");
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
	}

	
	public static void main(String[] args) {
		new AppMain();

	}

}
