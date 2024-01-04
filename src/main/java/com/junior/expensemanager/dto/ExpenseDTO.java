package com.junior.expensemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO  {
    private Long id;

     private String name;

     private String description;

     private Long  amount;

     private Date date;

     private String dateString;

     private String expenseId;

     private String expenseType;

 public Long getAmount() {
  if(Objects.equals(expenseType, "Tieu") && amount>0) return -amount;
  else return amount;
 }
}
