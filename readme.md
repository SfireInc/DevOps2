# TP1

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
```

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

    (From https://docs.oracle.com/javase/9/docs/api/java/lang/management/MemoryUsage.html)



# TP2

1. Which exporter did you use ? Describe your configuration.

    We used elasticsearch-Exporter (quay.io/prometheuscommunity/elasticsearch-exporter:v1.3.0)
