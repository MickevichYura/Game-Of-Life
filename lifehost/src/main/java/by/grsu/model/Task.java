package by.grsu.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    @Id
    @Column(name = "task_id")
    private long taskId;

    @Column(name = "host_id")
    private long hostByHostId;

    @Column(name = "part_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long partId;

    @Column(name = "life_data")
    @Lob
    private String lifeData;

    public Task() {
    }

    public Task(long taskId, long hostByHostId, String lifeData) {
        this.taskId = taskId;
        this.hostByHostId = hostByHostId;
        this.lifeData = lifeData;
    }

    public long getHostByHostId() {
        return hostByHostId;
    }

    public void setHostByHostId(long hostByHostId) {
        this.hostByHostId = hostByHostId;
    }

    public long getTaskId() {
        return taskId;
    }


    public long getPartId() {
        return partId;
    }


    public String getLifeData() {
        return lifeData;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public void setPartId(long partId) {
        this.partId = partId;
    }

    public void setLifeData(String lifeData) {
        this.lifeData = lifeData;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", hostByHostId=" + hostByHostId +
                ", partId=" + partId +
                ", lifeData='" + lifeData + '\'' +
                '}';
    }
}
