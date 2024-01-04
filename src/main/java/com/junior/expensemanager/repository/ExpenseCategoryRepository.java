package com.junior.expensemanager.repository;

import com.junior.expensemanager.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    ExpenseCategory findByName(String name);

    boolean existsByName(String name);
}
