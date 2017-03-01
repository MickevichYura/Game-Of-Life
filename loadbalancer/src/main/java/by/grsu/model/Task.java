//package by.grsu.model;
//
//import org.springframework.data.annotation.Id;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "tasks")
//public class Task implements Serializable {
//
////    @JoinColumn(name = "hosts")
//    private Host hostId;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long taskId;
//
////    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long partId;
//
//    @Column(name = "lifeData")
//    private String lifeData;
//
//    protected Task() {
//    }
//
//    public Host getHostId() {
//        return hostId;
//    }
//
//    public long getTaskId() {
//        return taskId;
//    }
//
//    public long getPartId() {
//        return partId;
//    }
//
//    public String getLifeData() {
//        return lifeData;
//    }
//}
