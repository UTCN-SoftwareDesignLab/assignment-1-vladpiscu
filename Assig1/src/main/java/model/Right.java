package model;

import java.util.Objects;

public class Right {
    private Long id;
    private String right;

    public Right(long id, String right) {
        this.id = id;
        this.right = right;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Right right1 = (Right) o;
        return Objects.equals(id, right1.id) &&
                Objects.equals(right, right1.right);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, right);
    }
}
