package edu.uoc.epcsd.user.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import edu.uoc.epcsd.user.domain.DigitalSession;

public class DigitalSessionUnitTest {

    @Test
    public void testDigitalSessionLinkIsNotNull() {

        Long id = 1L;
        String description = "fake description";
        String location = "fake location";
        String link = "http://fakelink.com";
        Long userId = 2L;


        DigitalSession digitalSession = DigitalSession.builder()
                .id(id)
                .description(description)
                .location(location)
                .link(link)
                .userId(userId)
                .build();

        // Assertion
        assertNotNull(digitalSession.getLink(), "The link should not be null");
    }
}
