import java.util.*;

public class Pass2MacroProcessor {
    public static void main(String[] mainArgs) {

        // Step 1: Macro Definition Table (MDT)
        // (In real scenarios this is produced by Pass 1)
        List<String> MDT = Arrays.asList(
            "A 1,&ARG1",
            "L 2,&ARG2",
            "ST 1,&ARG1",
            "MEND"
        );

        // Step 2: MNT and ALA simulated
        // (Here only one macro called INCR)
        Map<String, List<String>> MNT = new HashMap<>();
        MNT.put("INCR", MDT);

        // Step 3: Source program with macro call
        List<String> sourceCode = Arrays.asList(
            "START",
            "INCR X,Y",
            "END"
        );

        // Step 4: Macro Expansion
        System.out.println("----- PASS 2 MACRO EXPANSION OUTPUT -----\n");
        System.out.println("Expanded Code:\n");

        for (String line : sourceCode) {
            line = line.trim();
            boolean expanded = false;

            // Check if line starts with a macro name from MNT
            for (String macroName : MNT.keySet()) {
                if (line.startsWith(macroName)) {
                    expanded = true;

                    // Extract actual arguments
                    String[] actualArgs = line.split(" ")[1].split(",");

                    // Fetch macro body
                    List<String> macroBody = MNT.get(macroName);

                    // Replace arguments and print expanded code
                    for (String stmt : macroBody) {
                        if (!stmt.equalsIgnoreCase("MEND")) {
                            String expandedLine = stmt
                                    .replace("&ARG1", actualArgs[0])
                                    .replace("&ARG2", actualArgs[1]);
                            System.out.println(expandedLine);
                        }
                    }
                    break;
                }
            }

            // Print non-macro lines as-is
            if (!expanded) {
                System.out.println(line);
            }
        }

        System.out.println("\n----- PASS 2 COMPLETED SUCCESSFULLY -----");
    }
}
