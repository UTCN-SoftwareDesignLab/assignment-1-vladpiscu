package repository.account;

import database.DBConnectionFactory;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountRepositoryMySQLTest {

    private static AccountRepository accountRepository;
    private static ClientRepository clientRepository;
    private static Long clientId;

    @BeforeClass
    public static void setUpClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        accountRepository = new AccountRepositoryMySQL(connection);
        clientRepository = new ClientRepositoryMySQL(connection, accountRepository);

    }

    @Before
    public void cleanUp() {
        accountRepository.removeAll();
        Client client = new ClientBuilder()
                .setName("Client")
                .setPnc("123")
                .setCardNb("cardNb")
                .setAddress("address")
                .build();
        clientRepository.save(client);
        clientId = client.getId();
    }

    @After
    public void tearDown() throws Exception {
        clientRepository.removeAll();
    }

    @Test
    public void addAccountToClient() {
        Account account = new AccountBuilder()
                .setType("type")
                .setAmount(0)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        assertTrue(accountRepository.addAccountToClient(clientId, account));
    }

    @Test
    public void updateAccount() {
        Account account = new AccountBuilder()
                .setType("type")
                .setAmount(0)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        accountRepository.addAccountToClient(clientId, account);
        account.setAmount(10);
        assertTrue(accountRepository.updateAccount(account));
    }

    @Test
    public void findAccountsForClient() {
        Account account = new AccountBuilder()
                .setType("type")
                .setAmount(0)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        accountRepository.addAccountToClient(clientId, account);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        assertEquals(accounts, accountRepository.findAccountsForClient(clientId));
        accountRepository.addAccountToClient(clientId, account);
        assertEquals(2, accountRepository.findAccountsForClient(clientId).size());
    }

    @Test
    public void removeAccount() {
        Account account = new AccountBuilder()
                .setType("type")
                .setAmount(0)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        accountRepository.addAccountToClient(clientId, account);
        accountRepository.addAccountToClient(clientId, account);
        accountRepository.addAccountToClient(clientId, account);
        accountRepository.addAccountToClient(clientId, account);
        accountRepository.removeAccount(account.getId());
        assertEquals(3, accountRepository.findAccountsForClient(clientId).size());

    }

    @Test
    public void removeAll() {
        accountRepository.removeAll();
    }
}