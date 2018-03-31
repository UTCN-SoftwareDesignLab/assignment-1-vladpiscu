package model;

import java.sql.Date;
import java.util.Objects;

public class Account {
    private Long id;
    private String type;
    private int amount;
    private Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return amount == account.amount &&
                Objects.equals(id, account.id) &&
                Objects.equals(type, account.type) &&
                Objects.equals(creationDate, account.creationDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, type, amount, creationDate);
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", creationDate=" + creationDate;
    }
}
