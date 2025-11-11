import java.util.Scanner;

public class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] bt = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];

        // Input burst times
        System.out.println("Enter burst time for each process:");
        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
        }

        // Calculate Waiting Time
        wt[0] = 0;
        for (int i = 1; i < n; i++) {
            wt[i] = wt[i - 1] + bt[i - 1];
        }

        // Calculate Turnaround Time
        for (int i = 0; i < n; i++) {
            tat[i] = bt[i] + wt[i];
        }

        // Print process table
        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println("Process\tBurst Time\tWaiting Time\tTurnaround Time");
        System.out.println("-----------------------------------------------------");

        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t\t" + bt[i] + "\t\t" + wt[i] + "\t\t" + tat[i]);
        }

        System.out.println("-----------------------------------------------------");

        // Calculate and print averages
        float avgWT = 0, avgTAT = 0;
        for (int i = 0; i < n; i++) {
            avgWT += wt[i];
            avgTAT += tat[i];
        }
        avgWT /= n;
        avgTAT /= n;

        System.out.println();
        System.out.printf("Average Waiting Time: %.2f\n", avgWT);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);

        sc.close();
    }
}
