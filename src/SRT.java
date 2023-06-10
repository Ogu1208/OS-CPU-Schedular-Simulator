import java.util.*;

public class SRT extends CPUSchedulingAlgorithm {
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
        int time = rows.get(0).getArrivalTime();  // 첫 번째 프로세스의 도착시간

        // time Quantum 설정
        int timeQuantum = this.getTimeQuantum();

        while (!rows.isEmpty()) {
            // available Row의 List를 만든다.
            List<Row> availableRows = new ArrayList();

            // for문을 돌며 time보다 row의 도착시간이 작거나 같으면 available Row에 추가한다.
            for (Row row : rows) {
                if (row.getArrivalTime() <= time) {
                    availableRows.add(row);
                }
            }

            // burstTime이 작은(남아있는 작업 시간이 가장 적은 순)으로 정렬한다. (Shortest Remaining Time)
            Collections.sort(availableRows, (Object o1, Object o2) -> {
                if (((Row) o1).getBurstTime() == ((Row) o2).getBurstTime()) {
                    return 0;
                } else if (((Row) o1).getBurstTime() < ((Row) o2).getBurstTime()) {
                    return -1;
                } else {
                    return 1;
                }
            });

            // 가능한 rows들 중 burstTime이 가장 적은 row(process)를 구한다.
            Row row = availableRows.get(0);

            // 한 텀의  burst time 계산
            // (timeQuantum보다 burstTime이 작으면 burstTime만큼, 아니라면 timeQuantum만큼 실행)
            int bt = (row.getBurstTime() < timeQuantum ? row.getBurstTime() : timeQuantum);

            // timeline(List<Event>)에 burstTime이 가장 작은(남아있는 작업 시간이 가장 적은) row(process)의 Event를 추가
            // startTime : time
            // finishTime : time + row의 bt
            this.getTimeline().add(new Event(row.getProcessName(), time, time + bt));
            time += bt;  // time 갱신 (timme+=bt)

            // row의 burstTime = burstTime - bt 로 업데이트
            row.setBurstTime(row.getBurstTime() - bt);

            // 만약 해당 process의 burstTime이 끝났다면, 같은 이름의 프로세스를 찾아 rows행에서 remove시킨다.
            if (row.getBurstTime() == 0) {
                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).getProcessName().equals(row.getProcessName())) {
                        rows.remove(i);
                        break;
                    }
                }
            }
        }

        // for문으로 Event의 List를 뒤에서부터 거꾸로 찾으면서
        for (int i = this.getTimeline().size() - 1; i > 0; i--) {
            List<Event> timeline = this.getTimeline();

            // timeline의 마지막 프로세스와 마지막-1의 프로세스가 같다면
            if (timeline.get(i - 1).getProcessName().equals(timeline.get(i).getProcessName())) {
                // 마지막-1의 프로세스의 finishTime을 마지막 프로세스의 finishTime으로 업데이트하고
                timeline.get(i - 1).setFinishTime(timeline.get(i).getFinishTime());
                // 마지막 프로세스를 삭제한다.
                timeline.remove(i);
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
                    if (map.containsKey(event.getProcessName())) {
                        // 기존  waitingTime에 (이전에 작업했을 때의 finishTime - 이번 작업의 startTime)을 더함.
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