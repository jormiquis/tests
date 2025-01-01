package edu.uoc.epcsd.user.unit;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.service.DigitalSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DigitalSessionControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DigitalSessionService digitalSessionService;

    @Test
    public void testGetDigitalSessionByUser() throws Exception {
        Long userId = 1L;
        DigitalSession session1 = new DigitalSession(1L, "description1", "location1", "link1", userId);
        DigitalSession session2 = new DigitalSession(2L, "description2", "location2", "link2", userId);
        List<DigitalSession> sessions = Arrays.asList(session1, session2);

        Mockito.when(digitalSessionService.findDigitalSessionByUser(userId)).thenReturn(sessions);

        mockMvc.perform(get("/digital/digitalByUser")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description", is("description1")))
                .andExpect(jsonPath("$[1].description", is("description2")));
    }
}