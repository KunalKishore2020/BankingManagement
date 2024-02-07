package banking;

import java.sql.Connection;
import java.sql.DriverManager;


import java.util.*;

public class BankingApp {
	private static final String url = "jdbc:mysql://localhost:3306/banking_system";
	private static final String userName = "root";
	private static final String password = "";

	public static void main(String[] args) throws ClassNotFoundException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection connection = DriverManager.getConnection(url, userName, password);
			Scanner scanner = new Scanner(System.in);
			User user = new User(connection, scanner);
			Accounts accounts = new Accounts(connection, scanner);
			AccountManager accountManager = new AccountManager(connection, scanner);

			String email;
			String account_number;

			while (true) {
				System.out.println("***WELCOME TO BANK***");
				System.out.println();
				System.out.println("1. Registor");
				System.out.println("2. LogIn");
				System.out.println("3. Exit");

				System.out.println("Enter your choice: ");
				int choice1 = scanner.nextInt();
				switch (choice1) {
				case 1: {
					user.registor();
					break;
				}
				case 2: {
					email = user.login();
					if (email != null) {
						System.out.println();
						System.out.println("User Loged IN!");
						if (!accounts.account_exist(email)) {
							System.out.println();
							System.out.println("1: Open an bank account");
							System.out.println("2: Exit");
							if (scanner.nextInt() == 1) {
								account_number = accounts.open_account(email);
								System.out.println("Account created successfully");
								System.out.println("Your account number is :" + account_number);
							} else {
								break;
							}
						}
						account_number = accounts.getAccount_number(email);
						int choice2 = 0;
						while (choice2 != 5) {
							System.out.println();
							System.out.println("1. Debit Money");
							System.out.println("2. credit Money");
							System.out.println("3. Transfer Money");
							System.out.println("4. check balance");
							System.out.println("5. Log Out");
							System.out.println("Enter your choice: ");
							choice2 = scanner.nextInt();

							switch (choice2) {
							case 1: {
								accountManager.debit_money(account_number);
								break;
							}
							case 2: {
								accountManager.credit_money(account_number);
								break;
							}
							case 3: {
								accountManager.transfer_money(account_number);
								break;
							}
							case 4: {
								accountManager.getBalance(account_number);
								break;
							}
							case 5: {
								break;
							}
							default:
								throw new IllegalArgumentException("Unexpected value: " + choice2);
							}
						}
					}
				}
				case 3: {
					System.out.println("Thank you for using BANKING SYSTEM!!!!!!");
					System.out.println("Exiting system");
					return;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + choice1);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

