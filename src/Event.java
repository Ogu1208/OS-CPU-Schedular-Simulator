public class Event {
    private final String processName;  // 수정 x
    private final int startTime;  // 시작 시간 - 수정 xCPUSchedulingAlgorithm
    private int finishTime;  // 끝나는 시간 - 수정 x
    public Event(String processName, int startTime, int finishTime)
    {
        this.processName = processName;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    // getter
    public String getProcessName() {
        return processName;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    // setter
    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }
}
