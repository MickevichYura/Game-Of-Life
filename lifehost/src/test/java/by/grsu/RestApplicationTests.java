package by.grsu;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RestApplicationTests {

//	@Mock
//	Service

    private MockMvc mockMvc;

    @InjectMocks
    private Controller lifeHost;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lifeHost).build();
    }

    @Test
    public void connectToLoadBalancerTest() throws Exception {
        mockMvc.perform(get("/connect").param("hostIp", "192.168.100.1"))
                .andExpect(jsonPath("$.message", is("192.168.100.1")));
    }

    @Test
    public void disconnectFromHostTest() throws Exception {
        mockMvc.perform(get("/disconnect1").param("hostId", "11")).andExpect(jsonPath("$.message", is("wrong host id")));
    }
//
//    @Test
//    public void pingHostTest() throws Exception {
//        mockMvc.perform(get("/ping").param("hostId", "0"))
//                .andExpect(jsonPath("$.message", is("is free")))
//                .andExpect(jsonPath("$.result", is(true)));
//    }
//
//
//    @Test
//    public void taskHostTest() throws Exception {
//        mockMvc.perform(get("/task")
//                .param("hostId", "0").param("taskId", "0").param("partId", "0").param("lifeData", "0"))
//				.andExpect(jsonPath("$.message", is("is busy")))
//                .andExpect(jsonPath("$.result", is(false)));
//    }
}
