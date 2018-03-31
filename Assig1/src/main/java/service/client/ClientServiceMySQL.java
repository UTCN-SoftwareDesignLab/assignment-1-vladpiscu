package service.client;

import model.Client;
import model.User;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import model.validation.ClientValidator;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.client.ClientRepository;

import java.util.List;

public class ClientServiceMySQL implements ClientService{
    private final ClientRepository clientRepository;

    public ClientServiceMySQL(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Notification<Boolean> update(Long id, String name, String cardNb, String pnc, String address) {
        Client client = new ClientBuilder()
                .setId(id)
                .setName(name)
                .setCardNb(cardNb)
                .setPnc(pnc)
                .setAddress(address)
                .build();

        ClientValidator clientValidator = new ClientValidator(client);
        boolean clientValid = clientValidator.validate();
        Notification<Boolean> clientRegisterNotification = new Notification<>();

        if (!clientValid) {
            clientValidator.getErrors().forEach(clientRegisterNotification::addError);
            clientRegisterNotification.setResult(Boolean.FALSE);
            return clientRegisterNotification;
        } else {
            clientRegisterNotification.setResult(clientRepository.update(client));
            return clientRegisterNotification;
        }
    }

    @Override
    public Notification<Boolean> save(String name, String cardNb, String pnc, String address) {
        Client client = new ClientBuilder()
                .setName(name)
                .setCardNb(cardNb)
                .setPnc(pnc)
                .setAddress(address)
                .build();

        ClientValidator clientValidator = new ClientValidator(client);
        boolean clientValid = clientValidator.validate();
        Notification<Boolean> clientRegisterNotification = new Notification<>();

        if (!clientValid) {
            clientValidator.getErrors().forEach(clientRegisterNotification::addError);
            clientRegisterNotification.setResult(Boolean.FALSE);
            return clientRegisterNotification;
        } else {
            clientRegisterNotification.setResult(clientRepository.save(client));
            return clientRegisterNotification;
        }
    }
}
