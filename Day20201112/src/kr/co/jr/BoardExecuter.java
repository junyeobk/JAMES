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
		try { //한번 로드하고 연결 유지하기 위해 처음에 선언
			// 1.로드(적재 = 자바에게 내가 데이터베이스를 뭘 쓰겠다.)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;
		
		while (true) { // 반복문
			System.out.println("=====게시판 작성=====");
			System.out.println("R:등록 S:검색 D:삭제 U:수정 L:목록");
			char protocol = input.next().charAt(0);

			try {
				// 2.연결(Connection) DriverManager.getConnection
				conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "khbclass", "dkdlxl");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

			if (protocol == 'r' || protocol == 'R') {// 등록
				System.out.print("제목|내용:");
				String titleContent = input.next();
				int indexI = titleContent.indexOf("|");
				String title = titleContent.substring(0, indexI);
				String content = titleContent.substring(indexI + 1);// 3~끝 //분리 끝
				System.out.print("작성자:");
				String author = input.next();
				System.out.print("날짜:");
				String nal = input.next();
				System.out.print("조회수:");
				int readCount = input.nextInt();

				try {
					// 3.준비(Statement) 3-1.공간을 준비
					// 3.준비 3-2.쿼리준비
					Statement stmt = conn.createStatement();
					String sql = "insert into BOARD(no,title,content,author,nal,readCount) values(board_no.nextval,'"
							+ title + "','" + content + "','" + author + "','" + nal + "'," + readCount + ")";
					// 4.실행 execute
					int cnt = stmt.executeUpdate(sql);
					System.out.println(cnt + "건 게시글이 등록되었습니다.");
					conn.close(); // 인터페이스 close
					stmt.close(); // 인터페이스 close
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} // 등록
			
			else if (protocol == 's' || protocol == 'S') {// 검색
				System.out.print("찾는 게시글 제목 입력 : ");
				String titleSearch = input.next();
				System.out.print("번호\t제목\t내용\t작성자\t날짜\t조회수\n");
				//3.준비
				try {
					Statement stmt = conn.createStatement();
					String sql = "SELECT NO, TITLE, CONTENT,AUTHOR,NAL,READCOUNT FROM BOARD WHERE TITLE = '"+titleSearch+"'";
					ResultSet rs = stmt.executeQuery(sql); //행을 나타낸다. executeQuery => select에서 주력 // executeUpdate => 등록 삭제 수정 
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
				
			} // 검색
			
			else if (protocol == 'd' || protocol == 'D') {// 삭제
				System.out.println("삭제할 게시물의 제목 : ");
				String titleDelete = input.next();
				try {
					//3.준비 3-1. 공간준비
					//3-2. 쿼리준비
					Statement stmt = conn.createStatement();
					String sql = "delete from board where title='"+titleDelete+"'";
					int cnt = stmt.executeUpdate(sql); //실행
					System.out.println(cnt+"건 게시글이 삭제되었습니다.");
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // 삭제
			
			else if (protocol == 'u' || protocol == 'U') {// 수정
				System.out.print("변경할 게시물의 제목을 입력하세요 : ");
				String titleSearch = input.next();
				try {
					//3. 공간/쿼리 준비
					Statement stmt = conn.createStatement();
					String sql = "SELECT TITLE,CONTENT,AUTHOR,NAL,READCOUNT FROM BOARD WHERE TITLE ='"+titleSearch+"'";
					ResultSet rs = stmt.executeQuery(sql);
					System.out.println("===변경하기 전 게시글입니다.===");
					while(rs.next()) {
						String title = rs.getString("title");
						String content = rs.getString("content");
						String author = rs.getString("author");
						String nal = rs.getString("nal");
						int readCount = rs.getInt("readCount");
						System.out.print(title+"\t"+content+"\t"+author+"\t"+nal+"\t"+readCount+"\n");
					}
					System.out.println("정말로 수정하겠습니까?(Y/N)");
					char option = input.next().charAt(0);
					if(option == 'Y' || option == 'y') {
						System.out.print("제목|내용:");
						String titleContent = input.next();
						int indexI = titleContent.indexOf("|");
						String titleUpdate = titleContent.substring(0, indexI);
						String contentUpdate = titleContent.substring(indexI + 1);// 3~끝 //분리 끝
						System.out.print("작성자:");
						String authorUpdate = input.next();
						System.out.print("날짜:");
						String nalUpdate = input.next();
						System.out.print("조회수:");
						int readCountUpdate = input.nextInt();
						
						stmt = conn.createStatement();
						sql = "UPDATE board SET TITLE = '"+titleUpdate+"',CONTENT = '"+contentUpdate+"', AUTHOR = '"+authorUpdate+"', NAL = '"+nalUpdate+"', READCOUNT = '"+readCountUpdate+"' WHERE TITLE = '"+titleSearch+"'";
						int cnt = stmt.executeUpdate(sql);
						System.out.println(cnt+"건 게시글이 수정되었습니다.");
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

			} // 수정
			else if (protocol == 'l' || protocol == 'L') {// 전체출력
				System.out.println("===ㄱ게시판 전체출력===");
				System.out.print("번호\t제목\t내용\t작성자\t날짜\t조회수\n");
				//3.준비 3-1. 공간준비
				//3-2. 쿼리준비
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
			} // 전체출력
		} // 반복문
	}

}
