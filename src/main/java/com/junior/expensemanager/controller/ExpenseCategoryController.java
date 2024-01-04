package com.junior.expensemanager.controller;

import com.junior.expensemanager.service.ExpenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/expenseCategory")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @GetMapping("/getExpenseNames")
    @ResponseBody
    public List<String> getExpenseNames() {
        List<String> expenseNames = expenseCategoryService.getAllExpenseNames();
        return expenseNames;
    }

    @PostMapping("/addOrUpdateExpenseCategory")
    public void addOrUpdateExpenseCategory(@RequestParam("name") String name) {
        expenseCategoryService.addOrUpdateExpenseCategory(name);
    }
}
