package models;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;
public class LoanTest {
    @Test
    public void NewLoanObjectGetsCorrectlyCreated_true() throws Exception {
        Loan loan = setupNewLoan();
        assertEquals(true, loan instanceof Loan);
    }

    @Test
    public void LoanInstantiatesWithDescription_true() throws Exception {
        Loan loan = setupNewLoan();
        assertEquals("Mow the lawn", loan.getDescription());
    }

    @Test
    public void isCompletedPropertyIsFalseAfterInstantiation() throws Exception {
        Loan loan = setupNewLoan();
        assertEquals(false, loan.isCompleted()); //should never start as completed
    }

    @Test
    public void getCreatedAtInstantiatesWithCurrentTimeToday() throws Exception {
        Loan loan = setupNewLoan();
        assertEquals(LocalDateTime.now().getDayOfWeek(), loan.getCreatedAt().getDayOfWeek());
    }

    //helper methods
    public Loan setupNewLoan(){
        return new Loan("Mow the lawn", 1);
    }

    @Test
    public void getCategoryId() {
    }

    @Test
    public void setCategoryId() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void setDescription() {
    }

    @Test
    public void setCompleted() {
    }

    @Test
    public void setId() {
    }

    @Test
    public void getDescription() {
    }

    @Test
    public void getCompleted() {
    }

    @Test
    public void getCreatedAt() {
    }

    @Test
    public void getId() {
    }

    @Test
    public void testEquals1() {
    }

    @Test
    public void testHashCode1() {
    }

    @Test
    public void isCompleted() {
    }

    @Test
    public void setCreatedAt() {
    }

    @Test
    public void testEquals2() {
    }

    @Test
    public void testHashCode2() {
    }
}
