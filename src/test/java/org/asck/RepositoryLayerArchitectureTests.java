package org.asck;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import javax.persistence.Entity;

import org.springframework.stereotype.Repository;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class RepositoryLayerArchitectureTests {

	@ArchTest
	public static final ArchRule classesThatAnnotatedWithSpringRepositoryAnnotationShouldResideInPackageXRepository = classes()
			.that().areAnnotatedWith(Repository.class).should()
			.resideInAPackage("..repository");

	@ArchTest
	public static final ArchRule repositoryClassesShouldBeNamedXRepository = classes().that()
			.resideInAPackage("..repository").should()
			.haveSimpleNameEndingWith("Repository");

	@ArchTest
	public static final ArchRule repositoryClassesShouldBeAnnotatedWithRepository = classes().that()
			.resideInAPackage("..repository").should()
			.beAnnotatedWith(Repository.class);
	
	@ArchTest
	public static final ArchRule repositoryModelClassesShouldBeAnnotatedWithEntity = classes().that()
			.resideInAPackage("..repository.model").and().haveSimpleNameEndingWith("TableModel").should()
			.beAnnotatedWith(Entity.class);

	@ArchTest
	public static final ArchRule repositoryModelClassesShouldHaveSimpleNameEndingWith = classes().that()
			.resideInAPackage("..repository.model").and().areAnnotatedWith(Entity.class).should().haveSimpleNameEndingWith("TableModel");
	

}
