package banking;

import java.sql.*;
import java.util.Scanner;
import java.util.UUID;

public class Accounts {

	private Connection connection;
	private Scanner scanner;

	public Accounts(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public String open_account(String email) {

		if (!account_exist(email)) {
			String open_account_query = "INSERT INTO accounts(account_number, full_name, email , balance, security_pin) VALUES (?, ?, ?, ?, ?)";
			scanner.nextLine();
			System.out.println("Enter full Name");
			String full_name = scanner.nextLine();
			System.out.println("Enter Initial amount");
			double balance  = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter PIN");
			String security_pin = scanner.nextLine();
			try {
				String account_number = generateAccountNumber();
				PreparedStatement prepsredStatement = connection.prepareStatement(open_account_query);
				prepsredStatement.setString(1, account_number);
				prepsredStatement.setString(2, full_name);
				prepsredStatement.setString(3, email);
				prepsredStatement.setDouble(4, balance);
				prepsredStatement.setString(5, security_pin);
				int rowAffected = prepsredStatement.executeUpdate();
				if(rowAffected > 0) {
					return account_number;
				}else {
					throw new RuntimeException("Account Creation failed");
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		
		}
		throw new RuntimeException("Account Already Exist");
	}
	
	public boolean  account_exist(String email) {
		String query = "SELECT account_number FROM accounts WHERE email = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true;
			}else {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String generateAccountNumber() {
//		try {
//			Statement statement  = connection.createStatement();
//			ResultSet resultSet  = statement.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
//			if(resultSet.next()) {
//				long last_account_number = resultSet.getLong("account_number");
//				return last_account_number+1;
//			}else {
//				return 1000100;
//			}
//		}catch(SQLException e) {
//			e.printStackTrace();
//		}
	    return UUID.randomUUID().toString().replace("-", ""); 

//		return 1000100;
	}
	
	public String getAccount_number(String email) {
		String query = "SELECT account_number FROM accounts WHERE email = ?";
		try {
			PreparedStatement preparedStatement  = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return resultSet.getString("account_number");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Account number does not exist");
	}

	
	
}

