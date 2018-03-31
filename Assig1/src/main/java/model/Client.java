package model;

import java.util.List;
import java.util.Objects;

public class Client {
    private long id;
    private String name;
    private String cardNb;
    private String pnc;
    private String address;
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNb() {
        return cardNb;
    }

    public void setCardNb(String cardNb) {
        this.cardNb = cardNb;
    }

    public String getPnc() {
        return pnc;
    }

    public void setPnc(String pnc) {
        this.pnc = pnc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(name, client.name) &&
                Objects.equals(cardNb, client.cardNb) &&
                Objects.equals(pnc, client.pnc) &&
                Objects.equals(address, client.address) &&
                Objects.equals(accounts, client.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cardNb, pnc, address, accounts);
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + '\'' +
                ", cardNb='" + cardNb + '\'' +
                ", pnc='" + pnc + '\'' +
                ", address='" + address;
    }
}
