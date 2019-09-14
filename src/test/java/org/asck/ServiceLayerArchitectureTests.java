package org.asck;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.springframework.stereotype.Service;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class ServiceLayerArchitectureTests {

	@ArchTest
	public static final ArchRule classesThatResidesInServicePackageShouldEndWithService = classes().that()
			.resideInAPackage("..service").should().haveSimpleNameEndingWith("Service");

	@ArchTest
	public static final ArchRule serviceClassesShouldHaveSpringServiceAnnotation = classes().that()
			.resideInAPackage("..service.impl").and().haveSimpleNameEndingWith("ServiceImpl").should()
			.beAnnotatedWith(Service.class);

	@ArchTest
	public static final ArchRule serviceClassesShouldBePackagePrivate = classes().that()
			.resideInAPackage("..service.impl").and().areAnnotatedWith(Service.class).should().bePackagePrivate();
	@ArchTest
	public static final ArchRule classesThatAnnotatedWithSpringServiceAnnotationShouldResideInPackageXService = classes()
			.that().areAnnotatedWith(Service.class).should().resideInAPackage("..service.impl");

}
