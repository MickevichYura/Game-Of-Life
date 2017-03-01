package by.grsu.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "hosts")
public class Host implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "host_id")
    private long hostId;

    @Column(name = "ip")
    private String ip;

    protected Host(){

    }

    public Host(String ip) {
        this.ip = ip;
    }

    public long getHostId() {
        return hostId;
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
