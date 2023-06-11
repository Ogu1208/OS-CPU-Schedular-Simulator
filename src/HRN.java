import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HRN extends CPUSchedulingAlgorithm {

    @Override
    public void process() {
        // Sort list of objects using Collection.sort() with lambdas only
        // 도착시간(ArrivalTime)을 기준으로 빠른순으로 정렬한다. (도착한 순서대로)
        Collections.sort(this.getRows(), (Object o1, Object o2) -> {
            if (((Row) o1).getArrivalTime() == ((Row) o2).getArrivalTime()) {
                return 0;
            } else if (((Row) o1).getArrivalTime() < ((Row) o2).getArrivalTime()) {
                return -1;
            } else {
                return 1;
            }
        });

        // rows = this.getRows를 깊은 복사
        List<Row> rows = Utility.deepCopy(this.getRows());
        int time = rows.get(0).getArrivalTime();  // 도착 시간

        while (!rows.isEmpty()) {
            // available Row의 List를 만든다.
            List<Row> availableRows = new ArrayList();

            // for문을 돌며 time보다 row의 도착시간이 작거나 같으면 available Row에 추가한다.
            for (Row row : rows) {
                if (row.getArrivalTime() <= time) {
                    availableRows.add(row);
                }
            }

            // for문을 돌며 우선순위를 계산한다
            // priority = (대기 시간 + CPU 사용 시간) / CPU 사용시간
            for (Row row : availableRows) {
                row.setWaitingTime(time - row.getArrivalTime());
                row.setPriority((row.getWaitingTime() + row.getBurstTime()) / row.getBurstTime());
            }

            /*
            System.out.println("우선순위 : ");
            for (Row row : availableRows) {
                System.out.println(row.getProcessName() + ": " + row.getPriority() + ", " + row.getWaitingTime());
            }
            ::qcd
            dfd*/


            // 우선순위가 높은 순(숫자가 큰 순 = 에이징 순)으로 정렬한다. (Priority)
            Collections.sort(availableRows, (Object o1, Object o2) -> {
                if (((Row) o1).getPriority() == ((Row) o2).getPriority()) {
                    return 0;
                } else if (((Row) o1).getPriority() > ((Row) o2).getPriority()) {
                    return -1;
                } else {
                    return 1;
                }
            });

            // 가능한 rows들 중 우선순위가 가장 높은 row(process)를 구한다.
            Row row = availableRows.get(0);

            // timeline(List<Event>)에 우선순위가 가장 높은 row(process)의 Event를 추가
            // startTime : time
            // finishTime : time + row의 burstTime(실행시간)
            this.getTimeline().add(new Event(row.getProcessName(), time, time + row.getBurstTime()));
            time += row.getBurstTime();

            // for문을 돌며 깊은복사한 rows에서 실행된 row를 찾아 삭제한다.(remove)
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).getProcessName().equals(row.getProcessName())) {
                    rows.remove(i);
                    break;
                }
            }
        }

        // for문을 돌며 waitingTime, turnAroundTime, responseTime set
        for (Row row : this.getRows()) {
            // waitingTime = 시작 시작 - 도착 시간
            row.setWaitingTime(this.getEvent(row).getStartTime() - row.getArrivalTime());
            // turnAroundTime = 대기 시간 + 실행 시간(burst time)
            row.setTurnaroundTime(row.getWaitingTime() + row.getBurstTime());
            // responseTime = 첫 작업을 시작한 후 첫 번째 출력(반응)이 나오기 전까지 시간
            row.setResponseTime(row.getWaitingTime() + 1);

        }
    }
}
