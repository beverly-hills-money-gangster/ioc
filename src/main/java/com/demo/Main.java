package com.demo;

import com.demo.container.ContainerInitializer;

// TODO use maven wrapper
// TODO add javadoc
// TODO make clean DDD design
// TODO add more tests
// TODO add gitignore
// TODO add readme
// TODO configure circle ci
// TODO introduce @PostConstruct
// TODO close all closeables on exit
// TODO don't support Sets. Support List only
// TODO integrate with logback
// TODO read more on reflection, annotation, spring, tree and cycles
public class Main {

  static void main() {
    try (var container = new ContainerInitializer().init(Main.class)) {

    }
  }
}
