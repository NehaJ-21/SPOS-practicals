import java.io.*;
import java.util.*;

/*
Pass-1 Assembler (Simplified version)
Reads input.txt and creates:
→ intermediate.txt
→ symtab.txt
→ littab.txt
Works on Ubuntu and Windows.
*/

public class Pass1Assembler {
    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        String line;
        int LC = 0;

        LinkedHashMap<String, Integer> symtab = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> littab = new LinkedHashMap<>();
        ArrayList<String> intermediate = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Split by spaces or tabs
            String[] parts = line.split("\\s+");

            // START directive
            if (parts[0].equalsIgnoreCase("START")) {
                LC = Integer.parseInt(parts[1]);
                intermediate.add("(AD,01) (C," + LC + ")");
                continue;
            }

            // END directive
            if (parts[0].equalsIgnoreCase("END")) {
                // Assign addresses to any remaining literals
                for (Map.Entry<String, Integer> e : littab.entrySet()) {
                    if (e.getValue() == -1) {
                        littab.put(e.getKey(), LC);
                        intermediate.add(LC + "\t(Literal)\t" + e.getKey());
                        LC++;
                    }
                }
                intermediate.add("(AD,02)");
                break;
            }

            // Label handling
            String label = parts[0].equals("-") ? null : parts[0];
            int idx = (label == null) ? 0 : 1;
            String opcode = parts[idx].toUpperCase();

            if (label != null) {
                if (!symtab.containsKey(label))
                    symtab.put(label, LC);
            }

            // LTORG directive
            if (opcode.equals("LTORG")) {
                for (Map.Entry<String, Integer> e : littab.entrySet()) {
                    if (e.getValue() == -1) {
                        littab.put(e.getKey(), LC);
                        intermediate.add(LC + "\t(Literal)\t" + e.getKey());
                        LC++;
                    }
                }
                continue;
            }

            // DC (Define Constant)
            if (opcode.equals("DC")) {
                String constant = parts[idx + 1].replaceAll("'", "");
                intermediate.add(LC + "\t(DL,01)\t(C," + constant + ")");
                LC++;
                continue;
            }

            // DS (Define Storage)
            if (opcode.equals("DS")) {
                int size = Integer.parseInt(parts[idx + 1]);
                intermediate.add(LC + "\t(DL,02)\t(C," + size + ")");
                LC += size;
                continue;
            }

            // Literal detection (=...)
            for (String t : parts) {
                if (t.startsWith("=")) {
                    if (!littab.containsKey(t)) {
                        littab.put(t, -1);
                    }
                }
            }

            // Default case (Instruction)
            intermediate.add(LC + "\t(IS,XX)\t" + line);
            LC++;
        }

        br.close();

        // Write SYMTAB
        PrintWriter ps = new PrintWriter("symtab.txt");
        int i = 1;
        for (Map.Entry<String, Integer> e : symtab.entrySet()) {
            ps.println(i + "\t" + e.getKey() + "\t" + e.getValue());
            i++;
        }
        ps.close();

        // Write LITTAB
        ps = new PrintWriter("littab.txt");
        i = 1;
        for (Map.Entry<String, Integer> e : littab.entrySet()) {
            ps.println(i + "\t" + e.getKey() + "\t" + e.getValue());
            i++;
        }
        ps.close();

        // Write INTERMEDIATE
        ps = new PrintWriter("intermediate.txt");
        for (String s : intermediate) {
            ps.println(s);
        }
        ps.close();

        System.out.println("✅ Pass-1 completed successfully!");
        System.out.println("Files generated: symtab.txt, littab.txt, intermediate.txt");
    }
}
