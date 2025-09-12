package usingjdbc;

import java.sql.*;
import java.util.Scanner;

public class UsingJDBCWithBatchProcessing {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/project";
        String username = "root";
        String password = "root";

        try (Connection c = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to database successfully!");
            Scanner sc = new Scanner(System.in);


            String insertSQL = "INSERT INTO assignment1(id, age, name, position, company) VALUES(?,?,?,?,?)";
            PreparedStatement insertPS = c.prepareStatement(insertSQL);

            String updateSQL = "UPDATE assignment1 SET company=? WHERE id=?";
            PreparedStatement updatePS = c.prepareStatement(updateSQL);

            String deleteSQL = "DELETE FROM assignment1 WHERE id=?";
            PreparedStatement deletePS = c.prepareStatement(deleteSQL);

            while (true) {
                System.out.println("\n****** Select Your Choice ******");
                System.out.println("1. Insert (Batch)");
                System.out.println("2. Read");
                System.out.println("3. Update (Batch)");
                System.out.println("4. Delete (Batch)");
                System.out.println("5. Search A Specific Person");
                System.out.println("6. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                if (choice == 1) {
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Position: ");
                    String position = sc.nextLine();
                    System.out.print("Enter Company: ");
                    String company = sc.nextLine();

                    insertPS.setInt(1, id);
                    insertPS.setInt(2, age);
                    insertPS.setString(3, name);
                    insertPS.setString(4, position);
                    insertPS.setString(5, company);

                    insertPS.addBatch();
                    System.out.println("Insert query added to batch!");
                }

                else if (choice == 2) {
                    String selectSQL = "SELECT * FROM assignment1";
                    try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(selectSQL)) {
                        System.out.println("\n--- Records in Table ---\n");
                        while (rs.next()) {
                            System.out.println(" ID: " + rs.getInt("id"));
                            System.out.println(" Age: " + rs.getInt("age"));
                            System.out.println(" Name: " + rs.getString("name"));
                            System.out.println(" Position: " + rs.getString("position"));
                            System.out.println(" Company: " + rs.getString("company"));
                            System.out.println();
                        }
                    }
                }

                else if (choice == 3) {
                    System.out.print("Enter ID to update: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new Company: ");
                    String company = sc.nextLine();

                    PreparedStatement checkPS = c.prepareStatement("SELECT COUNT(*) FROM assignment1 WHERE id = ?");
                    checkPS.setInt(1, id);
                    ResultSet rs = checkPS.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        updatePS.setString(1, company);
                        updatePS.setInt(2, id);
                        updatePS.addBatch();
                        System.out.println("Update query added to batch!");
                    } else {
                        System.out.println("No record found with ID: " + id);
                    }
                }


                else if (choice == 4) {
                    System.out.print("Enter ID to delete: ");
                    int id = sc.nextInt();

                    deletePS.setInt(1, id);
                    deletePS.addBatch();
                    System.out.println("Delete query added to batch!");
                }

                else if (choice == 5) {
                    System.out.println("Search By: ");
                    System.out.println("1. ID");
                    System.out.println("2. Name");
                    System.out.println("3. Age");
                    System.out.println("4. Position");
                    System.out.println("5. Company");
                    System.out.print("Enter Your Choice To Find The Person: ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine();

                    String sql = "";
                    PreparedStatement ps = null;

                    if (searchChoice == 1) {
                        System.out.print("Enter ID: ");
                        int searchID = sc.nextInt();
                        sql = "SELECT * FROM assignment1 WHERE id = ?";
                        ps = c.prepareStatement(sql);
                        ps.setInt(1, searchID);
                    }
                    else if (searchChoice == 2) {
                        System.out.print("Enter Name: ");
                        String searchName = sc.nextLine();
                        sql = "SELECT * FROM assignment1 WHERE name = ?";
                        ps = c.prepareStatement(sql);
                        ps.setString(1, searchName);
                    }
                    else if (searchChoice == 3) {
                        System.out.print("Enter Age: ");
                        int searchAge = sc.nextInt();
                        sql = "SELECT * FROM assignment1 WHERE age = ?";
                        ps = c.prepareStatement(sql);
                        ps.setInt(1, searchAge);
                    }
                    else if (searchChoice == 4) {
                        System.out.print("Enter Position: ");
                        String searchPosition = sc.nextLine();
                        sql = "SELECT * FROM assignment1 WHERE position = ?";
                        ps = c.prepareStatement(sql);
                        ps.setString(1, searchPosition);
                    }
                    else if (searchChoice == 5) {
                        System.out.print("Enter Company: ");
                        String searchCompany = sc.nextLine();
                        sql = "SELECT * FROM assignment1 WHERE company = ?";
                        ps = c.prepareStatement(sql);
                        ps.setString(1, searchCompany);
                    }
                    else {
                        System.out.println("Invalid choice!");
                        continue;
                    }

                    ResultSet rs = ps.executeQuery();
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("\n--- Person Found ---");
                        System.out.println(" ID: " + rs.getInt("id"));
                        System.out.println(" Age: " + rs.getInt("age"));
                        System.out.println(" Name: " + rs.getString("name"));
                        System.out.println(" Position: " + rs.getString("position"));
                        System.out.println(" Company: " + rs.getString("company"));
                        System.out.println("----------------------");
                    }
                    if (!found) {
                        System.out.println("No matching record found!");
                    }
                }

                else if (choice == 6) {
                    System.out.println("Goodbye!");
                    break;
                }

                else {
                    System.out.println("Invalid choice!");
                }
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
    }
}
