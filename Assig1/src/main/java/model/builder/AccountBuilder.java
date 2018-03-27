package model.builder;

import model.Account;
import java.sql.Date;

public class AccountBuilder {
    private Account account;

    public AccountBuilder(){
        account = new Account();
    }

    public AccountBuilder setId(Long id){
        account.setId(id);
        return this;
    }

    public AccountBuilder setType(String type){
        account.setType(type);
        return this;
    }

    public AccountBuilder setAmount(int amount){
        account.setAmount(amount);
        return this;
    }

    public AccountBuilder setCreationDate(Date creationDate){
        account.setCreationDate(creationDate);
        return this;
    }

    public Account build(){
        return account;
    }
}
