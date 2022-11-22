import dao.Sql2oCategoryDao;
import dao.Sql2oLoanDao;
import models.Category;
import models.Loan;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) { //type “psvm + tab” to autocreate this
        port(8090);
        staticFileLocation("/public");
        String connectionString = "jdbc:postgresql://localhost:5432/myloans";      //connect to todolist, not todolist_test!

        Sql2o sql2o = new Sql2o(connectionString, "kajela", "8444");
        Sql2oLoanDao loanDao = new Sql2oLoanDao(sql2o);
        Sql2oCategoryDao categoryDao = new Sql2oCategoryDao(sql2o);


        get("/loan", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index.html");
        }, new HandlebarsTemplateEngine());

        //get: show all loans in all categories and show all categories

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Category> allCategories = categoryDao.getAll();
            model.put("categories", allCategories);
            model = new HashMap<>();
           List<Loan> loans = loanDao.getAll();
            model.put("loans", loans);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to create a new category

        get("/categories/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Category> categories = categoryDao.getAll(); //refresh list of links for navbar
            model.put("categories", categories);
            return new ModelAndView(model, "category-form.hbs"); //new layout
        }, new HandlebarsTemplateEngine());

        //post: process a form to create a new category

        post("/categories", (req, res) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            Category newCategory = new Category(name);
            categoryDao.add(newCategory);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete all categories and all loans

        get("/categories/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            categoryDao.clearAllCategories();
            loanDao.clearAllLoans();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete all loans

        get("/loans/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            loanDao.clearAllLoans();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get a specific category (and the loans it contains)
        get("/categories/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfCategoryToFind = Integer.parseInt(req.params("id")); //new
            Category foundCategory = categoryDao.findById(idOfCategoryToFind);
            model.put("category", foundCategory);
            List<Loan> allLoansByCategory = categoryDao.getAllLoansByCategory(idOfCategoryToFind);
            model.put("tasks", allLoansByCategory);
            model.put("categories", categoryDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "category-detail.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a category

        get("/categories/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("editCategory", true);
            Category category = categoryDao.findById(Integer.parseInt(req.params("id")));
            model.put("category", category);
            model.put("categories", categoryDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "category-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a category

        post("/categories/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfCategoryToEdit = Integer.parseInt(req.params("id"));
            String newName = req.queryParams("newCategoryName");
            categoryDao.update(idOfCategoryToEdit, newName);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: delete an individual loan

        get("/categories/:category_id/loans/:loan_id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfLoanToDelete = Integer.parseInt(req.params("loan_id"));
            loanDao.deleteById(idOfLoanToDelete);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: show new loan form
        get("/loans/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Category> categories = categoryDao.getAll();
            model.put("categories", categories);
            return new ModelAndView(model, "loan-form.hbs");
        }, new HandlebarsTemplateEngine());

        //task: process new loan form
        post("/loans", (req, res) -> { //URL to make new task on POST route
            Map<String, Object> model = new HashMap<>();
            List<Category> allCategories = categoryDao.getAll();
            model.put("categories", allCategories);
            String description = req.queryParams("description");
            int categoryId = Integer.parseInt(req.queryParams("categoryId"));
            Loan newLoan = new Loan(description, categoryId);        //See what we did with the hard coded categoryId?
            loanDao.add(newLoan);
//            List<Task> tasksSoFar = taskDao.getAll();
//            for (Task taskItem: tasksSoFar
//                 ) {
//                System.out.println(taskItem);
//            }
//            System.out.println(tasksSoFar);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: show an individual loan that is nested in a category

        get("/categories/:category_id/loans/:loan_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfLoanToFind = Integer.parseInt(req.params("loan_id")); //pull id - must match route segment
            Loan foundLoan = loanDao.findById(idOfLoanToFind); //use it to find task
            int idOfCategoryToFind = Integer.parseInt(req.params("category_id"));
            Category foundCategory = categoryDao.findById(idOfCategoryToFind);
            model.put("category", foundCategory);
            model.put("loan", foundLoan); //add it to model for template to display
            model.put("categories", categoryDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "loan-detail.hbs"); //individual task page.
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a loan

        get("/loans/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Category> allCategories = categoryDao.getAll();
            model.put("categories", allCategories);
            Loan loan = loanDao.findById(Integer.parseInt(req.params("id")));
            model.put("loan", loan);
            model.put("editLoan", true);
            return new ModelAndView(model, "loan-form.hbs");
        }, new HandlebarsTemplateEngine());

        //loan: process a form to update a task
        post("/loans/:id", (req, res) -> { //URL to update task on POST route
            Map<String, Object> model = new HashMap<>();
            int loanToEditId = Integer.parseInt(req.params("id"));
            String newContent = req.queryParams("description");
            int newCategoryId = Integer.parseInt(req.queryParams("categoryId"));
            loanDao.update(loanToEditId, newContent, newCategoryId);  // remember the hardcoded categoryId we placed? See what we've done to/with it?
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());



    }
}

