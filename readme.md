## Architecture of docker-compose

```txt
┌─────────────────────────────────┐ ┌───────────────────┐
│app-network                      │ │monitoring         │
│ ┌──────────┐ ┌──────────┐ ┌─────┴─┴────┐ ┌──────────┐ │
│ │          │ │          │ │            │ │          │ │
│ │ DataBase ├─┤ Exporter ├─┤ Prometheus ├─┤ Grafana  │ │
│ │          │ │          │ │            │ │          │ │
│ └────┬─────┘ └──────────┘ └─────┬─┬────┘ └──────────┘ │
│      │                          │ │                   │
│ ┌────┴─────┐                    │ └───────────────────┘
│ │          │                    │
│ │ BackEnd  │                    │  
│ │          │                    │
│ └────┬─────┘                    │
│      │                          │
│ ┌────┴─────┐                    │
│ │          │                    │
│ │ FrontEnd │                    │
│ │          │                    │
│ └──────────┘                    │
│                                 │
└─────────────────────────────────┘


    ┌────────────────────────────────────────────────────────────────────────────────┐
    │                                                                                │
    │ resa_net                                                                       │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                                                                                │
    │                           ┌────────────┐                                       │
    └───────────────────────────┤            ├───────────────────────────────────────┘
                                │ Prometheus │
    ┌───────────────────────────┤            ├───────────────────────────────────
    │app-network                └┬────┬───┬─┬┘ monitoring
    │ ┌──────────┐ ┌──────────┐  │    │   │ │  ┌──────────┐
    │ │          │ │          │  │    │   │ │  │          │
    │ │ DataBase ├─┤ Exporter ├──┘    │   │ └──┤ Grafana  │
    │ │          │ │          │       │   │    │          │
    │ └────┬─────┘ └──────────┘       │   │    └──────────┘
    │      │                          │   │
    │ ┌────┴─────┐                    │   │    ┌──────────┐
    │ │          │                    │   │    │          │
    │ │ BackEnd  │                    │   │    │ Alerting │
    │ │          │                    │   │    │          │
    │ └────┬─────┘                    │   │    └──────────┘
    │      │                          │   │
    │ ┌────┴─────┐                    │   │
    │ │          │                    │   │
    │ │ FrontEnd │                    │   │
    │ │          │                    │   │
    │ └──────────┘                    │
    │                                 │
    └─────────────────────────────────┘
```

# Links to dashboards:

