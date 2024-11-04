package com.example.cruddemo.service;

import com.example.cruddemo.model.BankInformation;
// src/main/java/com/yourpackage/service/BankInformationService.java

import java.util.UUID;

public interface BankInformationService {
    BankInformation createBankInformation(UUID userId, BankInformation bankInfo) throws Exception;

    BankInformation getBankInformationByUserId(UUID userId) throws Exception;

    BankInformation updateBankInformation(UUID userId, BankInformation bankInfo) throws Exception;

    void deleteBankInformation(UUID userId);
}
