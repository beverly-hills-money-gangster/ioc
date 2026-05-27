package com.demo.container.samples.positive.dependency.application.repo;

import com.demo.annotation.Component;
import com.demo.container.samples.positive.dependency.application.datasource.Datasource;

@Component
public class BankAccountRepositoryImpl implements BankAccountRepository {

  private final Datasource datasource;

  public BankAccountRepositoryImpl(Datasource datasource) {
    this.datasource = datasource;
  }

  @Override
  public void createAccount(String accountName) {
    if (datasource.get(accountName) != null) {
      throw new IllegalStateException("Account %s already exists".formatted(accountName));
    }
    datasource.set(accountName, 0);
  }
}
