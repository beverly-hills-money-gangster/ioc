package com.demo.container.samples.positive.dependency.application;

import com.demo.annotation.Component;
import com.demo.container.samples.positive.dependency.application.health.HealthCheck;
import com.demo.container.samples.positive.dependency.application.service.BankAccountService;
import com.demo.container.samples.positive.dependency.application.service.WalletService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppSampleMainClass {

  private final BankAccountService bankAccountService;
  private final WalletService walletService;
  private final List<HealthCheck> healthChecks;

  public AppSampleMainClass(
      BankAccountService bankAccountService,
      WalletService walletService,
      List<HealthCheck> healthChecks) {
    this.bankAccountService = bankAccountService;
    this.walletService = walletService;
    this.healthChecks = healthChecks;
  }

  public Map<Class<?>, Boolean> getHealth() {
    Map<Class<?>, Boolean> health = new HashMap<>();
    for (HealthCheck healthCheck : healthChecks) {
      health.put(healthCheck.getClass(), healthCheck.isHealthy());
    }
    return health;
  }

  public void createAccount(String accountName) {
    bankAccountService.createAccount(accountName);
  }

  public int deposit(String accountName, int amount) {
    walletService.deposit(accountName, amount);
    return walletService.getFunds(accountName);
  }
}
