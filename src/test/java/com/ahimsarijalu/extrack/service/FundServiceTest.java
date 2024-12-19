package com.ahimsarijalu.extrack.service;

import com.ahimsarijalu.extrack.fund.Fund;
import com.ahimsarijalu.extrack.fund.FundDTO;
import com.ahimsarijalu.extrack.fund.FundRepository;
import com.ahimsarijalu.extrack.fund.FundService;
import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundServiceTest {

    @Mock
    private FundRepository fundRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FundService fundService;

    private UUID userId;
    private UUID fundId;
    private User user;
    private Fund fund;
    private FundDTO fundDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        fundId = UUID.randomUUID();

        user = new User();
        user.setId(userId);

        fund = new Fund();
        fund.setId(fundId);
        fund.setName("Emergency Fund");
        fund.setBalance(1000L);
        fund.setUser(user);

        fundDTO = new FundDTO();
        fundDTO.setId(fundId.toString());
        fundDTO.setName("Emergency Fund");
        fundDTO.setBalance(1000L);
        fundDTO.setUserId(userId.toString());
    }

    @Test
    void testSaveFund_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fundRepository.save(any(Fund.class))).thenReturn(fund);

        FundDTO result = fundService.saveFund(userId.toString(), fundDTO);

        assertNotNull(result);
        assertEquals(fund.getName(), result.getName());
        assertEquals(fund.getBalance(), result.getBalance());
        verify(userRepository).findById(userId);
        verify(fundRepository).save(any(Fund.class));
    }

    @Test
    void testSaveFund_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundService.saveFund(userId.toString(), fundDTO));

        assertEquals("User not found with this id: " + userId, exception.getMessage());
        verify(userRepository).findById(userId);
        verify(fundRepository, never()).save(any(Fund.class));
    }

    @Test
    void testGetAllFundsByUserId_Success() {
        when(fundRepository.findAllByUserId(userId)).thenReturn(Collections.singletonList(fund));

        List<FundDTO> result = fundService.getAllFundsByUserId(userId.toString());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fund.getName(), result.getFirst().getName());
        verify(fundRepository).findAllByUserId(userId);
    }

    @Test
    void testGetAllFundsByUserId_NoFundsFound() {
        when(fundRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundService.getAllFundsByUserId(userId.toString()));

        assertEquals("No Funds found for this user", exception.getMessage());
        verify(fundRepository).findAllByUserId(userId);
    }

    @Test
    void testGetFundById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fund));

        FundDTO result = fundService.getFundById(userId.toString(), fundId.toString());

        assertNotNull(result);
        assertEquals(fund.getName(), result.getName());
        verify(userRepository).findById(userId);
        verify(fundRepository).findById(fundId);
    }

    @Test
    void testGetFundById_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundService.getFundById(userId.toString(), fundId.toString()));

        assertEquals("User not found with this id: " + userId, exception.getMessage());
        verify(userRepository).findById(userId);
        verify(fundRepository, never()).findById(fundId);
    }

    @Test
    void testUpdateFund_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fund));
        when(fundRepository.save(fund)).thenReturn(fund);

        fundDTO.setName("Updated Fund");
        fundDTO.setBalance(2000L);

        FundDTO result = fundService.updateFund(userId.toString(), fundId.toString(), fundDTO);

        assertNotNull(result);
        assertEquals("Updated Fund", result.getName());
        assertEquals(2000L, result.getBalance());
        verify(fundRepository).save(fund);
    }

    @Test
    void testDeleteFund_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fund));

        assertDoesNotThrow(() -> fundService.deleteFund(userId.toString(), fundId.toString()));

        verify(fundRepository).delete(fund);
    }
}
