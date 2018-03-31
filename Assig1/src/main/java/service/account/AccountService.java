package service.account;

import model.Account;
import model.validation.Notification;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {
    List<Account> getAccountsForClient(Long clientId);
    Notification<Boolean> addAccountForClient(Long clientId, String type, int amount, LocalDate date);
    Notification<Boolean> updateAccount(Long accountId, String type, int amount, LocalDate date);
    Notification<Boolean> transferMoneyBetweenAccounts(Account srcAccount, Account dstAccount, int amount);
    Notification<Boolean> payBills(Long accountId, String type, int amountAccount, LocalDate date, int amount);
    boolean removeAccount(Long accountId);
}
