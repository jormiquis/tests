package edu.uoc.epcsd.user.unit;

import edu.uoc.epcsd.user.domain.service.DigitalSessionServiceImpl;
import edu.uoc.epcsd.user.domain.service.UserNotFoundException;
import edu.uoc.epcsd.user.application.rest.response.GetUserResponseTest;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        MockitoAnnotations.openMocks(this);
        userId = 1L;

        Field field = DigitalSessionServiceImpl.class.getDeclaredField("getUserById");
        field.setAccessible(true);
        field.set(digitalSessionService, "http://fake-url.com/users/{id}");
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

    GetUserResponseTest mockedResponse = new GetUserResponseTest();
    mockedResponse.setId(userId);

    ResponseEntity<GetUserResponseTest> responseEntity = new ResponseEntity<>(mockedResponse, HttpStatus.OK);

    when(restTemplate.getForEntity(anyString(), eq(GetUserResponseTest.class), eq(userId)))
            .thenReturn(responseEntity);

    when(digitalSessionRepository.findDigitalSessionByUser(userId))
            .thenReturn(digitalSessions);

    List<DigitalSession> result = digitalSessionService.findDigitalSessionByUser(userId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Session 1", result.get(0).getDescription());
    assertEquals("Session 2", result.get(1).getDescription());
}
@Test
void findDigitalSessionByUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {

    when(restTemplate.getForEntity(anyString(), eq(GetUserResponseTest.class), eq(userId)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        digitalSessionService.findDigitalSessionByUser(userId);
    });

    assertEquals("User with idUser '" + userId + "' not found", exception.getMessage());
}
}