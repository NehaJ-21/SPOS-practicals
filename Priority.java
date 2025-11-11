import java.util.Scanner;

public class Priority {
    public static void main(String[] args) {
        Scanner S=new Scanner(System.in);
        System.out.print("Enter Number of Processes: ");
        int NP=S.nextInt();

        int P[] = new int[NP];
        int PBT[] = new int[NP];
        int AT[] = new int[NP];
        int Priority[] = new int[NP];
        int CT[] = new int[NP];
        int WT[] = new int[NP];
        int TAT[] = new int[NP];

        System.out.println("Enter Process Burst Time: ");
        for(int i=0;i<NP;i++) {
            P[i]=i+1;
            System.out.print("P[" + P[i] + "]: ");
            PBT[i]=S.nextInt();
        }

        System.out.println("Enter Process Arrival Time: ");
        for(int i=0;i<NP;i++)
            AT[i]=S.nextInt();

        System.out.println("Enter Process Priority: ");
        for(int i=0;i<NP;i++)
            Priority[i]=S.nextInt();

        // Sort processes by arrival and priority
        for(int i=0;i<NP;i++) {
            for(int j=0;j<NP-i-1;j++) {
                if(AT[j]>AT[j+1] || (AT[j]==AT[j+1] && Priority[j]>Priority[j+1])) {
                    int temp=AT[j]; AT[j]=AT[j+1]; AT[j+1]=temp;
                    temp=PBT[j]; PBT[j]=PBT[j+1]; PBT[j+1]=temp;
                    temp=Priority[j]; Priority[j]=Priority[j+1]; Priority[j+1]=temp;
                    temp=P[j]; P[j]=P[j+1]; P[j+1]=temp;
                }
            }
        }

        CT[0]=AT[0]+PBT[0];
        TAT[0]=CT[0]-AT[0];
        WT[0]=TAT[0]-PBT[0];
        for(int i=1;i<NP;i++) {
            CT[i]=PBT[i]+CT[i-1];
            TAT[i]=CT[i]-AT[i];
            WT[i]=TAT[i]-PBT[i];
        }

        float AVWT=0, AVTAT=0;
        for(int i=0;i<NP;i++){
            AVWT+=WT[i];
            AVTAT+=TAT[i];
        }
        AVWT/=NP;
        AVTAT/=NP;

        System.out.println("Processes Arrival Burst Priority Waiting Turnaround");
        for(int i=0;i<NP;i++)
            System.out.println(P[i]+"\t\t"+AT[i]+"\t\t"+PBT[i]+"\t\t"+Priority[i]+"\t\t"+WT[i]+"\t\t"+TAT[i]);

        System.out.println("\nAverage Waiting Time: "+AVWT);
        System.out.println("Average Turn Around Time: "+AVTAT);
    }
}