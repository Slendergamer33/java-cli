package task1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class TaskOne {

    private static final List<String> bufferOutput = new ArrayList<>();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            TaskOne taskA = new TaskOne();

            System.out.println("Enter commands: cat, sort, uniq, wc or | ");
            while (true) {
                System.out.print(">> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                try {
                    taskA.executeCommands(input);
                    bufferOutput.forEach(System.out::println);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                } finally {
                    bufferOutput.clear();
                }
            }
        }
    }
    // Splits and processes each piped command (e.g. cat file.txt | sort | uniq)
    public void executeCommands(String inputString) {
        if (inputString == null || inputString.isBlank()) return;

        String[] pipeline = inputString.split("\\|");
        List<String> intermediateOutput = null;

        for (int i = 0; i < pipeline.length; i++) {
            String command = pipeline[i].trim();
            String[] tokens = command.split("\\s+");

            if (tokens.length == 0 || tokens[0].isEmpty()) continue;

            String cmd = tokens[0];
            // Store result to pass to next pipe and execute command
            switch (cmd) {
                case "cat":
                    intermediateOutput = handleCat(tokens);
                    break;
                case "wc":
                    intermediateOutput = handleWc(tokens, intermediateOutput);
                    break;
                case "sort":
                    intermediateOutput = handleSort(tokens, intermediateOutput);
                    break;
                case "uniq":
                    intermediateOutput = handleUniq(tokens, intermediateOutput);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid command " + cmd);
            }
        }

        if (intermediateOutput != null) {
            bufferOutput.addAll(intermediateOutput);
        }
    }

    // Reads file and returns content as lines
    private List<String> handleCat(String[] tokens) {
        if (tokens.length < 2) {
            System.out.println("Usage: cat <filename>");
            return Collections.emptyList(); // fallback
        }

        return readFile(tokens[1]);
    }

    // Counts lines, words, and bites
    private List<String> handleWc(String[] tokens, List<String> input) {
        boolean onlyLineCount = tokens.length >= 2 && tokens[1].equals("-l");
        List<String> lines;

        if (onlyLineCount) {
            if (tokens.length == 3) {
                lines = readFile(tokens[2]);
            } else if (input != null) {
                lines = input;
            } else {
                throw new IllegalArgumentException("Invalid wc -l usage");
            }

            return List.of(String.valueOf(lines.size()));
        } else {
            if (tokens.length == 2) {
                lines = readFile(tokens[1]);
            } else if (input != null) {
                lines = input;
            } else {
                throw new IllegalArgumentException("Invalid wc usage");
            }

            int linesCount = lines.size();
            int wordCount = 0;
            int byteCount = 0;

            for (String line : lines) {
                wordCount += line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
                byteCount += line.getBytes(StandardCharsets.UTF_8).length + 1;
            }

            return List.of(linesCount + " " + wordCount + " " + byteCount);
        }
    }

    // Sorts lines in ascending order
    private List<String> handleSort(String[] tokens, List<String> input) {
        List<String> lines;

        if (tokens.length == 2) {
            lines = readFile(tokens[1]);
        } else if (input != null) {
            lines = new ArrayList<>(input);
        } else {
            throw new IllegalArgumentException("Invalid sort usage");
        }

        Collections.sort(lines);
        return lines;
    }

    // Removes consecutive duplicate lines
    private List<String> handleUniq(String[] tokens, List<String> input) {
        List<String> lines;

        if (tokens.length == 2) {
            lines = readFile(tokens[1]);
        } else if (input != null) {
            lines = input;
        } else {
            throw new IllegalArgumentException("Invalid uniq usage");
        }

        List<String> result = new ArrayList<>();
        String prev = null;

        for (String line : lines) {
            if (!line.equals(prev)) {
                result.add(line);
                prev = line;
            }
        }

        return result;
    }

    // Reads a file and returns contents as a list of strings and throws if file is not valid
    private List<String> readFile(String filename) {
        Path path = Paths.get(filename);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            if (Files.isDirectory(path)) {
                throw new IllegalArgumentException(filename + " is a directory");
            }
            throw new IllegalArgumentException("Invalid file " + filename);
        }

        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid file " + filename);
        }
    }

    public List<String> getCommandOutput() {
        return new ArrayList<>(bufferOutput);
    }
}