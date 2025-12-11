package emp;

import java.sql.*;  
import java.util.*;
 
public class PayrollCalc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		int ch;
		do
		{
			//enter operation choice
			System.out.println("======EMPLOYEE PAYROLL CALCULATOR=======");
			System.out.println("Enter 1. to add Employee");
			System.out.println("Enter 2. to calculate salary");
			System.out.println("Enter 3. to delete employee");
			System.out.println("Enter 4. to generate pay slip");
			System.out.println("Enter 5. to view all employee list");
			System.out.println("Enter 0. to exit application");
			ch= sc.nextInt();
			switch(ch) {
			case 1:
				addEmployee();
				break;
			case 2:
				 calculateSalary();
				 break;
			case 3:
				deleteEmployee();
				break;
				
			case 4:
				generatePaySlip();
				break;
				
			case 5:
				viewEmployeeDetails();
				break;
				
			case 0:
				System.out.println("Exit.....");
				break;
				default:
					System.out.println("enter valid  integer for operation ");
			}
			}while(ch!=0);
			
			
		

	}
	static void addEmployee() {
	    Scanner sc1 = new Scanner(System.in);
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/epc", "root", "*******");

	        System.out.println("Enter Employee Name");
	        String name = sc1.nextLine();
	        System.out.println("Enter Employee Department");
	        String dept = sc1.nextLine();
	        System.out.println("Enter Basic Salary");
	        double bs = sc1.nextDouble();
	        System.out.println("Enter HRA");
	        double hra = sc1.nextDouble();
	        System.out.println("Enter DA");
	        double da = sc1.nextDouble();
	        System.out.println("Enter Deductions");
	        double deduc = sc1.nextDouble();
   // insert emp into employee table
	        String sql = "INSERT INTO Emp_detail(emp_name, dept, basic_salary, hra, da, deduction) VALUES (?, ?, ?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, name);
	        ps.setString(2, dept);
	        ps.setDouble(3, bs);
	        ps.setDouble(4, hra);
	        ps.setDouble(5, da);
	        ps.setDouble(6, deduc);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println("Employee Added Successfully");

	            String sql2 = "SELECT * FROM Emp_detail ORDER BY id DESC LIMIT 1";
	            PreparedStatement ps1 = con.prepareStatement(sql2);
	            ResultSet rs = ps1.executeQuery();

	            if (rs.next()) {
	                System.out.println("\n Employee's Details:");
	                System.out.println("ID: " + rs.getInt("id"));
	                System.out.println("Name: " + rs.getString("emp_name"));
	                System.out.println("Dept: " + rs.getString("dept"));
	                System.out.println("Basic: " + rs.getDouble("basic_salary"));
	                System.out.println("HRA: " + rs.getDouble("hra"));
	                System.out.println("DA: " + rs.getDouble("da"));
	                System.out.println("Deduction: " + rs.getDouble("deduction"));
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	static void calculateSalary() {
	    Scanner sc2 = new Scanner(System.in);
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/epc", "root", "******"  );   

	        System.out.println("Enter Employee id:");
	        int id = sc2.nextInt();
	        PreparedStatement ps = con.prepareStatement(
	            "SELECT basic_salary, hra, da, deduction FROM Emp_detail WHERE id=?"
	        );
	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();

	        if(rs.next()) {
	            double gs = rs.getDouble("basic_salary") 
	                      + rs.getDouble("hra") 
	                      + rs.getDouble("da");

	            double net_sal = gs - rs.getDouble("deduction");
	            System.out.println("Gross Salary: " + gs);
	            System.out.println("Net Salary: " + net_sal);

	   
	            PreparedStatement ps1 = con.prepareStatement(
	                "INSERT INTO Payroll(emp_id, basic_salary, hra, da, deduction,gross_salary, net_salary, pay_date) "
	                + "VALUES(?, ?, ?, ?, ?, ?,?, CURDATE())"
	            );
	            ps1.setInt(1, id);
	            ps1.setDouble(2, rs.getDouble("basic_salary"));
	            ps1.setDouble(3, rs.getDouble("hra"));
	            ps1.setDouble(4, rs.getDouble("da"));
	            ps1.setDouble(5, rs.getDouble("deduction"));
	            ps1.setDouble(6,gs);
	            ps1.setDouble(7, net_sal);

	            int rows = ps1.executeUpdate();
	            if(rows > 0) {
	                System.out.println("Payroll saved successfully!");
	            }

	            ps1.close();
	        } else {
	            System.out.println("Employee not found!");
	        }

	        rs.close();
	        ps.close();
	        con.close();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}

	static void deleteEmployee() {
	    Scanner sc2 = new Scanner(System.in);

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/epc", "root", "*******"
	        );

	        System.out.println("Enter Employee ID:");
	        int id = sc2.nextInt();

	        // First delete payroll records
	        PreparedStatement ps1 = con.prepareStatement(
	            "DELETE FROM payroll WHERE emp_id = ?"
	        );
	        ps1.setInt(1, id);
	        ps1.executeUpdate();

	        // Then delete employee
	        PreparedStatement ps2 = con.prepareStatement(
	            "DELETE FROM Emp_detail WHERE id = ?"
	        );
	        ps2.setInt(1, id);
	        int rows = ps2.executeUpdate();

	        if (rows > 0)
	            System.out.println("Employee deleted successfully!");
	        else
	            System.out.println("Employee not found!");

	        ps1.close();
	        ps2.close();
	        con.close();
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	    }
	}

	    
	static void generatePaySlip() {
	    Scanner sc = new Scanner(System.in);

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/epc", "root", "******");

	        System.out.println("Enter Employee ID:");
	        int id = sc.nextInt();

	       
	        PreparedStatement ps = con.prepareStatement(
	            "SELECT emp_name, dept FROM Emp_detail WHERE id=?");
	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            System.out.println("Employee not found!");
	            return;
	        }

	        String emp_name = rs.getString("emp_name");
	        String dept = rs.getString("dept");

	 
	        PreparedStatement ps1 = con.prepareStatement(
	            "SELECT * FROM Payroll WHERE emp_id=? ");
	        ps1.setInt(1, id);
	        ResultSet rs1 = ps1.executeQuery();

	        if (rs1.next()) {
	            System.out.println("\n========== SALARY SLIP ==========");
	            System.out.println("Pay ID          : " + rs1.getInt("pay_id"));
	            System.out.println("Employee Name   : " + emp_name);
	            System.out.println("Department      : " + dept);
	            System.out.println("---------------------------------");
	            System.out.println("Basic Salary    : " + rs1.getDouble("basic_salary"));
	            System.out.println("HRA             : " + rs1.getDouble("hra"));
	            System.out.println("DA              : " + rs1.getDouble("da"));
	            System.out.println("Deduction       : " + rs1.getDouble("deduction"));
	            System.out.println("---------------------------------");
	            System.out.println("Gross Salary    : " + rs1.getDouble("gross_salary"));
	            System.out.println("Net Salary      : " + rs1.getDouble("net_salary"));
	            System.out.println("Pay Date        : " + rs1.getDate("pay_date"));
	            System.out.println("=================================\n");
	        } else {
	            System.out.println("No Payroll Found for this Employee!");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	static void viewEmployeeDetails()
	{try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/epc", "root", "******"
        );  
        PreparedStatement ps=con.prepareStatement("select *from Emp_detail");
        ResultSet rs=ps.executeQuery();
       
        while (rs.next()) {
            System.out.println("ID           : " + rs.getInt("id"));
            System.out.println("Name         : " + rs.getString("emp_name"));
            System.out.println("Department   : " + rs.getString("dept"));
            System.out.println("Basic Salary : " + rs.getDouble("basic_salary"));
            System.out.println("HRA          : " + rs.getDouble("hra"));
            System.out.println("DA           : " + rs.getDouble("da"));
            System.out.println("Deduction    : " + rs.getDouble("deduction"));
            System.out.println("--------------------------------------");
        }
        
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}

		
		
	}

}
