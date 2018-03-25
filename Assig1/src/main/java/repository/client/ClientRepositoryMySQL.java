package repository.client;

import model.Client;
import model.User;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.account.AccountRepository;
import repository.user.AuthenticationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.CLIENT;
import static database.Constants.Tables.USER;

public class ClientRepositoryMySQL implements ClientRepository {
    private final Connection connection;
    private final AccountRepository accountRepository;

    public ClientRepositoryMySQL(Connection connection, AccountRepository accountRepository)
    {
        this.connection = connection;
        this.accountRepository = accountRepository;
    }


    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "Select * from client";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                clients.add(getClientFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public Notification<Client> findByPNC(String pnc) {
        Notification<Client> findByPNCNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + CLIENT + "` where `pnc`=\'" + pnc + "\'";
            ResultSet clientResultSet = statement.executeQuery(fetchUserSql);
            if (clientResultSet.next()) {
                Client client = new ClientBuilder()
                        .setId(clientResultSet.getLong("id"))
                        .setName(clientResultSet.getString("name"))
                        .setPnc(clientResultSet.getString("pnc"))
                        .setCardNb(clientResultSet.getString("cardNb"))
                        .setAddress(clientResultSet.getString("address"))
                        .setAccounts(accountRepository.findAccountsForClient(clientResultSet.getLong("id")))
                        .build();
                findByPNCNotification.setResult(client);
                return findByPNCNotification;
            } else {
                findByPNCNotification.addError("Client doesn't exist!\n");
                return findByPNCNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(Client client) {
        try {
            PreparedStatement insertClientStatement = connection
                    .prepareStatement("INSERT INTO client values (null, ?, ?, ?, ?)");
            insertClientStatement.setString(1, client.getName());
            insertClientStatement.setString(2, client.getCardNb());
            insertClientStatement.setString(3, client.getPnc());
            insertClientStatement.setString(4, client.getAddress());
            insertClientStatement.executeUpdate();

            ResultSet rs = insertClientStatement.getGeneratedKeys();
            rs.next();
            long clientId = rs.getLong(1);
            client.setId(clientId);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Client client) {
        try {
            PreparedStatement insertClientStatement = connection
                    .prepareStatement("UPDATE client SET client.name=?, cardNb=?, pnc=?, address=? WHERE id=?");
            insertClientStatement.setString(1, client.getName());
            insertClientStatement.setString(2, client.getCardNb());
            insertClientStatement.setString(3, client.getPnc());
            insertClientStatement.setString(4, client.getAddress());
            insertClientStatement.setLong(5, client.getId());
            insertClientStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            accountRepository.removeAll();
            String sql = "DELETE from client where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Client getClientFromResultSet(ResultSet rs) throws SQLException {
        return new ClientBuilder()
                .setId(rs.getLong("id"))
                .setName(rs.getString("name"))
                .setPnc(rs.getString("pnc"))
                .setAddress(rs.getString("address"))
                .setCardNb(rs.getString("cardNb"))
                .build();
    }
}
