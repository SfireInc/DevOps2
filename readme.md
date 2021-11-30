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
![Graph CPU Usage](.\img\GraphCPUUsage.PNG "Graph CPU Usage")
The System CPU Usage corresponds to the global system CPU Usage (the amount of the hardware CPU being used).

2. What represents the CPU load ?

The CPU load represents the number of processes waiting for the CPU to be available, the higher it is the worse the performance will be because it means that processes will start to take longer, because they will have to wait the end of other processing tasks.

3. What are all those types of thread ?

pending 
busy
Current
Live Daemon/Non Daemon
New

4. What do they all mean (Thread States)?

Runnable 
Blocked
Waiting
Timed-Waiting
New
Terminated

5. Memory max / used / committed ?