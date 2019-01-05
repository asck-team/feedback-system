package org.asck;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class ControllerLayerArchitectureTests {

	@ArchTest
	public static final ArchRule classesThatResidesInControllerPackageShouldEndWithController = classes().that()
			.resideInAPackage("..controller").and().areAnnotatedWith(RestController.class).should()
			.haveSimpleNameEndingWith("Controller");

	@ArchTest
	public static final ArchRule controllerClassesShouldHaveSpringRestControllerAnnotation = classes().that()
			.resideInAPackage("..controller").and().haveSimpleNameEndingWith("Controller").should()
			.beAnnotatedWith(RestController.class);
	
	@ArchTest
	public static final ArchRule classesThatAnnotatedWithSpringRestControllerAnnotationShouldResideInPackageXController = classes()
			.that().areAnnotatedWith(RestController.class).should()
			.resideInAPackage("..controller");


}
