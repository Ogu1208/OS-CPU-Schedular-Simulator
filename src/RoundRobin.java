import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundRobin extends CPUSchedulingAlgorithm {

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
        int time = rows.get(0).getArrivalTime();

        // time Quantum 설정
        int timeQuantum = this.getTimeQuantum();

        while (!rows.isEmpty()) {
            Row row = rows.get(0);
            // 한 텀의  burst time 계산
            // (timeQuantum보다 burstTime이 작으면 burstTime만큼, 아니라면 timeQuantum만큼 실행)
            int bt = (row.getBurstTime() < timeQuantum ? row.getBurstTime() : timeQuantum);

            // timeline(List<Event>)에 가장 먼저 도착한 row(process)의 Event를 추가
            // startTime : time
            // finishTime : time + row의 bt
            this.getTimeline().add(new Event(row.getProcessName(), time, time + bt));
            time += bt;  // time 갱신 (timme+=bt)
            rows.remove(0);

            // 만약 프로세스의 burstTime이 timeQuantum보다 크다면
            if (row.getBurstTime() > timeQuantum) {
                // burtTime을 bursttime - timeQuantum으로 갱신
                row.setBurstTime(row.getBurstTime() - timeQuantum);

                // timequantum만큼 일한 프로세스 row를 큐의 뒤에 추가
                for (int i = 0; i < rows.size(); i++) {
                    // 만약 rows의 행이 방금 작업이 끝난 시간 time보다 늦게 도착한다면
                    if (rows.get(i).getArrivalTime() > time) {
                        // 해당 자리에 방금 끝난 프로세스 row를 추가
                        rows.add(i, row);
                        break;
                    }
                    // 아니라면 큐의 마지막에 row 추가
                    else if (i == rows.size() - 1) {
                        rows.add(row);
                        break;
                    }
                }
            }
        }

        Map map = new HashMap();

        // for문을 돌며 row와 evnet 비교
        for (Row row : this.getRows()) {
            map.clear();

            // waitingTime = 시작 시작 - 도착 시간
            for (Event event : this.getTimeline()) {
                // row와 evnet의 프로세스 이름이 같으면
                if (event.getProcessName().equals(row.getProcessName())) {
                    // map에 프로세스 이름의 key가 있다면 (이미 한번 이상 burstTime만큼 작업했다면)
                    if (map.containsKey(event.getProcessName())) {   // 기존  waitingTime에 (이전에 작업했을 때의 finishTime - 이번 작업의 startTime)을 더함.
                        int w = event.getStartTime() - (int) map.get(event.getProcessName());
                        row.setWaitingTime(row.getWaitingTime() + w);
                    }
                    // 아직 map에 프로세스 이름의 key가 없다면
                    else {   // row의 waitingTime을 startTime - ArrivalTime으로 초기화
                        row.setWaitingTime(event.getStartTime() - row.getArrivalTime());
                    }

                    // map에 key : processName, value: finishTime을 삽입
                    map.put(event.getProcessName(), event.getFinishTime());
                }
            }

            // turnAroundTime = 대기 시간 + 실행 시간(burst time)
            row.setTurnaroundTime(row.getWaitingTime() + row.getBurstTime());
            // responseTime = 첫 작업을 시작한 후 첫 번째 출력(반응)이 나오기 전까지 시간
            row.setResponseTime(row.getWaitingTime() + 1);
        }
    }
}

