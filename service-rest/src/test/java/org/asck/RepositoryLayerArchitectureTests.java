package org.asck;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.springframework.stereotype.Repository;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class RepositoryLayerArchitectureTests {

	@ArchTest
	public static final ArchRule repositoryClassesShouldOnlyBeAccessedByServiceClasses = classes().that()
			.resideInAPackage("org.asck.repository").should().onlyBeAccessed().byAnyPackage(
					"org.asck.service", "org.asck.service.impl");

	@ArchTest
	public static final ArchRule classesThatAnnotatedWithSpringRepositoryAnnotationShouldResideInPackageXRepository = classes()
			.that().areAnnotatedWith(Repository.class).should()
			.resideInAPackage("org.asck.repository");

	@ArchTest
	public static final ArchRule repositoryClassesShouldBeNamedXRepository = classes().that()
			.resideInAPackage("org.asck.service.repository").should()
			.haveSimpleNameEndingWith("Repository");

	@ArchTest
	public static final ArchRule repositoryClassesShouldBeAnnotatedWithRepository = classes().that()
			.resideInAPackage("org.asck.service.repository").should()
			.beAnnotatedWith(Repository.class);

}
