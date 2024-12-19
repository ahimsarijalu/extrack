package com.ahimsarijalu.extrack.controller;

import com.ahimsarijalu.extrack.fund.FundController;
import com.ahimsarijalu.extrack.fund.FundDTO;
import com.ahimsarijalu.extrack.fund.FundService;
import com.ahimsarijalu.extrack.utils.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class FundControllerTest {

    @Mock
    private FundService fundService;

    @InjectMocks
    private FundController fundController;

    private UUID userId;
    private UUID fundId;
    private FundDTO fundDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        fundId = UUID.randomUUID();

        fundDTO = new FundDTO();
        fundDTO.setId(fundId.toString());
        fundDTO.setName("Emergency Fund");
        fundDTO.setBalance(1000L);
        fundDTO.setUserId(userId.toString());
    }

    @Test
    void testCreateFundByUserId_Success() {
        when(fundService.saveFund(userId.toString(), fundDTO)).thenReturn(fundDTO);

        ResponseEntity<ApiResponse<FundDTO>> response = fundController.createFundByUserId(userId.toString(), fundDTO);

        assertNotNull(response);
        assertEquals(CREATED, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getSuccess());
        assertEquals("Fund created successfully", response.getBody().getMessage());
        verify(fundService).saveFund(userId.toString(), fundDTO);
    }

    @Test
    void testCreateFundByUserId_UserNotFound() {
        when(fundService.saveFund(userId.toString(), fundDTO)).thenThrow(new EntityNotFoundException("User not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundController.createFundByUserId(userId.toString(), fundDTO));

        assertEquals("User not found", exception.getMessage());
        verify(fundService).saveFund(userId.toString(), fundDTO);
    }

    @Test
    void testGetFundByUserId_Success() {
        when(fundService.getAllFundsByUserId(userId.toString())).thenReturn(Collections.singletonList(fundDTO));

        ResponseEntity<ApiResponse<List<FundDTO>>> response = fundController.getFundByUserId(userId.toString());

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getSuccess());
        assertEquals("Funds fetched successfully", response.getBody().getMessage());
        verify(fundService).getAllFundsByUserId(userId.toString());
    }

    @Test
    void testGetFundByUserId_NoFundsFound() {
        when(fundService.getAllFundsByUserId(userId.toString())).thenThrow(new EntityNotFoundException("No Funds found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundController.getFundByUserId(userId.toString()));

        assertEquals("No Funds found", exception.getMessage());
        verify(fundService).getAllFundsByUserId(userId.toString());
    }

    @Test
    void testGetFundById_Success() {
        when(fundService.getFundById(userId.toString(), fundId.toString())).thenReturn(fundDTO);

        ResponseEntity<ApiResponse<FundDTO>> response = fundController.getFundById(userId.toString(), fundId.toString());

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getSuccess());
        assertEquals("Fund fetched successfully", response.getBody().getMessage());
        verify(fundService).getFundById(userId.toString(), fundId.toString());
    }

    @Test
    void testGetFundById_FundNotFound() {
        when(fundService.getFundById(userId.toString(), fundId.toString()))
                .thenThrow(new EntityNotFoundException("Fund not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundController.getFundById(userId.toString(), fundId.toString()));

        assertEquals("Fund not found", exception.getMessage());
        verify(fundService).getFundById(userId.toString(), fundId.toString());
    }

    @Test
    void testUpdateFund_Success() {
        when(fundService.updateFund(userId.toString(), fundId.toString(), fundDTO)).thenReturn(fundDTO);

        ResponseEntity<ApiResponse<FundDTO>> response = fundController.updateFund(userId.toString(), fundId.toString(), fundDTO);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getSuccess());
        assertEquals("Fund updated successfully", response.getBody().getMessage());
        verify(fundService).updateFund(userId.toString(), fundId.toString(), fundDTO);
    }

    @Test
    void testDeleteFund_Success() {
        doNothing().when(fundService).deleteFund(userId.toString(), fundId.toString());

        ResponseEntity<ApiResponse<Void>> response = fundController.deleteFund(userId.toString(), fundId.toString());

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getSuccess());
        assertEquals("Fund deleted successfully", response.getBody().getMessage());
        verify(fundService).deleteFund(userId.toString(), fundId.toString());
    }

    @Test
    void testDeleteFund_FundNotFound() {
        doThrow(new EntityNotFoundException("Fund not found"))
                .when(fundService).deleteFund(userId.toString(), fundId.toString());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundController.deleteFund(userId.toString(), fundId.toString()));

        assertEquals("Fund not found", exception.getMessage());
        verify(fundService).deleteFund(userId.toString(), fundId.toString());
    }
}