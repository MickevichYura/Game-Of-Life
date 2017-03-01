package by.grsu;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
    private final String BASE_URL = "http://localhost:8091/";

    private final int HOST_ID = 0;

    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public State connect(@RequestParam String hostIp) {
        String sUrl = BASE_URL + "connect?ip=" + hostIp;
        RestTemplate restTemplate = new RestTemplate();

        State state = restTemplate.getForObject(sUrl, State.class);

        System.out.println(state.getMessage());
        return state;
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    public State disconnect(@RequestBody int hostId) {
        String message;
        boolean result = false;
        boolean isError = true;

        if (hostId != HOST_ID) {
            message = "wrong host id";
        } else {
            result = true;
            isError = false;
            message = "finish task first";
        }

        return new State(result, isError, message);
    }

    @RequestMapping("/push")
    public State push() {
        int hostId = 0;
        int taskId = 11;
        int partId = 4;
        String lifeData = "0000011111";
        String sUrl = BASE_URL + "push?hostId=" + hostId + "&taskId=" + taskId + "&partId=" + partId
                + "&lifeData=" + lifeData;
        RestTemplate restTemplate = new RestTemplate();

        State state = restTemplate.getForObject(sUrl, State.class);

        return state;
    }

    @RequestMapping("/task")
    public State task(@RequestParam int hostId, @RequestParam int taskId,
                      @RequestParam int partId, @RequestParam String lifeData) {
        String message = "is busy";
        boolean result = false;
        boolean isError = true;

        if (hostId != HOST_ID) {
            message = "wrong host id";
            return new State(result, isError, message);
        }


        return new State(result, isError, message);
    }

    @RequestMapping("/ping")
    public State ping(@RequestParam int hostId) {
        String message;
        boolean result = false;
        boolean isError = true;

        if (hostId == HOST_ID) {
            result = true;
            isError = false;
            message = "is free";
        } else {
            message = "wrong host id";
        }
        return new State(result, isError, message);
    }

    private String performTask(String lifeData){
        return lifeData + "111000";
    }

}
