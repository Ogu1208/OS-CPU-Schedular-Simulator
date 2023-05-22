import java.util.Collections;
import java.util.List;

public class FCFS extends CPUSchedulingAlgorithm{
    @Override
    public void process() {

        // Sort list of objects using Collection.sort() with lambdas only
        // 도착시간(ArrivalTime)을 기준으로 빠른순으로 정렬한다. (도착한 순서대로)
        Collections.sort(this.getRows(), (Object o1, Object o2) -> {
            if (((Row) o1).getArrivalTime() == ((Row) o2).getArrivalTime())
            {
                return 0;
            }
            else if (((Row) o1).getArrivalTime() < ((Row) o2).getArrivalTime())
            {
                return -1;
            }
            else
            {
                return 1;
            }
        });

        List<Event> timeline = this.getTimeline();

        // for문을 돌며 각 프로세스의 Event 값들을 Event List에 add
        for (Row row : this.getRows())
        {
            if (timeline.isEmpty())
            {   // Event 리스트가 비어있으면 첫번째 process Event 추가
                timeline.add(new Event(row.getProcessName(), row.getArrivalTime(), row.getArrivalTime() + row.getBurstTime()));
            }
            else
            {   // 비어있지 않으면 (2번째 프로세스부터) Event 추가
                Event event = timeline.get(timeline.size() - 1);  // 마지막 프로세스를 가져옴
                // startTime : 마지막에 진행된 프로세스의 끝난시간
                // finishTime : 마지막에 진행된 프로세스의 끝난시간 + row의 burstTime
                timeline.add(new Event(row.getProcessName(), event.getFinishTime(), event.getFinishTime() + row.getBurstTime()));
            }
        }

        // for문을 돌며 waitingTime, turnAroundTime, responseTime set
        for (Row row : this.getRows())
        {
            // waitingTime = 시작 시작 - 도착 시간
            row.setWaitingTime(this.getEvent(row).getStartTime() - row.getArrivalTime());
            // turnAroundTime = 대기 시간 + 실행 시간(burst time)
            row.setTurnaroundTime(row.getWaitingTime() + row.getBurstTime());
            // responseTime = 프로세스가 처음으로 CPU를 할당받는 시간 - 도착시간
            row.setResponseTime(row.getWaitingTime() + 1);
        }
    }
}
