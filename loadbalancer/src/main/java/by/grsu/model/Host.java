package by.grsu.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;


@Entity
@Table(name = "hosts")
public class Host implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "host_id")
    private long hostId;

    @Column(name = "ip")
    private String ip;

    @OneToMany(mappedBy = "hostByHostId")
    private Collection<Task> tasksByHostId;


    protected Host() {
    }

    public Host(String ip) {
        this.ip = ip;
    }

    public Collection<Task> getTasksByHostId() {
        return tasksByHostId;
    }


    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "{" +
                "hostId=" + hostId +
                ", ip='" + ip + '\'' +
                '}';
    }
}
