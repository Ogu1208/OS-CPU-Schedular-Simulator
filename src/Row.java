public class Row {
    // Process | AT | BT | Priority | WT | TAT
    private String processName;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int waitingTime;
    private int turnaroundTime;
//    private int responseTime;

    public Row(String processName, int arrivalTime, int burstTime, int priority, int waitingTime, int turnaroundTime) {
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
    }

    public Row(String processName, int arrivalTime, int burstTime, int priority) {
        this(processName, arrivalTime, burstTime, priority, 0, 0);
    }

    public Row(String processName, int arrivalTime, int burstTime) {
        this(processName, arrivalTime, burstTime, 0, 0, 0);
    }


    // getter

    public String getProcessName() { return processName; }

    public int getArrivalTime() { return arrivalTime; }

    public int getBurstTime() { return burstTime; }

    public int getPriority() { return priority; }

    public int getWaitingTime() { return waitingTime; }

    public int getTurnaroundTime() { return turnaroundTime; }

    // setter
    public void setBurstTime(int burstTime)
    {
        this.burstTime = burstTime;
    }

    public void setWaitingTime(int waitingTime)
    {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime)
    {
        this.turnaroundTime = turnaroundTime;
    }

}
