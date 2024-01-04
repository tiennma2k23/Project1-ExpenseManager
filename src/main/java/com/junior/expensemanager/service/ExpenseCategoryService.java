package com.junior.expensemanager.service;

import com.junior.expensemanager.entity.ExpenseCategory;
import com.junior.expensemanager.repository.ExpenseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryService {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    public List<String> getAllExpenseNames() {
        List<ExpenseCategory> expenseCategories = expenseCategoryRepository.findAll();
        List<String> expenseNames = expenseCategories.stream().map(ExpenseCategory::getName).collect(Collectors.toList());
        return expenseNames;
    }

    public void addOrUpdateExpenseCategory(String name) {
        // Kiểm tra xem danh mục đã tồn tại chưa
        ExpenseCategory existingCategory = expenseCategoryRepository.findByName(name);

        if (existingCategory == null) {
            // Nếu chưa tồn tại, thêm mới
            ExpenseCategory newCategory = new ExpenseCategory();
            newCategory.setName(name);
            expenseCategoryRepository.save(newCategory);
        }
    }
}
