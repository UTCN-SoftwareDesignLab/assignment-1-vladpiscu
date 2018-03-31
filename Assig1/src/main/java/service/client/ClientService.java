package service.client;

import model.Client;
import model.validation.Notification;

import java.util.List;

public interface ClientService {
    List<Client> findAll();
    Notification<Boolean> update(Long id, String name, String cardNb, String pnc, String address);
    Notification<Boolean> save(String name, String cardNb, String pnc, String address);
}
