package App;

import java.util.ArrayList;
import java.util.List;

// small helper class for saving screen formats
public class Screens {
    public static String titleScreen() {
        return """
                ╔════════════════════════════════════════╗
                ║     Windows Console Games Collection   ║
                ╠════════════════════════════════════════╣
                ║                                        ║
                ║         *Press Enter To Start*         ║
                ║                                        ║
                ║                                        ║
                ║                                        ║
                ║              Lukas Nys                 ║
                ║              Simon Öfferlbauer         ║
                ║             Stefan Gaderer             ║
                ║             Daniel Lovrinovic          ║
                ╚════════════════════════════════════════╝
                """;
    }

    public static String gameSelectScreen() {
        return """
                ╔════════════════════════════════════════╗
                ║     Windows Console Games Collection   ║
                ╠════════════════════════════════════════╣
                ║                                        ║
                ║               Select Game:             ║
                ║                                        ║
                ║                  1 Demo                ║
                ║                  2 Breakout            ║
                ║                  3 Snake               ║
                ║                  4 Pong                ║
                ║                  5 Tetris              ║
                ║                                        ║
                ╚════════════════════════════════════════╝
                """;
    }

    public static String gameFinishedScreen() {
        return """
                ╔════════════════════════════════════════╗
                ║     Windows Console Games Collection   ║
                ╠════════════════════════════════════════╣
                ║                                        ║
                ║                                        ║
                ║                                        ║
                ║            Game has finished           ║
                ║                                        ║
                ║          *Press Enter to Start*        ║
                ║                                        ║
                ║                                        ║
                ║                                        ║
                ╚════════════════════════════════════════╝
                """;
    }

    public static String getInstructionsScreen(String instructions) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════╗\n");
        sb.append("║     Windows Console Games Colletion    ║\n");
        sb.append("╠════════════════════════════════════════╣\n");
        sb.append("║                                        ║\n");
        sb.append("║              Instructions:             ║\n");
        sb.append("║                                        ║\n");
        insertInstructions(sb, instructions);
        sb.append("║                                        ║\n");
        sb.append("║         *Press Enter to Start*         ║\n");
        sb.append("╚════════════════════════════════════════╝\n");
        return sb.toString();
    }

    private static void insertInstructions(StringBuilder sb, String instructions) {
        List<String> lines = splitIntoLines(instructions);
        for (String line : lines) {
            sb.append("║ ");
            sb.append(centerLine(line));
            sb.append(" ║\n");
        }
    }

    private static List<String> splitIntoLines(String instructions) {
        List<String> lines = new ArrayList<>();
        instructions.lines().forEach(lines::add);
        return lines;
    }

    private static String centerLine(String line) {
        int goalLength = 38;
        int actualLength = line.length();
        int spaceLeft = (goalLength - actualLength) / 2;
        int spaceRight = goalLength - actualLength - spaceLeft;
        return " ".repeat(spaceLeft) + line + " ".repeat(spaceRight);
    }
}
