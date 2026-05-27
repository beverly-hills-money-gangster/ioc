package com.demo.container;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.container.samples.positive.dependency.application.AppSampleMainClass;
import com.demo.container.samples.positive.dependency.application.health.InMemoryDatasourceHealthCheck;
import com.demo.container.samples.positive.dependency.application.health.PingHealthCheck;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ComponentClassScannerTest {


  @Test
  public void testRealApp() {
    var container = ContainerInitializer.init(AppSampleMainClass.class);
    var main = container.getInstance(AppSampleMainClass.class);

    main.createAccount("abc");
    main.deposit("abc", 15);
    int total = main.deposit("abc", 25);
    assertEquals(40, total);
    assertEquals(Map.of(
            InMemoryDatasourceHealthCheck.class, true,
            PingHealthCheck.class, true),
        main.getHealth());
  }

}
