package by.grsu;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class State implements Serializable {
    private boolean result;

    @SerializedName("error")
    private boolean isError;

    private String message;

    private State() {
    }

    public State(boolean result, boolean isError, String message) {
        this.result = result;
        this.isError = isError;
        this.message = message;
    }

    public State(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public boolean isError() {
        return isError;
    }

    public String getMessage() {
        return message;
    }
}
