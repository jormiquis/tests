package edu.uoc.epcsd.user.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = {
     "edu.uoc.epcsd.user"
})
public class ArchitecturalTest {


    @ArchTest
    static final ArchRule onion_architecture_is_respected_with_exception = onionArchitecture()
            .domainModels("edu.uoc.epcsd.user.domain..")
            .domainServices("edu.uoc.epcsd.user.domain.service..")
            .applicationServices("..")
            .adapter("cli", "..")
            .adapter("persistence", "edu.uoc.epcsd.user.infrastructure.repository.jpa..")
            .adapter("rest", "edu.uoc.epcsd.user.application.rest..")
            .ignoreDependency(
                "edu.uoc.epcsd.user.domain.service.AlertServiceImpl",
                "edu.uoc.epcsd.user.application.rest.response.GetProductResponse")
            .ignoreDependency(
                "edu.uoc.epcsd.user.domain.service.DigitalSessionServiceImpl",
                "edu.uoc.epcsd.user.application.rest.response.GetUserResponseTest");

}
