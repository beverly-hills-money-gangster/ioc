package com.demo.container.samples.positive.dependency.application.repo;

public interface WalletRepository {

  void deposit(String accountName, int amount);

  int getFunds(String accountName);
}
