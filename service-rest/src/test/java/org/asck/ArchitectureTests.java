package org.asck;

import org.junit.runner.RunWith;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchRules;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = { "org.asck" }, importOptions = { ImportOption.DontIncludeTests.class })
public class ArchitectureTests {

	@ArchTest
	public static final ArchRules layered_tests = ArchRules.in(LayeredArchitectureTest.class);

	@ArchTest
	public static final ArchRules controller_layer_tests = ArchRules.in(ControllerLayerArchitectureTests.class);

	@ArchTest
	public static final ArchRules service_layer_tests = ArchRules.in(ServiceLayerArchitectureTests.class);

	@ArchTest
	public static final ArchRules repository_layer_tests = ArchRules.in(RepositoryLayerArchitectureTests.class);

	@ArchTest
	public static final ArchRule interfaces_must_not_be_placed_in_implementation_packages = ArchRuleDefinition
			.noClasses().that().resideInAPackage("..impl..").should().beInterfaces();
	
	@ArchTest
	public static final ArchRules coding_rules = ArchRules.in(CodingRulesTests.class);

}
