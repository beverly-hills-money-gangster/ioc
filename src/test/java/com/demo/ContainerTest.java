package com.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.demo.container.ContainerInitializer;
import com.demo.container.samples.positive.dependency.application.AppSampleMainClass;
import com.demo.container.samples.positive.dependency.application.datasource.Datasource;
import com.demo.container.samples.positive.dependency.application.datasource.InMemoryDatasource;
import com.demo.container.samples.positive.dependency.application.health.InMemoryDatasourceHealthCheck;
import com.demo.container.samples.positive.dependency.application.health.PingHealthCheck;
import com.demo.container.samples.positive.dependency.primary.concrete.SomeComponent;
import com.demo.container.samples.positive.dependency.primary.concrete.SomePrimaryComponent;
import com.demo.container.samples.positive.dependency.primary.intrfc.SomePrimaryServiceImpl;
import com.demo.container.samples.positive.dependency.primary.intrfc.SomeService;
import com.demo.container.samples.positive.independent.IndependentComponent;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ContainerTest {


  @Test
  public void testRealApp() {
    try (var container = new ContainerInitializer("default").init(AppSampleMainClass.class)) {
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

  @Test
  public void testRealAppSpyClosable() throws IOException {
    var initializer = new ContainerInitializer("default");
    initializer.addDecorator(InMemoryDatasource.class, Mockito::spy);
    Datasource datasource;
    try (var container = initializer.init(AppSampleMainClass.class)) {
      datasource = container.getInstance(InMemoryDatasource.class);
    }
    verify(datasource).close();
  }

  @Test
  public void testRealAppGetNotExistingComponent() {
    var ex = assertThrows(IllegalStateException.class, () -> {
      try (var container = new ContainerInitializer("default").init(AppSampleMainClass.class)) {
        container.getInstance(Thread.class);
      }
    });
    assertEquals("No component of type java.lang.Thread found", ex.getMessage());
  }

  @Test
  public void testMissingAbstractDependency() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.missing.abstrc"));
    assertEquals(
        "Only 1 instance of com.demo.container.samples.negative.missing.abstrc.AbstractDependency expected. Got 0 registered.",
        ex.getMessage());
  }

  @Test
  public void testMissingAnnotationDependency() {
    var ex = assertThrows(IllegalStateException.class,
        () -> new ContainerInitializer().init(
            "com.demo.container.samples.negative.missing.component"));
    assertEquals(
        "Class com.demo.container.samples.negative.missing.component.MainComponent dependency com.demo.container.samples.negative.missing.component.SomeDependency is not registered",
        ex.getMessage());
  }

  @Test
  public void testInvalidMissingCollectionArray() {
    var ex = assertThrows(IllegalStateException.class,
        () -> new ContainerInitializer().init(
            "com.demo.container.samples.negative.missing.collection.array"));
    assertEquals("Can't find instance of class java.lang.Thread", ex.getMessage());
  }

  @Test
  public void testInvalidMissingCollectionList() {
    var ex = assertThrows(IllegalStateException.class,
        () -> new ContainerInitializer().init(
            "com.demo.container.samples.negative.missing.collection.list"));
    assertEquals("Can't find instance of class java.lang.Thread", ex.getMessage());
  }

  @Test
  public void testInvalidConstructorTwoConstructors() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.constructor.two"));
    assertTrue(ex.getMessage().startsWith("Only one constructor is expected for class"));
  }

  @Test
  public void testInvalidConstructorEnum() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.constructor.enumeration"));
    assertTrue(ex.getMessage().endsWith("constructor can't have enum params"));
  }

  @Test
  public void testInvalidConstructorPrimitive() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.constructor.primitive"));
    assertTrue(ex.getMessage().endsWith("constructor can't have primitive params"));
  }

  @Test
  public void testInvalidConstructorPrivate() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.constructor.priv"));
    assertTrue(ex.getMessage().startsWith("No constructor available for class"));
  }

  @Test
  public void testInvalidConstructorSet() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.constructor.set"));
    assertTrue(ex.getMessage()
        .endsWith("constructor can't have any type of collection parameter other than List"));
  }

  @Test
  public void testInvalidStructureAbstract() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.structure.abstr"));
    assertTrue(ex.getMessage().startsWith("Abstract class can't be a component"));
  }

  @Test
  public void testInvalidStructureAmbiguous() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.structure.ambiguous"));
    assertEquals(
        "Only 1 instance of com.demo.container.samples.negative.invalid.structure.ambiguous.InterfaceDependencyComponent expected. Got 2 registered.",
        ex.getMessage());
  }


  @Test
  public void testInvalidStructureProfile() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.structure.profile"));
    assertEquals(
        "Class com.demo.container.samples.negative.invalid.structure.profile.InvalidProfileComponent is profiled but no profile is specified",
        ex.getMessage());
  }

  @Test
  public void testInvalidStructureInterface() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.structure.intrfc"));
    assertTrue(ex.getMessage().startsWith("Interface can't be a component"));
  }

  @Test
  public void testInvalidStructureCycleSelfRef() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.structure.cycle.selfref"));
    assertTrue(ex.getMessage().startsWith("Component can't be dependent on itself."));
  }

  @Test
  public void testInvalidStructureCycleSmall() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.structure.cycle.small"));
    assertTrue(
        ex.getMessage().startsWith("Can't traverse dependency graph. Likely a cycle detected."));
  }

  @Test
  public void testInvalidStructureCycleBig() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.invalid.structure.cycle.big"));
    assertTrue(
        ex.getMessage().startsWith("Can't traverse dependency graph. Likely a cycle detected."));
  }

  @Test
  public void testInvalidMissingAnnotation() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.negative.missing.annotation")) {
      assertTrue(container.isEmpty());
    }
  }

  @Test
  public void testInvalidMissingFiles() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.negative.missing.files")) {
      assertTrue(container.isEmpty());
    }
  }

  @Test
  public void testInvalidSMissingDependency() {
    var ex = assertThrows(IllegalStateException.class, () -> new ContainerInitializer().init(
        "com.demo.container.samples.negative.missing.dependency"));
    assertEquals(
        "Class com.demo.container.samples.negative.missing.dependency.MainComponent dependency com.demo.container.samples.negative.missing.dependency.MissingComponent is not registered",
        ex.getMessage());
  }

  @Test
  public void testPositiveIndependentComponent() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.independent")) {
      assertNotNull(container.getInstance(IndependentComponent.class));
    }
  }

  @Test
  public void testPositiveProfileProd() {
    try (var container = new ContainerInitializer("prod").init(
        "com.demo.container.samples.positive.dependency.profile")) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.profile.MainComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.profile.ProdDependency.class));
    }
  }

  @Test
  public void testPositiveProfileTest() {
    try (var container = new ContainerInitializer("test").init(
        "com.demo.container.samples.positive.dependency.profile")) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.profile.MainComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.profile.TestDependency.class));
    }
  }

  @Test
  public void testPositiveProfileDefault() {
    try (var container = new ContainerInitializer("default").init(
        "com.demo.container.samples.positive.dependency.profile")) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.profile.MainComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.profile.DefaultDependency.class));
    }
  }

  @Test
  public void testPositiveDependencyAbstractComponent() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.dependency.abstrct")) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.abstrct.MainComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.abstrct.DependencyComponentImpl.class));
    }
  }

  @Test
  public void testPositiveDependencyInterfaceComponent() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.dependency.intrfc")) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.intrfc.MainComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.intrfc.DependencyComponentImpl.class));
    }
  }

  @Test
  public void testPositiveDependencyCollectionList() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.dependency.collection.list")) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.list.MainComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.list.DependencyComponent1.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.list.DependencyComponent2.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.list.DependencyComponent3.class));
    }
  }

  @Test
  public void testPositiveDependencyCollectionArray() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.dependency.collection.array")) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.array.MainComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.array.DependencyComponent1.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.array.DependencyComponent2.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.collection.array.DependencyComponent3.class));
    }
  }

  @Test
  public void testPrimaryInterface() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.dependency.primary.intrfc")) {
      assertInstanceOf(SomePrimaryServiceImpl.class, container.getInstance(SomeService.class));
    }
  }

  @Test
  public void testPrimaryConcrete() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.dependency.primary.concrete")) {
      assertInstanceOf(SomePrimaryComponent.class, container.getInstance(SomeComponent.class));
    }
  }

  @Test
  public void testMultiPackageScan() {
    try (var container = new ContainerInitializer().init(
        "com.demo.container.samples.positive.dependency.primary.intrfc",
        "com.demo.container.samples.positive.dependency.primary.concrete",
        "com.demo.container.samples.positive.independent"
    )) {
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.primary.intrfc.SomeService.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.dependency.primary.concrete.SomeComponent.class));
      assertNotNull(container.getInstance(
          com.demo.container.samples.positive.independent.IndependentComponent.class));
    }
  }


  @Test
  public void testNegativePrimaryInterface() {
    var ex = assertThrows(IllegalStateException.class, () -> {
      try (var container = new ContainerInitializer().init(
          "com.demo.container.samples.negative.invalid.structure.primary.intrfc")) {
        container.getInstance(
            com.demo.container.samples.negative.invalid.structure.primary.intrfc.SomeService.class);
      }
    });
    assertTrue(ex.getMessage().startsWith("Ambiguous dependency"));
  }

}