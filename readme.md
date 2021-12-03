# Reports of DevOps 2 Week OUDARD Thomas POULARD Antonin

## Usefull Links:

[Grafana](http://129.151.248.155:3000/) (id: toudard pwd: 1234) </br>
[Prometheus](http://129.151.248.155:9090/) </br>
[Elastic/Kibana](http://129.151.248.155:5601/) </br>
[Jaeger](http://129.151.248.155:16686/) </br>
[AlertManager](http://129.151.248.155:9093/)</br>
[SampleAppFrontEnd](http://129.151.248.155:80/)</br>
[Resa-SwaggerUI](http://129.151.248.155:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)</br>
[Last Gatling report generated on the VPS (50k Users just for fun)](http://129.151.248.155:8081)</br>
[Repo GIT](https://github.com/SfireInc/DevOps2)

(If the VPS is down, it might be because of a DDOS considering everything is available publicly on github, please tell us and we'll do what we can to bring it back up)


## TP1 - Prometheus et Grafana


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

In the end we managed to setup the alertmanager and alerts on slack:

<img src="https://drive.google.com/uc?id=1AXIBQi4Qh-MSFTetudyOw_VaLdoeYzfU" alt="CaptureSlackAlert"/>

### Configuration Prometheus:

> docker-compose.yml

```yml
prometheus:
  image: "prom/prometheus:v2.31.1"
  container_name: "DevOps2_prometheus"
  volumes:
  - "./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml:ro"
  - "./prometheus/rules.yml:/etc/prometheus/rules.yml:ro"
  ports:
  - "9090:9090"
  networks:
  - "monitoring"
  - "app-network"
  - "resa_net"
```

> prometheus.yml

```yml
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

### Configuration Grafana

> docker-compose.yml

```yml
grafana:
  image: "grafana/grafana:8.2.0"
  container_name: "DevOps2_grafana"
  volumes:
  - "./grafana/datasources.yml:/etc/grafana/provisioning/datasources/datasources.yml:ro"
  - "./grafana/data/:/var/lib/grafana"
  environment:
  - "GF_SECURITY_ADMIN_USER=toudard"
  - "GF_SECURITY_ADMIN_PASSWORD=1234"
  ports:
  - "3000:3000"
  networks:
  - "monitoring"
```

> datasources.yml

```yml
apiVersion: 1
datasources:
- name: Prometheus
  url: prometheus:9090
  type: prometheus
  access: proxy
  isDefault: true
  version: 1
  editable: true
```

## TP2 - ELK

1. Which exporter did you use ? Describe your configuration.

    We used [elasticsearch-Exporter] (quay.io/prometheuscommunity/elasticsearch-exporter:v1.3.0)

    > docker-compose.yml

    ```yml
    elasticsearch:
      container_name: "DevOps2_elasticsearch"
      image: "docker.elastic.co/elasticsearch/elasticsearch:7.15.2"
      environment:
      - "discovery.type=single-node"
      networks:
      - "monitoring"

    elasticsearch_exporter:
      container_name: "DevOps2_elasticsearch_exporter"
      image: "quay.io/prometheuscommunity/elasticsearch-exporter:v1.3.0"
      command:
      - "--es.uri=http://elasticsearch:9200"
      networks:
      - "monitoring"
      depends_on:
      - "elasticsearch"
    ```

    It's a plug and use exporter, we just need to pass the url of elasticsearch and it gets all data that we need


2. List the existing Beats in the Elastic world, and explain in 1-2 lines what data it is supposed to collect.

    - Filebeat : for logs files
    - Metricbeat : for system-level metrics
    - Packetbeat : A network packet analyzer
    - Winlogbeat : collecting windows event logs
    - Auditbeat : for audit data (identifying securtiy breaches and so on)
    - Heartbeat : for uptime monitoring
    - Functionbeat : for monitoring cloud environments


3. List the steps you followed and the command you run in order to set up Filebeat.
    We encoutered some trouble with filebeat due too the absence of /var/lib/docker/containers on WSL.
    We decided to test with /var/run/docker.sock but the same probleme occured.

5. Explain how you managed to detect the problem, and how you fixed it or tried to fix it !
    Aborted due to previouss fails

## TP3 - Load Tests

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

<img src="https://drive.google.com/uc?id=11S0WMh2EDFNNjP9L5QSR2a2eLWG9sBFE" alt="CaptureGraphTestCharge"/>
We can see on that screen capture the huge spike in ressources during the gatling test

Here is the report generated by gatling after a 1500 users load test (on the report accessible on the VPS you can see that the application didn't survive the 50k requests, since it only have 4 CPUs we know that if we want to absorb that much load we need to add CPUs, because RAM wasn't the limiting factor)
<img src="https://drive.google.com/uc?id=1yLcsUOMGpmMDC05MVDuB6WI0jltvMfry" alt="CaptureGatlingReport"/>
And here are examples of results after a test in Jaeger
<img src="https://drive.google.com/uc?id=1QI9FPfHPMkae5OoB622j8vSvKlLhdj33" alt="CaptureJaegerGlobal"/>
<img src="https://drive.google.com/uc?id=1ZS8WnvhXhPp6dR3GLL92ao2thb04W4lf" alt="CaptureJaegerExample"/>