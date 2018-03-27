package repository.account;

import model.Account;
import model.builder.AccountBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;

public class AccountRepositoryMySQL implements AccountRepository {
    private Connection connection;

    public AccountRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public boolean addAccountToClient(Long clientId, Account account){
        try {
            PreparedStatement insertAccountStatement = connection
                    .prepareStatement("INSERT INTO account values (null, ?, ?, ?, ?)");
            insertAccountStatement.setLong(1, clientId);
            insertAccountStatement.setString(2, account.getType());
            insertAccountStatement.setInt(3, account.getAmount());
            insertAccountStatement.setDate(4, account.getCreationDate());
            insertAccountStatement.executeUpdate();

            ResultSet rs = insertAccountStatement.getGeneratedKeys();
            rs.next();
            long accountId = rs.getLong(1);
            account.setId(accountId);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean updateAccount(Account account) {
        try {
            PreparedStatement updateAccountStatement = connection
                    .prepareStatement("UPDATE account SET account.type=?, amount=?, creationDate=? WHERE id=?");
            updateAccountStatement.setString(1, account.getType());
            updateAccountStatement.setInt(2, account.getAmount());
            updateAccountStatement.setDate(3, account.getCreationDate());
            updateAccountStatement.setLong(4, account.getId());
            updateAccountStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Account> findAccountsForClient(Long clientId) {
        List<Account> accounts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String fetchAccountsSql = "Select * from `" + ACCOUNT + "` where `client_id`=\'" + clientId + "\'";
            ResultSet accountsResultSet = statement.executeQuery(fetchAccountsSql);
            while (accountsResultSet.next()) {
                Account account = new AccountBuilder()
                        .setId(accountsResultSet.getLong("id"))
                        .setType(accountsResultSet.getString("type"))
                        .setAmount(accountsResultSet.getInt("amount"))
                        .setCreationDate(accountsResultSet.getDate("creationDate"))
                        .build();
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean removeAccount(Long accountId) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from account where id = \'" + accountId + "\'";
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeAll(){
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from account where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
