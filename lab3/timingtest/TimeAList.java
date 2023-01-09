package timingtest;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.print("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> lst = new AList<>();
        AList<Integer> Ns = new AList<>();
        AList<Double> time = new AList<>();
        AList<Integer> ops = new AList<>();

        Stopwatch sw = new Stopwatch();
        int testNumber = 0;
        int tick = 0;
        for(int i = 0; i < 100000000; i++){
            lst.addLast(i);
            testNumber += 1;
            if(testNumber == Math.pow(2, tick) * 1000) {
                Ns.addLast(lst.size());
                time.addLast(sw.elapsedTime());
                ops.addLast(testNumber);
                tick += 1;
            }
        }
        printTimingTable(Ns, time, ops);
    }
}
