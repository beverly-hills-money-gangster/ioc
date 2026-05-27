package com.demo.container.samples.positive.dependency.application.repo;

import com.demo.annotation.Component;
import com.demo.container.samples.positive.dependency.application.datasource.Datasource;

@Component
public class WalletRepositoryImpl implements WalletRepository {

  private final Datasource datasource;

  public WalletRepositoryImpl(Datasource datasource) {
    this.datasource = datasource;
  }

  @Override
  public void deposit(String accountName, int amount) {
    Integer currentAmount = datasource.get(accountName);
    if (currentAmount == null) {
      throw new IllegalStateException("Not existing account %s".formatted(accountName));
    }
    datasource.set(accountName, currentAmount + amount);
  }

  @Override
  public int getFunds(String accountName) {
    Integer currentAmount = datasource.get(accountName);
    if (currentAmount == null) {
      throw new IllegalStateException("Not existing account %s".formatted(accountName));
    }
    return currentAmount;
  }
}
