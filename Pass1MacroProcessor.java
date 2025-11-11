import java.io.*;
import java.util.*;

public class Pass1MacroProcessor {
    public static void main(String[] mainArgs) throws IOException {

        // Sample input macro lines (can be replaced with file reading)
        List<String> lines = Arrays.asList(
            "MACRO",
            "INCR &ARG1,&ARG2",
            "A 1,&ARG1",
            "L 2,&ARG2",
            "ST 1,&ARG1",
            "MEND",
            "INCR X,Y",
            "END"
        );

        // Tables
        Map<String, Integer> MNT = new LinkedHashMap<>(); // Macro Name Table
        List<String> MDT = new ArrayList<>();             // Macro Definition Table
        Map<String, List<String>> ALA = new LinkedHashMap<>(); // Argument List Array

        boolean inMacro = false;
        String currentMacro = null;

        for (String line : lines) {
            line = line.trim();
            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                continue;
            }

            if (inMacro) {
                if (line.equalsIgnoreCase("MEND")) {
                    MDT.add("MEND");
                    inMacro = false;
                    currentMacro = null;
                    continue;
                }

                // First line after MACRO is macro prototype
                if (currentMacro == null) {
                    // split by whitespace
                    String[] parts = line.split("\\s+");
                    currentMacro = parts[0]; // macro name
                    // split parameter list; rename variable to avoid clash with "mainArgs"
                    String[] macroParams = parts.length > 1 ? parts[1].split(",") : new String[0];

                    MNT.put(currentMacro, MDT.size());
                    // store as list (copy) so later we can refer by macro name
                    ALA.put(currentMacro, Arrays.asList(macroParams));
                } else {
                    // Add macro body lines to MDT
                    MDT.add(line);
                }
            }
        }

        // Print tables to console (exam-style)
        System.out.println("MNT (Macro Name Table):");
        for (Map.Entry<String, Integer> e : MNT.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }

        System.out.println("\nALA (Argument List Array):");
        for (Map.Entry<String, List<String>> e : ALA.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }

        System.out.println("\nMDT (Macro Definition Table):");
        for (int i = 0; i < MDT.size(); i++) {
            System.out.println(i + " " + MDT.get(i));
        }
    }
}
