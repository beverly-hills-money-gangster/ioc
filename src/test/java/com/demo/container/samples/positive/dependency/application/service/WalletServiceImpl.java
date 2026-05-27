package com.demo.container.samples.positive.dependency.application.service;

import com.demo.annotation.Component;
import com.demo.container.samples.positive.dependency.application.repo.WalletRepository;

@Component
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;

  public WalletServiceImpl(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Override
  public void deposit(String accountName, int amount) {
    walletRepository.deposit(accountName, amount);
  }

  @Override
  public int getFunds(String accountName) {
    return walletRepository.getFunds(accountName);
  }
}
