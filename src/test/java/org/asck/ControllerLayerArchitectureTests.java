package org.asck;

import static com.tngtech.archunit.base.DescribedPredicate.dont;
import static com.tngtech.archunit.core.domain.properties.HasModifiers.Predicates.modifier;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.have;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class ControllerLayerArchitectureTests {

	@ArchTest
	public static final ArchRule classesThatResidesInControllerPackageAndAnnotatedWithRestControllerShouldEndWithController = classes().that()
			.resideInAPackage("..controller").and().areAnnotatedWith(RestController.class).should()
			.haveSimpleNameEndingWith("Controller");
	
	@ArchTest
	public static final ArchRule classesThatResidesInControllerPackageAndAnnotatedWithControllerShouldEndWithController = classes().that()
			.resideInAPackage("..controller").and().areAnnotatedWith(Controller.class).should().haveSimpleNameEndingWith("Controller");

	@ArchTest
	public static final ArchRule controllerClassesShouldHaveSpringRestControllerAnnotation = classes().that()
			.resideInAPackage("..controller").and().haveSimpleNameEndingWith("Controller").and(dont(have(modifier(JavaModifier.ABSTRACT)))).should()
			.beAnnotatedWith(RestController.class).orShould().beAnnotatedWith(Controller.class);
	
	@ArchTest
	public static final ArchRule classesThatAnnotatedWithSpringRestControllerAnnotationShouldResideInPackageXController = classes()
			.that().areAnnotatedWith(RestController.class).or().areAnnotatedWith(Controller.class).should()
			.resideInAPackage("..controller");


}
