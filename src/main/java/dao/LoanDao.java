package dao;

import models.Loan;
import java.util.List;
public interface LoanDao {
    // LIST
    List<Loan> getAll();

    // CREATE
    void add(Loan loan);

    // READ
    Loan findById(int id);

    // UPDATE
     void update(int id, String content, int categoryId);

    // DELETE
     void deleteById(int id);
     void clearAllLoans();
}
