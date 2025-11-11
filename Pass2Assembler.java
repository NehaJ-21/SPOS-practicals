import java.io.*;
import java.util.*;

/*
Pass-2 Assembler (Simplified)
Reads: intermediate.txt, symtab.txt, littab.txt
Generates: machinecode.txt
Works on both Ubuntu and Windows
*/

public class Pass2Assembler {
    public static void main(String[] args) throws Exception {

        List<String> intermediate = new ArrayList<>();
        Map<Integer, Integer> symtab = new HashMap<>();
        Map<Integer, Integer> littab = new HashMap<>();

        // Read intermediate.txt
        try (BufferedReader br = new BufferedReader(new FileReader("intermediate.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) intermediate.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("intermediate.txt not found");
            return;
        }

        // Read symtab.txt
        try (BufferedReader br = new BufferedReader(new FileReader("symtab.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 3)
                    symtab.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("symtab.txt not found");
        }

        // Read littab.txt
        try (BufferedReader br = new BufferedReader(new FileReader("littab.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 3)
                    littab.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("littab.txt not found");
        }

        PrintWriter out = new PrintWriter(new FileWriter("machinecode.txt"));
        out.println("LC\tMachine Code");
        out.println("-------------------------");

        for (String line : intermediate) {
            line = line.trim();

            // Ignore assembler directives
            if (line.contains("(AD") || line.contains("(DL,02)")) continue;

            // Handle Declarative (DC)
            if (line.contains("(DL,01)")) {
                int start = line.indexOf("(C,") + 3;
                int end = line.indexOf(")", start);
                String value = line.substring(start, end);
                String lc = line.split("\\s+")[0];
                out.println(lc + "\t00\t0\t" + value);
                continue;
            }

            // Handle Imperative Statements (IS)
            if (line.contains("(IS")) {
                String lc = line.split("\\s+")[0];

                String opcode = "00";
                if (line.toUpperCase().contains("MOVER")) opcode = "04";
                else if (line.toUpperCase().contains("MOVEM")) opcode = "05";
                else if (line.toUpperCase().contains("ADD")) opcode = "01";
                else if (line.toUpperCase().contains("SUB")) opcode = "02";
                else if (line.toUpperCase().contains("BC")) opcode = "07";

                String reg = "0";
                if (line.toUpperCase().contains("AREG")) reg = "1";
                else if (line.toUpperCase().contains("BREG")) reg = "2";
                else if (line.toUpperCase().contains("CREG")) reg = "3";

                // Find address from symtab or littab
                String addr = "000";
                if (line.contains("(S,")) {
                    int st = line.indexOf("(S,") + 3;
                    int en = line.indexOf(")", st);
                    int symIndex = Integer.parseInt(line.substring(st, en));
                    addr = symtab.getOrDefault(symIndex, 0).toString();
                } else if (line.contains("(L,")) {
                    int st = line.indexOf("(L,") + 3;
                    int en = line.indexOf(")", st);
                    int litIndex = Integer.parseInt(line.substring(st, en));
                    addr = littab.getOrDefault(litIndex, 0).toString();
                }

                out.println(lc + "\t" + opcode + "\t" + reg + "\t" + addr);
            }
        }

        out.close();
        System.out.println("Pass-2 completed. Output file: machinecode.txt");
    }
}