[Grafana](http://129.151.248.155:3000/) (id: toudard pwd: 1234)


# TP1 - Prometheus et Grafana


You can access our [Grafana](http://129.151.248.155:3000/) interface hosted on a VPS.


1. What's the difference between the system CPU usage and the process CPU usage ?

    The process CPU usage corresponds to the CPU being used by a defined process. (for example the usage of the total CPU Used by the JVM)

    The System CPU Usage corresponds to the global system CPU Usage (the amount of the hardware CPU being used).

    <img src="https://drive.google.com/uc?id=1ty3yTyZls7YvbsbEKaM4XNnelK_NhUEu" alt="Graph-CPUUsage"/>

    Here is a graph of the CPU Usage in Grafana, for both System CPU Usage and process (in this case JVM) CPU Usage.

2. What represents the CPU load ?

    The CPU load represents the number of processes running on the CPU, if this number becomes Higher than the number of available CPUs, the computing time will start to increase, because processes will have to wait for others to be finished and to make a thread available.

    <img src="https://drive.google.com/uc?id=1O0MzyDirBOPeIhq8AO1Wblif9y_cwJ6w" alt="Graph-CPULoad"/>

    On this graph we can see that there was probably some slowdown happening, because the number of needed threads by the system was higher than the available CPUs on the machine.

3. What are all those types of threads ?

    Daemon vs non-Daemon: non-Daemon can also be called a User Thread, the main thread is a User thread and all threads are by default user threads if the Daemon property is not set to true via "thread.setDaemon(true);". The main difference between Daemon and non-Daemon threads are that even if the main thread exits, User threads will keep running until the end of their lifecycle and Daemon threads will terminate once all the user threads are done.

4. What do they all mean (Thread States)?

    - Runnable: A thread executing in the Java virtual machine
    - Blocked: A thread that is blocked waiting for a monitor lock
    - Waiting: A thread that is waiting indefinitely for another thread to perform a particular action
    - Timed-Waiting: A thread that is waiting for another thread to perform an action for up to a specified waiting time
    - New: A thread that has not yet started
    - Terminated: A thread that has exited

5. Memory max / used / committed ?

    - max: represents the maximum amount of memory that can be used for memory management. Its value may be undefined. The maximum amount of memory may change over time if defined. The amount of used and committed memory will always be less than or equal to max if max is defined. A memory allocation may fail if it attempts to increase the used memory such that used > committed even if used <= max would still be true (for example, when the system is low on virtual memory)

    - used: represents the amount of memory currently used

    - commited: represents the amount of memory that is guaranteed to be available for use by the Java virtual machine. The amount of committed memory may change over time (increase or decrease). The Java virtual machine may release memory to the system and committed could be less than init. committed will always be greater than or equal to used.

        Source: [JavaDoc](https://docs.oracle.com/javase/9/docs/api/java/lang/management/MemoryUsage.html)

Configuration Prometheus:
    <img src="https://drive.google.com/uc?id=1GZKcTlWJYHg-s0dyjADlAUZUyyo5Lnj_" alt="Conf Prometheus docker-compose"/>    
    ```
    global:
  scrape_interval: 2s
  scrape_timeout: 1s

rule_files:
  - "rules.yml"

alerting:
  alertmanagers:
    - static_configs:
      - targets:
        - "alerting:9093"

scrape_configs:
  - job_name: "Prometheus"
    metrics_path: "/metrics"
    honor_labels: false
    honor_timestamps: true
    sample_limit: 0
    static_configs:
    - targets:
      - "prometheus:9090"
      labels:
        group: "Prometheus"
    
  - job_name: "Sample-backend"
    metrics_path: "/api/actuator/prometheus"
    honor_labels: false
    honor_timestamps: true
    sample_limit: 0
    static_configs:
      - targets:
        - "frontend"
        labels:
          group: "Backend"

  - job_name: "Postgres"
    metrics_path: "/metrics"
    static_configs:
      - targets:
        - "postgres_exporter:9187"
        labels:
          group: "Database"
  
  - job_name: "Resa-backend"
    metrics_path: "/api/actuator/prometheus"
    static_configs:
      - targets:
        - "resa-backend:8080"
        labels:
          group: "Backend"

  - job_name: "Elasticsearch"
    metrics_path: "/metrics"
    static_configs:
      - targets:
        - "elasticsearch_exporter:9114"
        labels:
          group: "Elasticsearch"
```


# TP2 - ELK

1. Which exporter did you use ? Describe your configuration.

    We used elasticsearch-Exporter (quay.io/prometheuscommunity/elasticsearch-exporter:v1.3.0)

    

2. List the existing Beats in the Elastic world, and explain in 1-2 lines what data it is supposed to collect.

3. List the steps you followed and the command you run in order to set up Filebeat.

5. Explain how you managed to detect the problem, and how you fixed it or tried to fix it !

# TP3 - Load Tests

We didn't achieve to build the gatling project due to references problems, so we answered to the questions we could but we didn't actually load-test the application.

1. What are those parameters used for ?

    - GATLING_RAMP_USERS/nbUsers Corresponds to the number of users simulated during the load test.
    - GATLING_RAMP_DURATION/rampDuration Corresponds to the speed at which the simulated users will connect to the service.

3. What can companies do to both absorb those previsible spikes while limiting their costs ?

    - A good solution is to use scalable infrastructure, which will be provisionned with more ressources depending on the load it is under. Every cloud hosting provider provide solutions for this and since they work on the "pay what you use" principle, you only pay extra when the infrastructure is scaled up to absorb more load, thus you can limit the cost by only paying extra when needed.

4. What is the difference between "limits" and "reservations" ?

    - Reservation is the amount dedicated for the container on the machine, it's preallocated to make sure it is always available to use for the container.

    - Limit is the amount of ressources that the container can use.

5. Explain what those two parameters are used for (Xms/Xmx).

    - Those parameters are used to allocate the memory to the heap of the JVM
    - Xms corresponds to the amount of memory allocated to the heap at the start of the JVM.
    - Xmx corresponds to the maximum amount of memory the heap is allowed to use.

6. Explain what is a N+1 problem. Why is it happening in our case ?

    - A N+1 problem is happening when a component of the application needed is answering slower than what is expected, for example if you call a database with an API the time lost during the query can be considered a N+1 problem if the query is huge or the database engine can't keep up with the request.

7. What is EAGER fetchtype ? What's the difference with LAZY fetchtype ?

    - The EAGER fetchtype loads all the ressources at once. It's opposed to the LAZY fetchtype, which only loads ressources when needed.
