package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;
    public User(Connection connection,Scanner scanner){
        this.connection= connection;
        this.scanner=scanner;
    }

    // User Register

    public void register(){
        scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if(user_exit(email)){
            System.out.println("user already Exits for this Email Address!!");
            return;
        }
        String register_query = "insert  into user(full_name,email,password) values(?,?,?)";
        try{
            PreparedStatement ps = connection.prepareStatement(register_query);
            ps.setString(1,full_name);
            ps.setString(2,email);
            ps.setString(3,password);
            int count = ps.executeUpdate();
            if(count>0){
                System.out.println("registration Successful!");
            }else {
                System.out.println("Registration failed!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String login_query = " select * from user where email = ? and password = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(login_query);
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return email;
            }else {
                return null;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exit(String email){
        String query = "select * from user where email =?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,email);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
