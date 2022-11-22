package models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Loan {
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private int id;
    private int categoryId;

    public Loan(String description, int categoryId) {
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.categoryId = categoryId;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan loan = (Loan) o;
        return completed == loan.completed && id == loan.id && categoryId == loan.categoryId && Objects.equals(description, loan.description) && Objects.equals(createdAt, loan.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, completed, createdAt, id, categoryId);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}