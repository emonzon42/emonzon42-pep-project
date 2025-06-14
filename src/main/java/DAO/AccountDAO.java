package DAO;

import java.sql.*;
import java.util.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    private Connection conn; //db connection set on initalization

    public AccountDAO(){
        conn = ConnectionUtil.getConnection();
    }
    
    public List<Account> getAllAccounts(){
        List<Account> accounts = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM account;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account Account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(Account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account insertAccount(Account acc){
        try {
//          Write SQL logic here. You should only be inserting with the name column, so that the database may
//          automatically generate a primary key.

            String sql = "INSERT INTO account (username, password) VALUES (?,?);" ;
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, acc.getUsername());
            preparedStatement.setString(2, acc.getPassword());

            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_Account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_Account_id, acc.getUsername(), acc.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByUsername(String username){
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account acc = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return acc;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccount(Account acc){

        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, acc.getUsername());
            preparedStatement.setString(2, acc.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
}
