package com.ahimsarijalu.extrack.fund;

import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ahimsarijalu.extrack.fund.FundUtils.mapFundToDTO;
import static com.ahimsarijalu.extrack.utils.MapperUtil.mapDTOToEntity;

@Service
public class FundService {

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private UserRepository userRepository;

    public FundDTO saveFund(String userId, FundDTO fundDTO) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + userId));

        Fund fund = mapDTOToEntity(fundDTO, Fund.class);
        fund.setUser(user);
        return mapFundToDTO(fundRepository.save(fund));
    }

    public List<FundDTO> getAllFundsByUserId(String userId) {
        List<FundDTO> funds = fundRepository.findAllByUserId(UUID.fromString(userId))
                .stream()
                .map(FundUtils::mapFundToDTO)
                .collect(Collectors.toList());

        if (funds.isEmpty()) {
            throw new EntityNotFoundException("No Funds found for this user");
        }

        return funds;
    }

    public FundDTO getFundByUserId(String userId) {
        return mapFundToDTO(fundRepository.findByUserId(UUID.fromString(userId)));
    }

    public FundDTO getFundById(String userId, String fundId) {
        userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + userId));

        return mapFundToDTO(fundRepository.findById(UUID.fromString(fundId))
                .orElseThrow(() -> new EntityNotFoundException("Fund not found with this id: " + fundId)));
    }

    public FundDTO updateFund(String userId, String fundId, FundDTO fundDTO) {
        userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + userId));
        Fund fund = fundRepository.findById(UUID.fromString(fundId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + fundId));

        if (fundDTO.getName() != null) {
            fund.setName(fundDTO.getName());
        }

        if (fundDTO.getBalance() != null) {
            fund.setBalance(fundDTO.getBalance());
        }
        return mapFundToDTO(fundRepository.save(fund));
    }

    public void deleteFund(String userId, String fundId) {
        userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + userId));
        Fund fund = fundRepository.findById(UUID.fromString(fundId))
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + fundId));
        fundRepository.delete(fund);
    }

}
