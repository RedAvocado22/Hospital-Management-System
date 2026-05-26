package com.hospital.hms.auth.service;

import com.hospital.hms.auth.entity.Account;
import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountQueryService {
    private final AccountRepository accountRepository;

    public Account getReferenceById(UUID id) {
        return accountRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public boolean hasRole(UUID id, String role) {
        Account account = accountRepository.findWithRoleById(id).orElseThrow(
                () -> new NotFoundException("Account not found with id: " + id)
        );

        return role.equals(account.getRole().getName());
    }
}
