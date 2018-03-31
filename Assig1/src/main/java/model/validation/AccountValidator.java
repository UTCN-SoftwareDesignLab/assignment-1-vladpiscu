package model.validation;

import model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountValidator {
    private static final int MIN_AMOUNT_OF_MONEY = 0;

    private final Account account;
    private final List<String> errors;

    public AccountValidator(Account account) {
        this.account = account;
        this.errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean validate() {
        validateAmountOfMoney(account.getAmount());
        return errors.isEmpty();
    }

    private void validateAmountOfMoney(int amount){
        if(amount < MIN_AMOUNT_OF_MONEY){
            errors.add("Not enough money for the operation!");
        }
    }
}
