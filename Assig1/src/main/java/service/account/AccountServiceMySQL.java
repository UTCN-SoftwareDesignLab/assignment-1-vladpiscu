package service.account;

import model.Account;
import model.builder.AccountBuilder;
import model.validation.AccountValidator;
import model.validation.ClientValidator;
import model.validation.Notification;
import repository.account.AccountRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AccountServiceMySQL implements AccountService {
    private AccountRepository accountRepository;

    public AccountServiceMySQL(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAccountsForClient(Long clientId) {
        return accountRepository.findAccountsForClient(clientId);
    }

    @Override
    public Notification<Boolean> addAccountForClient(Long clientId, String type, int amount, LocalDate date) {
        Date sqlDate = Date.valueOf(date);
        Account account = new AccountBuilder()
                .setType(type)
                .setAmount(amount)
                .setCreationDate(sqlDate)
                .build();
        AccountValidator accountValidator = new AccountValidator(account);
        boolean accountValid = accountValidator.validate();
        Notification<Boolean> accountRegisterNotification = new Notification<>();

        if (!accountValid) {
            accountValidator.getErrors().forEach(accountRegisterNotification::addError);
            accountRegisterNotification.setResult(Boolean.FALSE);
            return accountRegisterNotification;
        } else {
            accountRegisterNotification.setResult(accountRepository.addAccountToClient(clientId, account));
            return accountRegisterNotification;
        }
    }

    @Override
    public Notification<Boolean> updateAccount(Long accountId, String type, int amount, LocalDate date) {
        Account account = buildAccount(accountId, type, amount, date);
        AccountValidator accountValidator = new AccountValidator(account);
        boolean accountValid = accountValidator.validate();
        Notification<Boolean> accountUpdateNotification = new Notification<>();

        if (!accountValid) {
            accountValidator.getErrors().forEach(accountUpdateNotification::addError);
            accountUpdateNotification.setResult(Boolean.FALSE);
            return accountUpdateNotification;
        } else {
            accountUpdateNotification.setResult(accountRepository.updateAccount(account));
            return accountUpdateNotification;
        }
    }

    @Override
    public Notification<Boolean> transferMoneyBetweenAccounts(Account srcAccount, Account dstAccount, int amount) {
        srcAccount.setAmount(srcAccount.getAmount() - amount);
        AccountValidator accountValidator = new AccountValidator(srcAccount);
        boolean accountValid = accountValidator.validate();
        Notification<Boolean> accountTransferNotification = new Notification<>();

        if (!accountValid) {
            accountValidator.getErrors().forEach(accountTransferNotification::addError);
            accountTransferNotification.setResult(Boolean.FALSE);
            srcAccount.setAmount(srcAccount.getAmount() + amount);
            return accountTransferNotification;
        } else {
            dstAccount.setAmount(dstAccount.getAmount() + amount);
            boolean takeAmount = accountRepository.updateAccount(srcAccount);
            boolean putAmount = accountRepository.updateAccount(dstAccount);
            accountTransferNotification.setResult(takeAmount && putAmount);
            return accountTransferNotification;
        }
    }

    @Override
    public Notification<Boolean> payBills(Long accountId, String type, int amountAccount, LocalDate date, int amount) {
        Account account = buildAccount(accountId, type, amount, date);
        account.setAmount(account.getAmount() - amount);
        AccountValidator accountValidator = new AccountValidator(account);
        boolean accountValid = accountValidator.validate();
        Notification<Boolean> payBillsNotification = new Notification<>();

        if (!accountValid) {
            accountValidator.getErrors().forEach(payBillsNotification::addError);
            payBillsNotification.setResult(Boolean.FALSE);
            return payBillsNotification;
        } else {
            payBillsNotification.setResult(accountRepository.updateAccount(account));
            return payBillsNotification;
        }
    }

    @Override
    public boolean removeAccount(Long accountId) {
        return accountRepository.removeAccount(accountId);
    }

    public Account buildAccount(Long accountId, String type, int amount, LocalDate date){
        Date sqlDate = Date.valueOf(date);
        return (new AccountBuilder())
                .setId(accountId)
                .setType(type)
                .setAmount(amount)
                .setCreationDate(sqlDate)
                .build();
    }
}
