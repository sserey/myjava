package com.company;

import java.sql.*;
import java.io.*;
import java.util.Formatter;

public class Attendance{
	public static void main(String args[]) throws SQLException,Exception{
	
		Connection conn = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost/postgres","postgres","@sA29012000");
			if(conn == null){
				System.out.println("Connection Failed!!!!!");
			}else{
				System.out.println("Connection Successful!!!!");
			}
			final int TOTAL_CONDUCTED_LECTURE = 36;
			System.out.println("How many number of student you want to add: ");
			int no_of_student = Integer.parseInt(br.readLine());
			int roll_no=0;
			String name = null;
			String _class = null;
			//String[] class_array = new String[]{"FY","SY","TY"};
			String subject = null;
			int total_attendance = 0;
			float total_marks=0;
			for(int i = 0;i<no_of_student;i++){
				int roll_no_exist = 0;
				do{
				System.out.print("Enter roll number for student#"+(i+1)+": ");
				roll_no = Integer.parseInt(br.readLine());

				String select_query = "SELECT * FROM attendance";
				statement = conn.createStatement();
				rs = statement.executeQuery(select_query);
				while(rs.next()){
						if(rs.getInt(1) == roll_no){
							roll_no_exist = 1;
							System.out.println("Roll no is already existed");
						}
				}
				//System.out.println(roll_no_exist);


				}while(roll_no_exist==1);
				System.out.print("Enter name for student#"+(i+1)+": ");
				name = br.readLine();
				do {
					System.out.print("Enter class(FY,SY, TY): ");
					_class = br.readLine();
					_class = _class.toUpperCase();
				}while(!_class.equals("FY") && !_class.equals("SY") &&!_class.equals("TY") );
				System.out.print("Enter subject: ");
				subject = br.readLine();
				do {
					System.out.print("Enter total attendance: ");
					total_attendance = Integer.parseInt(br.readLine());
					if(total_attendance <0 || total_attendance>TOTAL_CONDUCTED_LECTURE){
						System.out.println("Attendance shouldn't be less than zero/more than "+TOTAL_CONDUCTED_LECTURE);
					}
					//System.out.println(total_attendance);
				}while((total_attendance < 0) || (total_attendance >TOTAL_CONDUCTED_LECTURE));
				System.out.println(total_attendance);
				System.out.println(TOTAL_CONDUCTED_LECTURE);
				total_marks =((float)total_attendance/(float)TOTAL_CONDUCTED_LECTURE);
				total_marks = total_marks *10;
				//System.out.print(total_marks);
				String insert_query = "INSERT INTO attendance VALUES(?,?,?,?,?,?)";
				preparedStatement = conn.prepareStatement(insert_query);
				preparedStatement.setInt(1,roll_no);
				preparedStatement.setString(2,name);
				preparedStatement.setString(3,_class);
				preparedStatement.setString(4,subject);
				preparedStatement.setInt(5,total_attendance);
				preparedStatement.setInt(6,(int)(total_marks));

				int status = preparedStatement.executeUpdate();
				if(status >0)
					System.out.println("Record inserted Successfully!!!!");
				else
					System.out.println("Failed to insert the record");
			}

			String select_query = "SELECT * FROM attendance";
			statement = conn.createStatement();
			rs = statement.executeQuery(select_query);
			System.out.println("Roll No | Name | Class | Subject | Total Attendance | Marks out of 10 ");
			System.out.println("--------+------+-------+---------+------------------+-----------------");


			while(rs.next()){
				System.out.print("\t"+rs.getInt(1) +" |  ");
					System.out.print(rs.getString(2)+" | ");
					System.out.print(rs.getString(3)+"   |       ");
					System.out.print(rs.getString(4)+"   |       ");
					System.out.print(rs.getInt(5)+"      |    ");
					System.out.print(rs.getInt(6));
				System.out.println();

			}
			conn.close();
		}catch(Exception e){
			System.out.println("Exception"+e);
		}
	}
}
