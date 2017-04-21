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
		
		bt = new JButton("���");
		
		
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
	
	//��� ��Ͻ� �ΰ��� ���̺�� �и��Ǿ� �����Ƿ� ������ ����(������ ���Ἲ ����)�� ��� Ʈ�������� ������ ���α׷��� �ۼ��غ���!
	//��� ����̶� ������ 2�� ���ξ����� �̷��� Ʈ�������̴�!! 
	public void regist(){
		PreparedStatement pstmt=null;
		StringBuffer sql = new StringBuffer();
		//������ �ϳ��� �Է� �����ϸ� ó������ ������ �Ϸ� �ǵ�������! (rollback)
		//staff ���̺� insert
		try {
			//Connection ��ü���� setAutoCommit(); �� �޼��尡 ����Ʈ�� true �� �ֱ� ������, jdbc�� �̿��� dml�� �����ڰ� ������ commit�� ���� �ʾƵ� �Ǿ�����.
			con.setAutoCommit(false); //Ʈ������� ����!
			
			sql.append("insert into staff(STAFF_ID, STAFF_NAME, AGE)");
			sql.append(" values(seq_staff.nextval, ?,?)");
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, t_name.getText());
			pstmt.setInt(2, Integer.parseInt(t_age.getText()));
			pstmt.executeUpdate();
			
			//bio ���̺� insert
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
			//Connection ��ü�� autoCommit �Ӽ��� �ٽ� ��������!
//			try {
//				con.commit();
//				JOptionPane.showMessageDialog(this, "��ϿϷ�");
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
	}

	
	public static void main(String[] args) {
		new AppMain();

	}

}
