package repository.client;

import model.Client;
import model.validation.Notification;

import java.util.List;

public interface ClientRepository {
    List<Client> findAll();

    Notification<Client> findByPNC(String pnc);

    boolean save(Client client);

    boolean update(Client client);

    void removeAll();


}
