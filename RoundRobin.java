import java.util.*;

public class RoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n], bt = new int[n], rem = new int[n];
        int[] wt = new int[n], tat = new int[n], ct = new int[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            rem[i] = bt[i];
        }

        System.out.print("Enter Time Quantum: ");
        int q = sc.nextInt();

        int time = 0, done = 0;
        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQ = new boolean[n];

        while (done < n) {
            for (int i = 0; i < n; i++)
                if (at[i] <= time && rem[i] > 0 && !inQ[i]) {
                    queue.add(i);
                    inQ[i] = true;
                }

            if (queue.isEmpty()) { time++; continue; }

            int i = queue.poll();
            int run = Math.min(rem[i], q);
            rem[i] -= run;
            time += run;

            for (int j = 0; j < n; j++)
                if (at[j] <= time && rem[j] > 0 && !inQ[j]) {
                    queue.add(j);
                    inQ[j] = true;
                }

            if (rem[i] == 0) {
                ct[i] = time;
                tat[i] = ct[i] - at[i];
                wt[i] = tat[i] - bt[i];
                done++;
            } else queue.add(i);
        }

        float avgWT = 0, avgTAT = 0;
        System.out.println("\nP\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" + ct[i] + "\t" + tat[i] + "\t" + wt[i]);
            avgWT += wt[i]; avgTAT += tat[i];
        }
        System.out.printf("\nAverage WT: %.2f", avgWT / n);
        System.out.printf("\nAverage TAT: %.2f\n", avgTAT / n);
    }
}