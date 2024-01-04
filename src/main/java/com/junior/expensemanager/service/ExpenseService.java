package com.junior.expensemanager.service;

import com.junior.expensemanager.dto.ExpenseDTO;
import com.junior.expensemanager.dto.ExpenseFilterDTO;
import com.junior.expensemanager.entity.Expense;
import com.junior.expensemanager.entity.User;
import com.junior.expensemanager.exception.ExpenseNotFoundException;
import com.junior.expensemanager.repository.ExpenseRepository;
import com.junior.expensemanager.util.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    public List<ExpenseDTO> getAllExpenseList() {
        User user = userService.getLoggedInUser();
        List<Expense> listExpense = expenseRepository.findByDateBetweenAndUserId(
                Date.valueOf(LocalDate.now().withDayOfMonth(1)),
                Date.valueOf(LocalDate.now()), user.getId());
        return listExpense.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private ExpenseDTO mapToDTO(Expense expense) {
//        ExpenseDTO expenseDTO = new ExpenseDTO();
//        expenseDTO.setId(expense.getId());
//        expenseDTO.setExpenseId(expense.getExpenseId());
//        expenseDTO.setAmount(expense.getAmount());
//        expenseDTO.setName(expense.getName());
//        expenseDTO.setDescription(expense.getDescription());
//        expenseDTO.setDate(expense.getDate());
//        return expenseDTO;

        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
        expenseDTO.setDateString(DateTimeUtil.convertToString(expense.getDate()));
        return expenseDTO;
    }

    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {
        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        // TODO: generate the expense id
        if (expense.getId() == null) {
            expense.setExpenseId(UUID.randomUUID().toString());
        }
        // TODO: set the expense date
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));
        // Return expense entity
        return expense;
    }

    public void saveExpenseDetails(ExpenseDTO expenseDTO) throws ParseException {
        Expense expense = mapToEntity(expenseDTO);
        if(!expense.getDate().before(new java.util.Date())) {
            throw new RuntimeException("Future date is not allowed");
        }
        expense.setUser(userService.getLoggedInUser());
        expenseRepository.save(expense);
    }

    public void deleteExpenseByExpenseId(String expenseId) {
        Expense expense = expenseRepository.findByExpenseId(expenseId);
        if(expense == null) {
            throw new ExpenseNotFoundException("Expense not found for the id: "+expenseId);
        }
        expenseRepository.delete(expense);
    }

    public ExpenseDTO findByIdExpenseId(String expenseId) {
        Expense expense = expenseRepository.findByExpenseId(expenseId);
        if(expense == null) {
            throw new ExpenseNotFoundException("Expense not found for the id:"+expenseId);
        }
        return mapToDTO(expense);
    }

    public List<ExpenseDTO> getFilterExpenses(ExpenseFilterDTO expenseFilterDTO) throws ParseException {
        String keyword = expenseFilterDTO.getKeyword();
        String sortBy = expenseFilterDTO.getSortBy();
        String fromDateStr = expenseFilterDTO.getFromDate();
        String toDateStr = expenseFilterDTO.getToDate();

        Date fromDate = !fromDateStr.isEmpty() ? DateTimeUtil.convertStringToDate(fromDateStr) : new Date(0);
        Date toDate = !toDateStr.isEmpty() ? DateTimeUtil.convertStringToDate(toDateStr) : new Date(System.currentTimeMillis());
        User user = userService.getLoggedInUser();
        List<Expense> expenses = expenseRepository.findByNameContainingAndDateBetweenAndUserId(
                keyword,
                fromDate,
                toDate,
                user.getId());
        List<ExpenseDTO> expensesDTO = expenses.stream().map(this::mapToDTO).collect(Collectors.toList());
        if (sortBy != null) {
            if (sortBy.equals("date")) {
                expensesDTO.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            } else {
                expensesDTO.sort((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));
            }
        }
        return expensesDTO;
    }

    public String totalExpenses(List<ExpenseDTO> expenses) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        Long sum = 0L;
        for (ExpenseDTO o : expenses) {

            sum = sum+o.getAmount();
        }
        return decimalFormat.format(sum);
    }
    public String totalChi(List<ExpenseDTO> expenses) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        Long sum = 0L;
        for (ExpenseDTO o : expenses) if (Objects.equals(o.getExpenseType(), "Chi")) {

            sum = sum+o.getAmount();
        }
        return decimalFormat.format(sum);
    }

    public String totalTieu(List<ExpenseDTO> expenses) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        Long sum = 0L;
        for (ExpenseDTO o : expenses) if (Objects.equals(o.getExpenseType(), "Tieu")) {

            sum = sum+Math.abs(o.getAmount());
        }
        return decimalFormat.format(sum);
    }
}
