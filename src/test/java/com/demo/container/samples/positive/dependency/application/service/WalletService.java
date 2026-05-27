package com.demo.container.samples.positive.dependency.application.service;

public interface WalletService {

  void deposit(String accountName, int amount);

  int getFunds(String accountName);
}
