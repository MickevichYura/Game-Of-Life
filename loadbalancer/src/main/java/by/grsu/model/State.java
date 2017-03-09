package by.grsu.model;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "states")
public class State implements Serializable {
    @Id
    @Column(name = "state_id")
    private long stateId;

    @Column(name = "is_result")
    private boolean result;

    @SerializedName("error")
    @Column(name = "is_error")
    private boolean isError;

    @Column(name = "life_data")
    @Lob
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

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }
}
