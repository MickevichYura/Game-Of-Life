package by.grsu;

import by.grsu.model.Host;
import by.grsu.repository.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@EnableAutoConfiguration
@RestController
public class LoadBalancer {

    @Autowired
    @Qualifier("hostRepository")
    private HostRepository hostRepository;

    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public State connect(@RequestParam String ip) {

        String message = "wrong ip";
        boolean result = true;
        boolean isError = false;

        Host host = hostRepository.save(new Host(ip));

        return new State(result, isError, String.valueOf(host.getHostId()));

    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    public State disconnectHost(@RequestBody int hostId) {
        String sUrl = "http://localhost:8090/" + "disconnect";

        RestTemplate restTemplate = getRestTemplate();
        HttpEntity<Integer> request = new HttpEntity<>(hostId, getHttpHeaders());

        return restTemplate.postForObject(sUrl, request, State.class);
    }

    private RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    private MultiValueMap<String, String> getHttpHeaders(){
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        return headers;
    }

    @RequestMapping("/push")
    public State push(@RequestParam int hostId, @RequestParam int taskId,
                      @RequestParam int partId, @RequestParam String lifeData) {

        String message = "wrong ip";
        boolean result = false;
        boolean isError = true;

        if (hostId == 0) {
            message = "wrong hostId";
        }
        if (taskId == 10) {
            message = "wrong taskId";
        }
        if (partId == 5) {
            message = "duplicate partId";
        }

        return new State(result, isError, message);

    }

    @RequestMapping("/task")
    public State sendTask(@RequestParam int hostId, @RequestParam int taskId,
                          @RequestParam int partId, @RequestParam String lifeData) {

        String sUrl = "http://localhost:8090/" + "task?hostId=" + hostId + "&taskId=" + taskId + "&partId=" + partId
                + "&lifeData=" + lifeData;
        RestTemplate restTemplate = new RestTemplate();

        State state = restTemplate.getForObject(sUrl, State.class);
        return state;
    }

    @RequestMapping("/ping")
    public State ping(@RequestParam int hostId) {
        String sUrl = "http://localhost:8090/" + "ping?hostId=" + hostId;
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(sUrl, State.class);
    }

}
