package com.demo.container.samples.positive.dependency.application.service;

import com.demo.annotation.Component;
import com.demo.container.samples.positive.dependency.application.repo.BankAccountRepository;

@Component
public class BankAccountServiceImpl implements BankAccountService {

  private final BankAccountRepository bankAccountRepository;

  public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
    this.bankAccountRepository = bankAccountRepository;
  }

  @Override
  public void createAccount(String accountName) {
    bankAccountRepository.createAccount(accountName);
  }
}
