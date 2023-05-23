import java.util.ArrayList;
import java.util.List;

public abstract class CPUSchedulingAlgorithm {
    private final List<Row> rows;
    private final List<Event> timeline;
    private int timeQuantum;

    public CPUSchedulingAlgorithm() {
        rows = new ArrayList<>();
        timeline = new ArrayList<>();
    }

    public boolean addRow(Row row)
    {
        return rows.add(row);
    }  // 행 추가
    public abstract void process();  // process 함수 (abstract)

    public double getAverageWaitingTime()
    {
        double avg = 0.0;

        for (Row row : rows)
        {
            avg += row.getWaitingTime();
        }

        return avg / rows.size();
    }

    public double getAverageTurnAroundTime()
    {
        double avg = 0.0;

        for (Row row : rows)
        {
            avg += row.getTurnaroundTime();
        }

        return avg / rows.size();
    }



    public double getAverageResponseTime()
    {
        double avg = 0.0;

        for (Row row : rows)
        {
            avg += row.getResponseTime();
        }

        return avg / rows.size();
    }


    // getter

    // process의 프로세스 이름과 같은 row를 반환
    public Row getRow(String process)
    {
        for (Row row : rows)
        {
            if (row.getProcessName().equals(process))
            {
                return row;
            }
        }

        return null;
    }

    // row의 process 이름과 같은 event를 반환
    public Event getEvent(Row row)
    {
        for (Event event : timeline)
        {
            if (row.getProcessName().equals(event.getProcessName()))
            {
                return event;
            }
        }

        return null;
    }



    public List<Row> getRows() {
        return rows;
    }

    public List<Event> getTimeline() {
        return timeline;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }


    // setter
    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

}
