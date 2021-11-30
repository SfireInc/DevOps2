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

<img src="https://i.ibb.co/LrC6vQk/Graph-CPUUsage.png" alt="Graph-CPUUsage"/>

Here is a graph of the CPU Usage in Grafana, for both System CPU Usage et process (in this case JVM) CPU Usage.

2. What represents the CPU load ?

The CPU load represents the number of processes running on the CPU, if this number becomes Higher than the number of available CPUs, the computing time will start to increase, because processes will have to wait for others to be finished and to make a thread available.

<img src="https://i.ibb.co/3RtdzMM/Graph-CPULoad.png" alt="Graph-CPULoad"/>

On this graph we can see that there was probably some slowdown happening, because the number of needed threads by the system was higher than the available CPUs on the machine.

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