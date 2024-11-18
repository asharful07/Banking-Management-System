package BankingManagementSystem;

import java.sql.*;
import java.util.Queue;
import java.util.Scanner;

public class Accounts {
    private Scanner scanner;
    private Connection connection;

    public Accounts(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public long open_account(String email){
        if(!account_exit(email)){
            String open_account_query = " insert into accounts(account_number,full_name,email,balance,security_pin) values(?,?,?,?,?)";
            scanner.nextLine();
            System.out.print("Enter full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();

            try{
                long account_number = generateAccountNumber();
                PreparedStatement ps = connection.prepareStatement(open_account_query);
                ps.setLong(1,account_number);
                ps.setString(2,full_name);
                ps.setString(3,email);
                ps.setDouble(4,balance);
                ps.setString(5,security_pin);

                int count = ps.executeUpdate();
                if(count>0){
                    return account_number;
                }else {
                    throw new RuntimeException("Account Creation failed");
                }

            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        throw new RuntimeException("Account Already Exist ");

    }
    public boolean account_exit(String email){
        String query = "select account_number from accounts where email = ?";
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

    private long generateAccountNumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select account_number from accounts order by account_number desc limit 1");

            if(resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }else {
                return 10000100;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public long getAccount_number(String email){
        String query = "select account_number from accounts where email=?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,email);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account number Does not Exit");
    }
}
