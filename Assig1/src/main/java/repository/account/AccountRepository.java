package repository.account;

import model.Account;
import model.Client;

import java.util.List;

public interface AccountRepository {
    void addAccountToClient(Long clientId, Account account);
    List<Account> findAccountsForClient(Long clientId);
    void removeAll();
}
