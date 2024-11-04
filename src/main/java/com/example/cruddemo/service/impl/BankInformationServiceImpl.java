package com.example.cruddemo.service.impl;
// src/main/java/com/yourpackage/service/impl/BankInformationServiceImpl.java

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.cruddemo.model.BankInformation;
import com.example.cruddemo.model.User;
import com.example.cruddemo.repository.BankInformationRepository;
import com.example.cruddemo.repository.UserRepository;
import com.example.cruddemo.service.BankInformationService;
import com.example.cruddemo.util.EncryptionUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BankInformationServiceImpl implements BankInformationService {

    private final BankInformationRepository _bankInformationRepository;
    private final UserRepository _userRepository;

    @Value("${encryption.key}") // Load encryption key from environment
    private String encryptionKey;

    public BankInformationServiceImpl(BankInformationRepository bankInformationRepository,
            UserRepository userRepository) {
        _bankInformationRepository = bankInformationRepository;
        _userRepository = userRepository;
    }

    @Override
    public BankInformation createBankInformation(UUID userId, BankInformation bankInfo) throws Exception {
        User user = _userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        bankInfo.setUser(user);

        // Encrypt the bank account number before saving
        String encryptedAccountNumber = EncryptionUtil.encrypt(bankInfo.getBankAccountNumber(), encryptionKey);
        bankInfo.setBankAccountNumber(encryptedAccountNumber);

        return _bankInformationRepository.save(bankInfo);
    }

    @Override
    public BankInformation getBankInformationByUserId(UUID userId) throws Exception {
        BankInformation bankInfo = _bankInformationRepository.findByUser_Id(userId)
                .orElseThrow(() -> new IllegalArgumentException("Bank information not found for user"));

        // Decrypt the bank account number before returning
        String decryptedAccountNumber = EncryptionUtil.decrypt(bankInfo.getBankAccountNumber(), encryptionKey);
        bankInfo.setBankAccountNumber(decryptedAccountNumber);

        return bankInfo;
    }

    @Override
    public BankInformation updateBankInformation(UUID userId, BankInformation bankInfo) throws Exception {
        BankInformation existingBankInfo = _bankInformationRepository.findByUser_Id(userId)
                .orElseThrow(() -> new IllegalArgumentException("Bank information not found for user"));

        // Encrypt the bank account number before saving
        String encryptedAccountNumber = EncryptionUtil.encrypt(bankInfo.getBankAccountNumber(), encryptionKey);
        existingBankInfo.setBankAccountNumber(encryptedAccountNumber);
        existingBankInfo.setBankName(bankInfo.getBankName());
        existingBankInfo.setAccountType(bankInfo.getAccountType());

        return _bankInformationRepository.save(existingBankInfo);
    }

    @Override
    public void deleteBankInformation(UUID userId) {
        BankInformation bankInfo = _bankInformationRepository.findByUser_Id(userId)
                .orElseThrow(() -> new IllegalArgumentException("Bank information not found for user"));
        _bankInformationRepository.delete(bankInfo);
    }
}
