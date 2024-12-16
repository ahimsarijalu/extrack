package com.ahimsarijalu.extrack.expense;

import com.ahimsarijalu.extrack.fund.Fund;
import com.ahimsarijalu.extrack.fund.FundNotFoundException;
import com.ahimsarijalu.extrack.fund.FundRepository;
import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserNotFoundException;
import com.ahimsarijalu.extrack.user.UserRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private FundRepository fundRepository;

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

        Fund fund = fundRepository.findById(UUID.fromString(expenseDTO.getFundId())).orElseThrow(() -> new FundNotFoundException(expenseDTO.getFundId()));

        Long totalAmount = fund.getBalance() - expenseDTO.getAmount();
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }

        fund.setBalance(totalAmount);

        Expense expense = mapDTOToEntity(expenseDTO, Expense.class);

        log.info(expense.toString());
        expense.setUser(user);
        expense.setFund(fund);
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
            long totalAmount = expense.getFund().getBalance() - expenseDTO.getAmount();
            if (totalAmount < 0) {
                throw new IllegalArgumentException("Total amount cannot be negative");
            }
            expense.getFund().setBalance(totalAmount);
        }
        if (expenseDTO.getCategory() != null) {
            expense.setCategory(expenseDTO.getCategory());
        }
        if (expenseDTO.getUserId() != null) {
            expense.setExpenseDate(expenseDTO.getExpenseDate());
        }
        mapExpenseToDTO(expenseRepository.save(expense));
    }

    @Transactional
    public void deleteExpense(String id) {
        Expense expense = expenseRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        Fund fund = expense.getFund();
        long totalAmount = fund.getBalance() + expense.getAmount();
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }

        fund.setBalance(totalAmount);
        fund.getExpenses().remove(expense);
        fundRepository.save(fund);

        expenseRepository.delete(expense);

        log.info("Expense deleted successfully: {}", id);
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

    public TopCategoryDTO findTopCategoryByUserId(String userId) {
        return expenseRepository.findTopCategoryByUserId(UUID.fromString(userId));
    }

}
