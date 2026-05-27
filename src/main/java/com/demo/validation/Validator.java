package com.demo.validation;

public interface Validator<T> {

  void validate(T t);
}
