package by.grsu;

import by.grsu.model.Host;
import by.grsu.model.State;
import by.grsu.repository.HostRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@ComponentScan
@EnableAutoConfiguration
@RestController
public class LoadBalancer {

    @Autowired
    @Qualifier("hostRepository")
    private HostRepository hostRepository;

    private Map<String, String> databaseStepsByTask;

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public State connect(@RequestBody String ip) {

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

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    private MultiValueMap<String, String> getHttpHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        return headers;
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public State push(@RequestParam String hostId, @RequestParam String taskId,
                      @RequestParam String partId, @RequestParam String lifeData) {

        String message = "wrong ip";
        boolean result = false;
        boolean isError = false;

        Gson gson = new Gson();
        if (databaseStepsByTask == null) {
            databaseStepsByTask = new HashMap<>();
        }
        databaseStepsByTask.put(partId, lifeData);
//        List<int[][]> collect = Arrays.stream(gson.fromJson(lifeData, String[].class)).map(a -> gson.fromJson(a, int[][].class)).collect(Collectors.toList());
        return new State(result, isError, message);
    }


    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public State sendTask(@RequestParam int taskId, @RequestParam String partId) {
        String part = databaseStepsByTask.get(partId);
        if (taskId == 1 && part != null) {
            return new State(true, false, part);
        }
        return new State(false, true, "wrong task or part");
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<State> sendTask(@RequestParam int taskId, @RequestParam int partId,
                                          @RequestParam int partSize, @RequestParam int maxSteps, @RequestParam String lifeData) {

        Host host = findFreeHost();
        String uri = "http://" + host.getIp() + ":8090/task";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("hostId", String.valueOf(host.getHostId()));
        params.set("taskId", String.valueOf(taskId));
        params.set("partId", String.valueOf(partId));
        params.set("partSize", String.valueOf(partSize));
        params.set("maxSteps", String.valueOf(maxSteps));
        params.set("lifeData", lifeData);
        return new RestTemplate().postForEntity(uri, params, State.class);
    }

    private Host findFreeHost() {
        Host host = new Host("localhost");
        host.setHostId(1);
        return host;
    }

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public State ping(@RequestBody int hostId) {
        String sUrl = "http://localhost:8090/" + "ping";
        RestTemplate restTemplate = getRestTemplate();
        HttpEntity<Integer> request = new HttpEntity<>(hostId, getHttpHeaders());

        return restTemplate.postForObject(sUrl, request, State.class);
    }

}
