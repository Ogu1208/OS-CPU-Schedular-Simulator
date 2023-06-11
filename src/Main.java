import java.util.List;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();
    }

    public static void test() {
        System.out.println("-----------------FCFS----------------");
        fcfs();
        System.out.println("-----------------SJF-----------------");
        sjf();
        System.out.println("-----------------PNP-----------------");
        pnp();
        System.out.println("-----------------PSP-----------------");
        psp();
        System.out.println("-----------------HRN-----------------");
        hrn();
        System.out.println("-----------------RR-----------------");
        rr();
        System.out.println("-----------------SRT-----------------");
        srt();
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

    public static void pnp()
    {
        CPUSchedulingAlgorithm pnp = new PriorityNonPreemptive();
        pnp.addRow(new Row("P1", 0, 30, 3));
        pnp.addRow(new Row("P2", 3, 18, 2));
        pnp.addRow(new Row("P3", 6, 9, 1));
        pnp.process();
        display(pnp);
    }

    public static void psp()
    {
        CPUSchedulingAlgorithm psp = new PriorityPreemtive();
        psp.addRow(new Row("P1", 0, 30, 3));
        psp.addRow(new Row("P2", 3, 18, 2));
        psp.addRow(new Row("P3", 6, 9, 1));
        psp.process();
        display(psp);
        display(psp);
    }

    private static void hrn() {
        CPUSchedulingAlgorithm hrn = new HRN();
        hrn.addRow(new Row("P1", 0,30));
        hrn.addRow(new Row("P2", 3, 18));
        hrn.addRow(new Row("P3", 6, 9));
        hrn.process();
        display(hrn);
    }

    public static void rr()
    {
        CPUSchedulingAlgorithm rr = new RoundRobin();
        rr.setTimeQuantum(10);
        rr.addRow(new Row("P1", 0,30));
        rr.addRow(new Row("P2", 3, 18));
        rr.addRow(new Row("P3", 6, 9));
        rr.process();
        display(rr);
    }

    public static void srt()
    {
        CPUSchedulingAlgorithm srt = new SRT();
        srt.setTimeQuantum(10);
        srt.addRow(new Row("P1", 0,30));
        srt.addRow(new Row("P2", 3, 18));
        srt.addRow(new Row("P3", 6, 9));
        srt.process();
        display(srt);
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