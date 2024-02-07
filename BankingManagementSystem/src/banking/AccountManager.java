package banking;

//package Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	private Connection connection;
	private Scanner scanner;

	public AccountManager(Connection connection, Scanner scanner) {

		this.connection = connection;
		this.scanner = scanner;
	}

	public void credit_money(String account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Amount:");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter Security PIN: ");
		String security_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if (account_number != null) {
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setString(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					String credit_money = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
						PreparedStatement preparedStatement1 = connection.prepareStatement(credit_money);
						preparedStatement1.setDouble(1, amount);
						preparedStatement1.setString(2, account_number);
						int rowsAffected = preparedStatement1.executeUpdate();
						if (rowsAffected > 0) {
							System.out.println("Rs," + amount + "debited successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance");
					}
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void debit_money(String account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Amount:");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter Security PIN: ");
		String security_pin = scanner.nextLine();

		try {
			connection.setAutoCommit(false);
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
			preparedStatement.setString(1, account_number);
			preparedStatement.setString(2, security_pin);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				double current_balance = resultSet.getDouble("balance");
				if (amount <= current_balance) {
					String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
					PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setString(2, account_number);
					int rowsAffected = preparedStatement1.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Rs," + amount + "debited successfully");
						connection.commit();
						connection.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction Failed");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				} else {
					System.out.println("Insufficient Balance");
				}
			}else {
				System.out.println("Invalid PIN");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);

	}
	public void getBalance(String account_number) {
		scanner.nextLine();
		System.out.println("Enter security Pin: ");
		String security_pin = scanner.nextLine();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?");
			preparedStatement.setString(1, account_number);
			preparedStatement.setString(2, security_pin);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				double balance = resultSet.getDouble("balance");
				System.out.println("balance: "+balance);
			}else {
				System.out.println("Invalid Pin!");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void transfer_money(String sender_account_number) throws SQLException{
		scanner.nextLine();
		System.out.println("Enter reciever account number");
		String reciever_account_number = scanner.nextLine();
		System.out.println("Enter amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter security Pin: ");
		String security_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if(sender_account_number !=null && reciever_account_number !=null) {
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setString(1, sender_account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if(amount <= current_balance) {
						String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
						String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
						PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
						PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
						creditPreparedStatement.setDouble(1, amount);
						creditPreparedStatement.setString(2, reciever_account_number);
						debitPreparedStatement.setDouble(1, amount);
						debitPreparedStatement.setString(2, sender_account_number);
						int rowsAffected1 = debitPreparedStatement.executeUpdate();
						int rowsAffected2 = creditPreparedStatement.executeUpdate();
						if(rowsAffected1 > 0 && rowsAffected2 > 0) {
							System.out.println("Transaction Successful");
							System.out.println("Rs "+amount+"Transfered Done");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						}else {
							System.out.println("Transaction Failed");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					}else {
						System.out.println("Invalid Security Pin!");
					}
				}else {
					System.out.println("Invalid account Number");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}

