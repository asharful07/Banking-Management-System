package BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final  String url = "jdbc:mysql://localhost:3306/banking_system?useSSL=false";
    private static final String user_name = "root";
    private static final String user_password = "123456";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url,user_name,user_password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection,scanner);
            Accounts accounts = new Accounts(connection,scanner);
            AccountManager accountManager = new AccountManager(connection,scanner);
            String email;
            long account_number;
            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice1 = scanner.nextInt();

                switch (choice1) {
                    case 1:
                        user.register();
                        break;

                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");

                            if (!accounts.account_exit(email)) {
                                System.out.println();
                                System.out.println("1. Open an Account ");
                                System.out.println("2. Exit");
                                int openAccountChoice = scanner.nextInt();
                                if (openAccountChoice == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account created successfully.");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;  // Exit the loop if the user chooses not to open an account
                                }
                            }

                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;

                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");
                                choice2 = scanner.nextInt();  // Use choice2 here instead of choice1

                                switch (choice2) {  // Use choice2 here as well
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        System.out.println("Logging out...");
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice.");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect email or password!");
                        }
                        break;

                    case 3:
                        System.out.println("Thank you for using the banking system!!!");
                        System.out.println("Exiting system!");
                        return;

                    default:
                        System.out.print("Enter a valid choice.");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
