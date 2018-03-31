package service.account;

import database.DBConnectionFactory;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountServiceMySQLTest {
    private static AccountService accountService;
    private static AccountRepository accountRepository;
    private static ClientRepository clientRepository;
    private Long clientId;

    @BeforeClass
    public static void setUpClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        accountRepository = new AccountRepositoryMySQL(connection);
        clientRepository = new ClientRepositoryMySQL(connection, accountRepository);
        accountService = new AccountServiceMySQL(accountRepository);

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
    public void getAccountsForClient() {
        Account account = new AccountBuilder()
                .setType("type")
                .setAmount(0)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        accountRepository.addAccountToClient(clientId, account);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        assertEquals(accounts, accountService.getAccountsForClient(clientId));
        accountRepository.addAccountToClient(clientId, account);
        assertEquals(2, accountService.getAccountsForClient(clientId).size());
    }

    @Test
    public void addAccountForClient() {
        LocalDate date = LocalDate.now();
        assertTrue(accountService.addAccountForClient(clientId, "type", 0, date).getResult());
        assertTrue(accountService.addAccountForClient(clientId, "type", -1, date).hasErrors());
    }

    @Test
    public void updateAccount() {
        LocalDate date = LocalDate.now();
        Account account = new AccountBuilder()
                .setType("type")
                .setAmount(0)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        accountRepository.addAccountToClient(clientId, account);
        assertTrue(accountService.updateAccount(account.getId(), "type", 10, date).getResult());
        assertTrue(accountService.updateAccount(account.getId(), "type", -5, date).hasErrors());
    }

    @Test
    public void transferMoneyBetweenAccounts() {
        Account srcAccount = new AccountBuilder()
                .setType("type")
                .setAmount(50)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        Account dstAccount = new AccountBuilder()
                .setType("type")
                .setAmount(0)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        accountRepository.addAccountToClient(clientId, srcAccount);
        accountRepository.addAccountToClient(clientId, dstAccount);
        assertTrue(accountService.transferMoneyBetweenAccounts(srcAccount, dstAccount, 30).getResult());
        assertEquals(20, srcAccount.getAmount());
        assertEquals(30, dstAccount.getAmount());
        assertTrue(accountService.transferMoneyBetweenAccounts(srcAccount, dstAccount, 30).hasErrors());
        assertEquals(20, srcAccount.getAmount());
        assertEquals(30, dstAccount.getAmount());
    }

    @Test
    public void payBills() {
        LocalDate date = LocalDate.now();
        Account account = new AccountBuilder()
                .setType("type")
                .setAmount(30)
                .setCreationDate(new Date(2017, 5, 10))
                .build();
        accountRepository.addAccountToClient(clientId, account);
        assertTrue(accountService.payBills(account.getId(), account.getType(), account.getAmount(), date, 20).getResult());
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
        accountService.removeAccount(account.getId());
        assertEquals(3, accountRepository.findAccountsForClient(clientId).size());
    }
}