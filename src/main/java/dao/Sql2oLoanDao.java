package dao;
import models.Loan;
import org.sql2o.*;
import java.util.List;
public class Sql2oLoanDao implements LoanDao{
    private final Sql2o sql2o;

    public Sql2oLoanDao(Sql2o sql2o){
        this.sql2o = sql2o; //making the sql2o object available everywhere so we can call methods in it
    }

    @Override
    public void add(Loan loan) {
        String sql = "INSERT INTO loans (description, categoryId) VALUES (:description, :categoryId)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(loan) //map my argument onto the query so we can use information from it
                    .executeUpdate() //run it all
                    .getKey(); //int id is now the row number (row “key”) of db
            loan.setId(id); //update object to set id now from database
        } catch (Sql2oException ex) {
            System.out.println(ex); //oops we have an error!
        }
    }

    @Override
    public List<Loan> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM loans") //raw sql
                    .executeAndFetch(Loan.class); //fetch a list
        }
    }


    @Override
    public Loan findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM loans WHERE id = :id")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Loan.class); //fetch an individual item
        }
    }
    @Override
    public void update(int id, String newDescription, int newCategoryId){
        String sql = "UPDATE loans SET (description, categoryId) = (:description, :categoryId) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("description", newDescription)
                    .addParameter("categoryId", newCategoryId)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    @Override
    public void deleteById(int id) {
        String sql = "DELETE from loans WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllLoans() {
        String sql = "DELETE from loans";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
}
