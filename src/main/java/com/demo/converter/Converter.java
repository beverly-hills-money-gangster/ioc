package com.demo.converter;

@FunctionalInterface
public interface Converter<F, T> {

  T convert(F from);
}
