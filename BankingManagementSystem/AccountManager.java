package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Credit money
    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement ps = connection.prepareStatement("select * from accounts where account_number =? and  security_pin = ?");
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String credit_query = "update accounts set balance = balance + ? where account_number=?";
                        PreparedStatement ps1 = connection.prepareStatement(credit_query);
                        ps1.setDouble(1, amount);
                        ps1.setLong(2, account_number);

                        int count = ps1.executeUpdate();
                        if (count > 0) {
                            System.out.println("Rs." + amount + " Credit Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.print("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                            return;
                        }

                    }else {
                        System.out.println("Invalid security pin!");
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    // Debit money
    public void debit_money(long account_number) {
        scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false); // Disable auto-commit to handle transactions manually

            if (account_number != 0) {
                // Check if the account exists and validate the security pin
                String query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");

                    // Check if there is sufficient balance
                    if (amount <= current_balance) {
                        // Perform the debit operation
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement ps1 = connection.prepareStatement(debit_query);
                        ps1.setDouble(1, amount);
                        ps1.setLong(2, account_number);

                        int count = ps1.executeUpdate();
                        if (count > 0) {
                            System.out.println("Rs. " + amount + " debited successfully.");
                            connection.commit(); // Commit the transaction
                        } else {
                            System.out.println("Transaction failed.");
                            connection.rollback(); // Rollback the transaction in case of failure
                        }
                    } else {
                        System.out.println("Insufficient balance!");
                    }
                } else {
                    System.out.println("Invalid account number or security pin!");
                }
            } else {
                System.out.println("Invalid account number!");
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback the transaction in case of an exception
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Balance
    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try{
            PreparedStatement ps = connection.prepareStatement("select balance from accounts where account_number = ? and security_pin =?");
            ps.setLong(1,account_number);
            ps.setString(2,security_pin);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
             double balance = resultSet.getDouble("balance");
                System.out.print("Balance: "+balance);
            }else {
                System.out.print("Invalid pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    // Transfer money
    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.println("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try
        {
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement ps = connection.prepareStatement("select * from accounts where account_number =? and security_pin =? ");
                ps.setLong(1,sender_account_number);
                ps.setString(2,security_pin);
                ResultSet resultSet = ps.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("Balance");
                    if(amount<=current_balance){
                        String debit_query = "update accounts set balance = balance -? where account_number=?";
                        String credit_query = "update accounts set balance = balance-? where account_number=?";
                        PreparedStatement credit_ps = connection.prepareStatement(credit_query);
                        PreparedStatement debit_ps = connection.prepareStatement(debit_query);
                        credit_ps.setDouble(1,amount);
                        credit_ps.setLong(2,receiver_account_number);
                        debit_ps.setDouble(1,amount);
                        debit_ps.setLong(2,sender_account_number);

                        int credit_count = credit_ps.executeUpdate();
                        int debit_count = debit_ps.executeUpdate();

                        if(credit_count>0 && debit_count>0){
                            System.out.println("Transaction Successfully");
                            System.out.println("Rs."+amount+"Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                            }
                        }else {
                        System.out.println("Transaction Failed ");
                        connection.rollback();
                        connection.setAutoCommit(true);
                        }

                    }


                }

            }catch (SQLException e){
            e.printStackTrace();
        }

    }
}










