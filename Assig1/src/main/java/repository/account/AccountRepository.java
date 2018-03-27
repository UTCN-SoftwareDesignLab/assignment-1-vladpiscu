package repository.account;

import model.Account;
import model.Client;

import java.util.List;

public interface AccountRepository {
    boolean addAccountToClient(Long clientId, Account account);

    boolean updateAccount(Account account);

    List<Account> findAccountsForClient(Long clientId);

    boolean removeAccount(Long accountId);
    void removeAll();
}
