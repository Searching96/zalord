package io.zalord;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ZalordApplicationTests {

	ApplicationModules modules = ApplicationModules.of(ZalordApplication.class);

	@Test 
	void verifyArchitecturalBoundaries() {
		// This single line will fail the build if a module boundary is violated
		modules.verify();
	}

	@Test
	void writeDocumentationSnippets() {
		// This generates UML diagrams of all bounded contexts automatically
		new Documenter(modules).writeModulesAsPlantUml();
	}
}
