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
2. What represents the CPU load ?
3. What are all those types of thread ?
4. What do they all mean ?
5. Memory max / used / committed ?