package org.asck;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

public class LayeredArchitectureTest {

	@ArchTest
	public static final ArchRule layer_dependencies_are_respected = Architectures.layeredArchitecture()
			.layer("Controllers").definedBy("org.asck.api.controller..")
			.layer("Services").definedBy("org.asck.api.service..")
			.layer("Repositories").definedBy("org.asck.api.repository..")
			
			.whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
			.whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers", "Services")
			.whereLayer("Repositories").mayOnlyBeAccessedByLayers("Services");

	
	
}
