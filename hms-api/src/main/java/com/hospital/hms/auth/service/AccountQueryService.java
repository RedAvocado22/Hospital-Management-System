package com.hospital.hms.auth.service;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountQueryService {
    private final AccountRepository accountRepository;

    public Account getReferenceById(UUID id) {
        return accountRepository.getReferenceById(id);
    }
}
