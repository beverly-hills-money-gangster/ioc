package com.demo;

import com.demo.container.ContainerInitializer;

// TODO add javadoc
// TODO make clean DDD design
// TODO add more tests
// TODO add gitignore
// TODO add readme
// TODO configure circle ci
// TODO introduce @PostConstruct
// TODO close all closeables on exit
public class Main {

  static void main() {
    ContainerInitializer.init(Main.class);
  }
}
