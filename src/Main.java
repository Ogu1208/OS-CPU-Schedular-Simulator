import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("-----------------FCFS----------------");
        fcfs();
        System.out.println("-----------------SJF-----------------");
        sjf();
    }

    public static void fcfs()
    {
        CPUSchedulingAlgorithm fcfs = new FCFS();
        fcfs.addRow(new Row("P1", 0,30));
        fcfs.addRow(new Row("P2", 3, 18));
        fcfs.addRow(new Row("P3", 6, 9));
        fcfs.process();
        display(fcfs);
    }

    public static void sjf()
    {
        CPUSchedulingAlgorithm sjf = new SJF();
        sjf.addRow(new Row("P1", 0, 5));
        sjf.addRow(new Row("P2", 2, 3));
        sjf.addRow(new Row("P3", 4, 2));
        sjf.addRow(new Row("P4", 6, 4));
        sjf.addRow(new Row("P5", 7, 1));
        sjf.process();
        display(sjf);
    }

    public static void display(CPUSchedulingAlgorithm object)
    {
        System.out.println("Process\tAT\tBT\tWT\tTAT\tRT");

        for (Row row : object.getRows())
        {
            System.out.println(row.getProcessName() + "\t\t" +
                    row.getArrivalTime() + "\t" +
                    row.getBurstTime() + "\t" +
                    row.getWaitingTime() + "\t" +
                    row.getTurnaroundTime() + "\t" +
                    row.getResponseTime());
        }
        System.out.println();

        for (int i = 0; i < object.getTimeline().size(); i++)
        {
            List<Event> timeline = object.getTimeline();
            System.out.print(timeline.get(i).getStartTime() + "(" + timeline.get(i).getProcessName() + ")");

            if (i == object.getTimeline().size() - 1)
            {
                System.out.print(timeline.get(i).getFinishTime());
            }
        }

        System.out.println("\n\nAverage WT: " + object.getAverageWaitingTime() +
                "\nAverage TAT: " + object.getAverageTurnAroundTime() +
                "\nAverage RT: " + object.getAverageResponseTime());
    }
}