import java.util.Scanner;

class Process {
    int pid;             // Process ID
    int arrivalTime;     // Arrival Time
    int burstTime;       // Burst Time
    int remainingTime;   // Remaining Time
    int completionTime;  // Completion Time
    int waitingTime;     // Waiting Time
    int turnaroundTime;  // Turnaround Time
    boolean isCompleted; // Completion Flag
}

public class PreemptiveSJF {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            p[i].pid = i + 1;
            System.out.print("Enter arrival time for Process " + p[i].pid + ": ");
            p[i].arrivalTime = sc.nextInt();
            System.out.print("Enter burst time for Process " + p[i].pid + ": ");
            p[i].burstTime = sc.nextInt();
            p[i].remainingTime = p[i].burstTime;
        }

        int completed = 0, currentTime = 0;
        double totalWT = 0, totalTAT = 0;

        while (completed != n) {
            int idx = -1;
            int minRemaining = Integer.MAX_VALUE;

            // Find the process with the shortest remaining time at the current time
            for (int i = 0; i < n; i++) {
                if (p[i].arrivalTime <= currentTime && !p[i].isCompleted) {
                    if (p[i].remainingTime < minRemaining) {
                        minRemaining = p[i].remainingTime;
                        idx = i;
                    }
                    // If remaining times are same, choose process with earlier arrival
                    if (p[i].remainingTime == minRemaining) {
                        if (p[i].arrivalTime < p[idx].arrivalTime) {
                            idx = i;
                        }
                    }
                }
            }

            if (idx != -1) {
                p[idx].remainingTime--;
                currentTime++;

                if (p[idx].remainingTime == 0) {
                    p[idx].isCompleted = true;
                    completed++;
                    p[idx].completionTime = currentTime;
                    p[idx].turnaroundTime = p[idx].completionTime - p[idx].arrivalTime;
                    p[idx].waitingTime = p[idx].turnaroundTime - p[idx].burstTime;

                    totalWT += p[idx].waitingTime;
                    totalTAT += p[idx].turnaroundTime;
                }
            } else {
                // No process has arrived yet, move time forward
                currentTime++;
            }
        }

        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println(p[i].pid + "\t" + p[i].arrivalTime + "\t" + p[i].burstTime + "\t" +
                    p[i].completionTime + "\t" + p[i].turnaroundTime + "\t" + p[i].waitingTime);
        }

        System.out.printf("\nAverage Turnaround Time: %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWT / n);

        sc.close();
    }
}
