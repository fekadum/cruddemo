package com.example.cruddemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cruddemo.model.BankInformation;

import java.util.Optional;
import java.util.UUID;

public interface BankInformationRepository extends JpaRepository<BankInformation, UUID> {
    Optional<BankInformation> findByUser_Id(UUID userId);

}
