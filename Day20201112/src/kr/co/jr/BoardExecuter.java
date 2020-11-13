package kr.co.jr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BoardExecuter {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		try { //�ѹ� �ε��ϰ� ���� �����ϱ� ���� ó���� ����
			// 1.�ε�(���� = �ڹٿ��� ���� �����ͺ��̽��� �� ���ڴ�.)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		
		while (true) { // �ݺ���
			System.out.println("=====�Խ��� �ۼ�=====");
			System.out.println("R:��� S:�˻� D:���� U:���� L:���");
			char protocol = input.next().charAt(0);

			try {
				// 2.����(Connection) DriverManager.getConnection
				conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "khbclass", "dkdlxl");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

			if (protocol == 'r' || protocol == 'R') {// ���
				System.out.print("����|����:");
				String titleContent = input.next();
				int indexI = titleContent.indexOf("|");
				String title = titleContent.substring(0, indexI);
				String content = titleContent.substring(indexI + 1);// 3~�� //�и� ��
				System.out.print("�ۼ���:");
				String author = input.next();
				System.out.print("��¥:");
				String nal = input.next();
				System.out.print("��ȸ��:");
				int readCount = input.nextInt();

				try {
					// 3.�غ�(Statement) 3-1.������ �غ�
					// 3.�غ� 3-2.�����غ�
					Statement stmt = conn.createStatement();
					String sql = "insert into BOARD(no,title,content,author,nal,readCount) values(board_no.nextval,'"
							+ title + "','" + content + "','" + author + "','" + nal + "'," + readCount + ")";
					// 4.���� execute
					int cnt = stmt.executeUpdate(sql);
					System.out.println(cnt + "�� �Խñ��� ��ϵǾ����ϴ�.");
					conn.close(); // �������̽� close
					stmt.close(); // �������̽� close
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} // ���
			
			else if (protocol == 's' || protocol == 'S') {// �˻�
				System.out.print("ã�� �Խñ� ���� �Է� : ");
				String titleSearch = input.next();
				System.out.print("��ȣ\t����\t����\t�ۼ���\t��¥\t��ȸ��\n");
				//3.�غ�
				try {
					Statement stmt = conn.createStatement();
					String sql = "SELECT NO, TITLE, CONTENT,AUTHOR,NAL,READCOUNT FROM BOARD WHERE TITLE = '"+titleSearch+"'";
					ResultSet rs = stmt.executeQuery(sql); //���� ��Ÿ����. executeQuery => select���� �ַ� // executeUpdate => ��� ���� ���� 
					int readCount = 0;
					while(rs.next()) {
						int no = rs.getInt("no");
						String title = rs.getString("title");
						String content = rs.getString("content");
						String author = rs.getString("author");
						String nal = rs.getString("nal");
						readCount = rs.getInt("readCount");
						System.out.print(no+"\t"+title+"\t"+content+"\t"+author+"\t"+nal+"\t"+readCount+"\n");
						readCount++;
					}
					stmt = conn.createStatement();
					sql = "UPDATE BOARD SET readCount = "+readCount+" WHERE title = '"+titleSearch+"'";
					int cnt = stmt.executeUpdate(sql);
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} // �˻�
			
			else if (protocol == 'd' || protocol == 'D') {// ����
				System.out.println("������ �Խù��� ���� : ");
				String titleDelete = input.next();
				try {
					//3.�غ� 3-1. �����غ�
					//3-2. �����غ�
					Statement stmt = conn.createStatement();
					String sql = "delete from board where title='"+titleDelete+"'";
					int cnt = stmt.executeUpdate(sql); //����
					System.out.println(cnt+"�� �Խñ��� �����Ǿ����ϴ�.");
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // ����
			
			else if (protocol == 'u' || protocol == 'U') {// ����
				System.out.print("������ �Խù��� ������ �Է��ϼ��� : ");
				String titleSearch = input.next();
				try {
					//3. ����/���� �غ�
					Statement stmt = conn.createStatement();
					String sql = "SELECT TITLE,CONTENT,AUTHOR,NAL,READCOUNT FROM BOARD WHERE TITLE ='"+titleSearch+"'";
					ResultSet rs = stmt.executeQuery(sql);
					System.out.println("===�����ϱ� �� �Խñ��Դϴ�.===");
					while(rs.next()) {
						String title = rs.getString("title");
						String content = rs.getString("content");
						String author = rs.getString("author");
						String nal = rs.getString("nal");
						int readCount = rs.getInt("readCount");
						System.out.print(title+"\t"+content+"\t"+author+"\t"+nal+"\t"+readCount+"\n");
					}
					System.out.println("������ �����ϰڽ��ϱ�?(Y/N)");
					char option = input.next().charAt(0);
					if(option == 'Y' || option == 'y') {
						System.out.print("����|����:");
						String titleContent = input.next();
						int indexI = titleContent.indexOf("|");
						String titleUpdate = titleContent.substring(0, indexI);
						String contentUpdate = titleContent.substring(indexI + 1);// 3~�� //�и� ��
						System.out.print("�ۼ���:");
						String authorUpdate = input.next();
						System.out.print("��¥:");
						String nalUpdate = input.next();
						System.out.print("��ȸ��:");
						int readCountUpdate = input.nextInt();
						
						stmt = conn.createStatement();
						sql = "UPDATE board SET TITLE = '"+titleUpdate+"',CONTENT = '"+contentUpdate+"', AUTHOR = '"+authorUpdate+"', NAL = '"+nalUpdate+"', READCOUNT = '"+readCountUpdate+"' WHERE TITLE = '"+titleSearch+"'";
						int cnt = stmt.executeUpdate(sql);
						System.out.println(cnt+"�� �Խñ��� �����Ǿ����ϴ�.");
						conn.close();
						stmt.close();
					}
					else {
						conn.close();
						stmt.close();
						continue;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} // ����
			else if (protocol == 'l' || protocol == 'L') {// ��ü���
				System.out.println("===���Խ��� ��ü���===");
				System.out.print("��ȣ\t����\t����\t�ۼ���\t��¥\t��ȸ��\n");
				//3.�غ� 3-1. �����غ�
				//3-2. �����غ�
				try {
					Statement stmt = conn.createStatement();
					String sql = "SELECT NO,TITLE,CONTENT,AUTHOR,NAL,READCOUNT FROM BOARD";
					ResultSet rs = stmt.executeQuery(sql);
					while(rs.next()) {
						int no = rs.getInt("no");
						String title = rs.getString("title");
						String content = rs.getString("content");
						String author = rs.getString("author");
						String nal = rs.getString("nal");
						int readCount = rs.getInt("readCount");
						System.out.print(no+"\t"+title+"\t"+content+"\t"+author+"\t"+nal+"\t"+readCount+"\n");					
					}
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // ��ü���
		} // �ݺ���
	}

}
