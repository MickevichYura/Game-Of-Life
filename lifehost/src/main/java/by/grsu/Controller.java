package by.grsu;

import by.grsu.model.State;
import by.grsu.model.Task;
import by.grsu.repository.StateRepository;
import by.grsu.repository.TaskRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    private final String BASE_URL = "http://localhost:8091/";

    private final int HOST_ID = 1;

    @Autowired
    @Qualifier("taskRepository")
    private TaskRepository taskRepository;

    @Autowired
    @Qualifier("stateRepository")
    private StateRepository stateRepository;

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public State connect(@RequestBody String hostIp) {
        String sUrl = BASE_URL + "connect";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("hostId", hostIp);

        State state = restTemplate.postForObject(sUrl, params, State.class);

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

    private ResponseEntity<State> push(String hostId, String taskId, String partId, String lifeData) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("hostId", hostId);
        params.set("taskId", taskId);
        params.set("partId", partId);
        params.set("lifeData", lifeData);
        String uri = "http://localhost:8091/push";
        Task task = taskRepository.save(new Task(Long.parseLong(taskId) + 1, Long.parseLong(hostId), lifeData));
        System.out.println(task);
        return new RestTemplate().postForEntity(uri, params, State.class);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public State task(@RequestParam String hostId, @RequestParam String taskId, @RequestParam String partId,
                      @RequestParam String partSize, @RequestParam String maxSteps, @RequestParam String lifeData) {

        if (Long.parseLong(hostId) != HOST_ID) {
            return new State(false, true, "wrong host id");
        }

        taskRepository.save(new Task(Long.parseLong(taskId), Long.parseLong(hostId), lifeData));
        Task task = taskRepository.findOne(Long.parseLong(taskId));
        System.out.println(task);

        (new Thread(() -> startTask(hostId, taskId, Integer.parseInt(partId),
                Integer.parseInt(partSize), Integer.parseInt(maxSteps), lifeData))).start();

        return new State(false, false, "ok");
    }

    private List<String> evaluateSteps(String initialLifeData, int stepsCount) {
        Gson gson = new Gson();
        int[][] cells = gson.fromJson(initialLifeData, int[][].class);
        List<String> matrixSteps = new ArrayList<>();
        int i = 0;
        while (i < stepsCount) {
            cells = performMatrix(cells);
            matrixSteps.add(gson.toJson(cells));
            i++;
        }
        return matrixSteps;
    }

    private void startTask(String hostId, String taskId, int partId, int partSize, int maxSteps, final String lifeData) {
        int currentStep = 0;
        String cells = lifeData;
        int stepsCount;
        do {
            currentStep++;
            stepsCount = (maxSteps >= currentStep * partSize) ? partSize : maxSteps - (currentStep - 1) * partSize;
            List<String> part = evaluateSteps(cells, stepsCount);
            State state = push(hostId, taskId, String.valueOf(partId + currentStep - 1), new Gson().toJson(part)).getBody();
            cells = part.get(stepsCount - 1);
            if (state.isError()) stateRepository.save(state);
        } while ((currentStep ) * partSize < maxSteps);
    }

    private int[][] performMatrix(int[][] matrix) {
        int[][] newArr = new int[matrix.length][matrix.length];

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                int cell = matrix[x][y];
                int alives = aliveNeighbors(matrix, x, y);

                if (cell == 1) {
                    if (alives < 2 || alives > 3) {
                        newArr[x][y] = 0;
                    } else if (alives == 2 || alives == 3) {
                        newArr[x][y] = 1;
                    }
                } else {
                    if (alives == 3) newArr[x][y] = 1;
                    else newArr[x][y] = 0;
                }
            }
        }
        return newArr;
    }

    private int aliveNeighbors(int[][] arr, int x, int y) {
        if (x > 0 && y > 0 && x < arr.length - 1 && y < arr.length - 1) {
            return arr[x - 1][y - 1] +
                    arr[x][y - 1] +
                    arr[x + 1][y - 1] +
                    arr[x - 1][y] +
                    arr[x + 1][y] +
                    arr[x - 1][y + 1] +
                    arr[x][y + 1] +
                    arr[x + 1][y + 1];
        } else {
            return 0;
        }
    }

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public State ping(@RequestBody int hostId) {
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

}
