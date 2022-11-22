package dao;
import models.Loan;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
public class Sql2oLoanDaoTest {
    private Sql2oLoanDao loanDao; //ignore me for now. We'll create this soon.
    private static Connection con; //must be sql2o class conn

    @Before
    public void setUp() {
        String connectionString = "jdbc:postgresql://localhost:5432/myloans_test"; // connect to postgres test database

        Sql2o sql2o = new Sql2o(connectionString, "kajela", "8444");
        loanDao = new Sql2oLoanDao(sql2o); //ignore me for now
        con = sql2o.open(); //keep connection open through entire test so, it does not get erased
    }

    @After
    public void tearDown() {
        System.out.println("clearing database");
        loanDao.clearAllLoans();
        con.close();
    }
    @AfterClass
    public static void shutDown() {
        con.close();
        System.out.println("connection closed");
    }
    @Test
    public void addingLoanSetsId() {
        Loan loan = setupNewLoan();
        int originalLoanId = loan.getId();
        loanDao.add(loan);
        assertNotEquals(originalLoanId, loan.getId()); //how does this work?
    }
    @Test
    public void existingLoansCanBeFoundById() {
        Loan loan = setupNewLoan();
        loanDao.add(loan); //add to dao (takes care of saving)
        Loan foundLoan = loanDao.findById(loan.getId()); //retrieve
        assertNotEquals(loan, foundLoan); //should be the same
    }

    @Test
    public void addedLoansAreReturnedFromGetAll() {
        Loan loan = setupNewLoan();
        loanDao.add(loan);
        assertEquals(1, loanDao.getAll().size());
    }

    @Test
    public void noLoansReturnsEmptyList() {
        assertEquals(0, loanDao.getAll().size());
    }


    @Test
    public void updateChangesLoanContent() {
        String initialDescription = "mow the lawn";
        Loan loan = new Loan (initialDescription, 1);// or use the helper method for easier refactoring
        loanDao.add(loan);
        loanDao.update(loan.getId(),"brush the cat", 1);
        Loan updatedLoan = loanDao.findById(loan.getId()); //why do I need to refind this?
        assertNotEquals(initialDescription, updatedLoan.getDescription());
    }
    @Test
    public void deleteByIdDeletesCorrectLoan() {
        Loan loan = setupNewLoan();
        loanDao.add(loan);
        loanDao.deleteById(loan.getId());
        assertEquals(0, loanDao.getAll().size());
    }
    @Test
    public void clearAllClearsAll() {
        Loan loan = setupNewLoan();
        Loan otherLoan = new Loan("brush the cat", 2);
        loanDao.add(loan);
        loanDao.add(otherLoan);
        int daoSize = loanDao.getAll().size();
        loanDao.clearAllLoans();
        assertTrue(daoSize > 0 && daoSize > loanDao.getAll().size()); //this is a little overcomplicated, but illustrates well how we might use `assertTrue` in a different way.
    }
    @Test
    public void categoryIdIsReturnedCorrectly() {
        Loan loan = setupNewLoan();
        int originalCatId = loan.getCategoryId();
        loanDao.add(loan);
        assertEquals(originalCatId, loanDao.findById(loan.getId()).getCategoryId());
    }
    //define the following once and then call it as above in your tests.
    public Loan setupNewLoan(){
        return new Loan("Mow the lawn", 1);
    }

}
