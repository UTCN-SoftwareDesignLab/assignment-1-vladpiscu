package service.client;

import database.DBConnectionFactory;
import model.Client;
import model.builder.ClientBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;

import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;

public class ClientServiceMySQLTest {
    private static ClientRepository clientRepository;
    private static ClientService clientService;

    @BeforeClass
    public static void setUpClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        AccountRepository accountRepository = new AccountRepositoryMySQL(connection);
        clientRepository = new ClientRepositoryMySQL(connection, accountRepository);
        clientService = new ClientServiceMySQL(clientRepository);
    }

    @Before
    public void cleanUp() {
        clientRepository.removeAll();
    }

    @Test
    public void findAll() {
        Client client1 = new ClientBuilder()
                .setName("Client")
                .setPnc("123")
                .setCardNb("cardNb")
                .setAddress("address")
                .build();
        Client client2 = new ClientBuilder()
                .setName("Client")
                .setPnc("234")
                .setCardNb("cardNb")
                .setAddress("address")
                .build();
        clientRepository.save(client1);
        clientRepository.save(client2);

        List<Client> clients = clientService.findAll();
        assertEquals(2, clients.size());
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
        assertTrue(clientService.update(client.getId(), client.getName(), client.getCardNb(), client.getPnc(), client.getAddress()).getResult());
    }

    @Test
    public void save() {
        assertTrue(clientService.save("PncTest","SaveTest","cardNb","address").hasErrors());
        assertTrue(clientService.save("PncTest","SaveTest","123","address").getResult());
    }
}