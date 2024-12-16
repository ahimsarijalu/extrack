package com.ahimsarijalu.extrack.fund;

import com.ahimsarijalu.extrack.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ahimsarijalu.extrack.utils.MapperUtil.mapToApiResponse;

@Slf4j
@RestController
@RequestMapping("/api/fund")
public class FundController {
    private final FundService fundService;


    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<FundDTO>> createFundByUserId(@PathVariable String userId, @RequestBody FundDTO fundDTO) {
        ApiResponse<FundDTO> response;
        try {
            FundDTO createdFund = fundService.saveFund(userId, fundDTO);
            response = mapToApiResponse(HttpStatus.CREATED.value(), true, "Fund created successfully", createdFund);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to create fund, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<FundDTO>>> getFundByUserId(@PathVariable String userId) {
        ApiResponse<List<FundDTO>> response;
        try {
            List<FundDTO> funds = fundService.getAllFundsByUserId(userId);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Funds fetched successfully", funds);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to fetch funds, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/{id}")
    public ResponseEntity<ApiResponse<FundDTO>> getFundById(@PathVariable String userId, @PathVariable String id) {
        ApiResponse<FundDTO> response;
        try {
            FundDTO fund = fundService.getFundById(userId, id);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Fund fetched successfully", fund);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to fetch fund, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/{id}")
    public ResponseEntity<ApiResponse<FundDTO>> updateFund(@PathVariable String userId, @PathVariable String id, @RequestBody FundDTO fundDTO) {
        ApiResponse<FundDTO> response;
        try {
            FundDTO updatedFund = fundService.updateFund(userId, id, fundDTO);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Fund updated successfully", updatedFund);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to update fund, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFund(@PathVariable String userId, @PathVariable String id) {
        ApiResponse<Void> response;
        try {
            fundService.deleteFund(userId, id);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Fund deleted successfully", null);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to delete fund, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
