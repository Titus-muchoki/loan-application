package dao;

import models.Category;
import models.Loan;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oCategoryDaoTest {

    private Sql2oCategoryDao categoryDao; //ignore me for now. We'll create this soon.
    private Sql2oLoanDao loanDao;
    private static Connection con; //must be sql2o class conn


    @Before
    public void setUp() {
        String connectionString = "jdbc:postgresql://localhost:5432/myloans_test"; // connect to postgres test database

        Sql2o sql2o = new Sql2o(connectionString, "kajela", "8444");
        categoryDao = new Sql2oCategoryDao(sql2o); //ignore me for now
        loanDao = new Sql2oLoanDao(sql2o);
        con = sql2o.open(); //keep connection open through entire test so, it does not get erased
    }

    @After
    public void tearDown() {
        System.out.println("clearing database");
        categoryDao.clearAllCategories();           // clear all categories after every test
        loanDao.clearAllLoans();
        con.close();
    }
    @AfterClass                                     //run once after all tests in this file completed
    public static void shutDown() {
        con.close();                               // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }
    @Test
    public void addingCategorySetsId() {
        Category category = new Category("mow the lawn");
        int originalCategoryId = category.getId();
        categoryDao.add(category);
        assertNotEquals(originalCategoryId, category.getId()); //how does this work?

    }

    @Test
    public void existingCategoryCanBeFoundById() {
        Category category = new Category("mow the lawn");
        categoryDao.add(category);
        Category foundCategory = categoryDao.findById(category.getId());
        assertEquals(category, foundCategory); //should be the same

    }
    @Test
    public void addedCategoriesAreReturnedFromGetAll() {
        Category category = setupNewCategory();
        categoryDao.add(category);
        assertEquals(1, categoryDao.getAll().size());
    }
    @Test
    public void noCategoriesReturnsEmptyList() {
        assertEquals(0, categoryDao.getAll().size());
    }
    @Test
    public void updateChangesCategoryContent() {
        String initialDescription = "Yardwork";
        Category category = new Category (initialDescription);
        categoryDao.add(category);
        categoryDao.update(category.getId(),"Cleaning");
        Category updatedCategory = categoryDao.findById(category.getId());
        assertNotEquals(initialDescription, updatedCategory.getName());
    }
    @Test
    public void deleteByIdDeletesCorrectCategory() {
        Category category = setupNewCategory();
        categoryDao.add(category);
        categoryDao.deleteById(category.getId());
        assertEquals(0, categoryDao.getAll().size());
    }
    @Test
    public void clearAllClearsAllCategories() {
        Category category = setupNewCategory();
        Category otherCategory = new Category("Cleaning");
        categoryDao.add(category);
        categoryDao.add(otherCategory);
        int daoSize = categoryDao.getAll().size();
        categoryDao.clearAllCategories();
        assertTrue(daoSize > 0 && daoSize > categoryDao.getAll().size());
    }
    @Test
    public void getAllLoansByCategoryReturnsLoansCorrectly() {
        Category category = setupNewCategory();
        categoryDao.add(category);
        int categoryId = category.getId();
        Loan newLoan = new Loan("mow the lawn", categoryId);
        Loan otherLoan = new Loan("pull weeds", categoryId);
        Loan thirdLoan = new Loan("trim hedge", categoryId);
        loanDao.add(newLoan);
        loanDao.add(otherLoan); //we are not adding loan 3 so, we can test things precisely.
        assertEquals(2, categoryDao.getAllLoansByCategory(categoryId).size());
        assertFalse(categoryDao.getAllLoansByCategory(categoryId).contains(newLoan));
        assertFalse(categoryDao.getAllLoansByCategory(categoryId).contains(otherLoan));
        assertFalse(categoryDao.getAllLoansByCategory(categoryId).contains(thirdLoan)); //things are accurate!
    }
    public Category setupNewCategory(){
        return new Category("Yardwork");
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void getName() {
    }

    @Test
    public void setName() {
    }

    @Test
    public void getId() {
    }

    @Test
    public void setId() {
    }
}
