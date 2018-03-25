package repository.client;

import database.DBConnectionFactory;
import database.JDBConnectionWrapper;
import model.Client;
import model.builder.ClientBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ClientRepositoryMySQLTest {
    private static ClientRepository clientRepository;

    @BeforeClass
    public static void setUpClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        AccountRepository accountRepository = new AccountRepositoryMySQL(connection);
        clientRepository = new ClientRepositoryMySQL(connection, accountRepository);
    }

    @Before
    public void cleanUp() {
        clientRepository.removeAll();
    }

    @Test
    public void findAll() {
        List<Client> clients = clientRepository.findAll();
        assertEquals(0, clients.size());
    }

    @Test
    public void findAllWhenDbNotEmpty() throws Exception {
        Client client = new ClientBuilder()
                .setName("Client")
                .setPnc("123")
                .setCardNb("cardNb")
                .setAddress("address")
                .build();
        clientRepository.save(client);
        clientRepository.save(client);
        clientRepository.save(client);

        List<Client> clients = clientRepository.findAll();
        assertEquals(3, clients.size());
    }

    @Test
    public void findByPNC() {
        Client client = new ClientBuilder()
                .setName("PncTest")
                .setPnc("456")
                .setCardNb("cardNb")
                .setAddress("address")
                .build();
        clientRepository.save(client);
        Client result = clientRepository.findByPNC("456").getResult();

        assertEquals(client, result);


    }

    @Test
    public void save() {
        assertTrue(clientRepository.save(
                new ClientBuilder()
                        .setName("PncTest")
                        .setPnc("SaveTest")
                        .setCardNb("cardNb")
                        .setAddress("address")
                        .build()));
    }

    @Test
    public void update() {
        Client client = new ClientBuilder()
                .setName("Client")
                .setPnc("123")
                .setCardNb("cardNb")
                .setAddress("address")
                .build();
        clientRepository.save(client);
        client.setName("UpdatedClient");
        assertTrue(clientRepository.update(client));
    }

    @Test
    public void removeAll() {
        clientRepository.removeAll();
        assertEquals(0, clientRepository.findAll().size());
    }
}