package by.grsu;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RestApplicationTests {

//	@Mock
//	Service

    private MockMvc mockMvc;

    @InjectMocks
    private LoadBalancer loadBalancer;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loadBalancer).build();
    }

    @Test
    public void disconnectHostTest() throws Exception {
        mockMvc.perform(post("/disconnect")
                .content("0")
                .contentType(contentType))
                .andExpect(jsonPath("$.message", is("finish task first")));
    }

    @Test
    public void disconnectWrongHostTest() throws Exception {
        mockMvc.perform(post("/disconnect")
                .content("11")
                .contentType(contentType))
                .andExpect(jsonPath("$.message", is("wrong host id")));
    }

    @Test
    public void pingHostTest() throws Exception {
        mockMvc.perform(post("/ping")
                .content("1")
                .contentType(contentType))
                .andExpect(jsonPath("$.message", is("is free")))
                .andExpect(jsonPath("$.result", is(true)));
    }


    @Test
    public void taskHostTest() throws Exception {
        mockMvc.perform(get("/task")
                .param("hostId", "0").param("taskId", "0").param("partId", "0").param("lifeData", "0"))
                .andExpect(jsonPath("$.message", is("is busy")))
                .andExpect(jsonPath("$.result", is(false)));
    }
}
