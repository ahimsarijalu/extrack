package com.ahimsarijalu.extrack.expense;

import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserNotFoundException;
import com.ahimsarijalu.extrack.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ahimsarijalu.extrack.expense.ExpenseUtil.mapExpenseToDTO;
import static com.ahimsarijalu.extrack.utils.MapperUtil.mapDTOToEntity;


@Slf4j
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(ExpenseUtil::mapExpenseToDTO)
                .collect(Collectors.toList());
    }

    public ExpenseDTO getExpenseById(String id) {
        return expenseRepository.findById(UUID.fromString(id))
                .map(ExpenseUtil::mapExpenseToDTO)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    public ExpenseDTO saveExpense(ExpenseDTO expenseDTO) {
        User user = userRepository.findById(UUID.fromString(expenseDTO.getUserId()))
                .orElseThrow(() -> new UserNotFoundException(expenseDTO.getUserId()));

        Expense expense = mapDTOToEntity(expenseDTO, Expense.class);
        expense.setUser(user);
        expense = expenseRepository.save(expense);
        return mapExpenseToDTO(expense);
    }

    public void updateExpense(String id, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        if (expenseDTO.getTitle() != null) {
            expense.setTitle(expenseDTO.getTitle());
        }
        if (expenseDTO.getDescription() != null) {
            expense.setDescription(expenseDTO.getDescription());
        }
        if (expenseDTO.getAmount() != null) {
            expense.setAmount(expenseDTO.getAmount());
        }
        if (expenseDTO.getCategory() != null) {
            expense.setCategory(expenseDTO.getCategory());
        }
        if (expenseDTO.getUserId() != null) {
            expense.setExpenseDate(expenseDTO.getExpenseDate());
        }
        mapExpenseToDTO(expenseRepository.save(expense));
    }

    public void deleteExpense(String id) {
        if (expenseRepository.findById(UUID.fromString(id)).isEmpty()) {
            throw new ExpenseNotFoundException(id);
        }
        expenseRepository.deleteById(UUID.fromString(id));

    }

    public List<ExpenseDTO> getAllExpensesByCategory(Category category) {
        return expenseRepository.findAllByCategory(category)
                .stream()
                .map(ExpenseUtil::mapExpenseToDTO)
                .collect(Collectors.toList());
    }

    public List<ExpenseDTO> searchByTitleAndDescription(String query) {
        return expenseRepository.findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .map(ExpenseUtil::mapExpenseToDTO)
                .collect(Collectors.toList());
    }

    public List<ExpenseDTO> getAllExpenseByUserId(String userId) {
        return expenseRepository.findAllByUserId(UUID.fromString(userId))
                .stream()
                .map(ExpenseUtil::mapExpenseToDTO)
                .collect(Collectors.toList());
    }

}
