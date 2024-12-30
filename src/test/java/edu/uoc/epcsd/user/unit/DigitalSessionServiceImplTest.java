package edu.uoc.epcsd.user.unit;

import edu.uoc.epcsd.user.domain.service.DigitalSessionServiceImpl;
import edu.uoc.epcsd.user.domain.service.UserNotFoundException;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.application.rest.response.GetUserResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.lang.reflect.Field;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DigitalSessionServiceImplTest {

        @Mock
    private DigitalSessionRepository digitalSessionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DigitalSessionServiceImpl digitalSessionService;

    private Long userId;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        userId = 1L;

        //make this for mocking getUserById
        Field field = DigitalSessionServiceImpl.class.getDeclaredField("getUserById");
        field.setAccessible(true);
        field.set(digitalSessionService, "http://mockedurl.com/users/{id}");
    }
    @Test
    void findDigitalSessionByUser_ShouldReturnDigitalSessions_WhenUserExists() {

        DigitalSession session1 = new DigitalSession();
        session1.setUserId(userId);
        session1.setDescription("Session 1");

        DigitalSession session2 = new DigitalSession();
        session2.setUserId(userId);
        session2.setDescription("Session 2");

        List<DigitalSession> digitalSessions = Arrays.asList(session1, session2);


        ResponseEntity<GetUserResponseTest> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(GetUserResponseTest.class), eq(userId)))
                .thenReturn(response);


        when(digitalSessionRepository.findDigitalSessionByUser(userId))
                .thenReturn(digitalSessions);


        List<DigitalSession> result = digitalSessionService.findDigitalSessionByUser(userId);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Session 1", result.get(0).getDescription());
        assertEquals("Session 2", result.get(1).getDescription());
    }

    @Test
    void findDigitalSessionByUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(restTemplate.getForEntity(anyString(), eq(GetUserResponseTest.class), eq(userId)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            digitalSessionService.findDigitalSessionByUser(userId);
        });

        assertEquals("User with idUser '"+ userId + "' not found", exception.getMessage());
    }
}