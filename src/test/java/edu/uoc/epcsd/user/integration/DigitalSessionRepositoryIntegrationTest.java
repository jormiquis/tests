package edu.uoc.epcsd.user.integration;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.DigitalSessionRepositoryImpl;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.UserRepositoryImpl;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.service.DigitalSessionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DigitalSessionRepositoryIntegrationTest {

    @Autowired
    private DigitalSessionRepositoryImpl digitalSessionRepository;

    @Autowired
    private UserRepositoryImpl UserRepository;


    @Test
    public void whenAddingDigitalSession_thenItCanBeFoundByUser() {

        User testUser = this.UserRepository.findUserById(1L).get();

        DigitalSession newSession = new DigitalSession(
            2L,
            "fake description",
            "fake location",
            "http://fake-link.es",
            testUser.getId()
            );

        Long sessionId = digitalSessionRepository.addDigitalSession(newSession);

        List<DigitalSession> sessions = digitalSessionRepository.findDigitalSessionByUser(newSession.getUserId());

        assertThat(sessions).isNotEmpty();
        assertThat(sessions).anyMatch(session -> session.getId().equals(sessionId));
        assertThat(sessions).anyMatch(session -> session.getDescription().equals(newSession.getDescription()));
        assertThat(sessions).anyMatch(session -> session.getLocation().equals(newSession.getLocation()));
        assertThat(sessions).anyMatch(session -> session.getLink().equals(newSession.getLink()));
    }
}