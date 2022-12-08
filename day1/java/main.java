import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Day1{
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Pass path to input file as first argument.");
            System.exit(-1);
        }

        Day1.loop(args[0]);
        Day1.streams(args[0], false);
        Day1.streams(args[0], true);
    }

    // a simple looping solution
    private static void loop(String filePath){
        List<String> inputLines = null;

        try {
            inputLines = Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            System.out.println(e);
            System.exit(-2);
        }

        int highestRun = 0;
        int currentRun = 0;

        for (String line : inputLines) {
            if(line.isBlank()){
                if(currentRun > highestRun)
                    highestRun = currentRun;
                currentRun = 0;
            }
            else{
                currentRun += Integer.parseInt(line);
            }
        }

        System.out.printf("loop - highest run: %d\n", highestRun);
    }

    // a somewhat silly streams solution
    private static void streams(String filePath, Boolean sumTopThree){
        String fileContent = null;

        try {
            fileContent = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("File doesnt exist");
            System.exit(-2);
        }

        int maxResult = 
            Arrays.stream(fileContent.split("\n\n"))
            .map(inputPart -> 
                Arrays.stream(inputPart.split("\n"))
                    .map(Integer::parseInt)
                    .reduce(0, Integer::sum )
            )
            .sorted(Comparator.reverseOrder())
            .limit(sumTopThree ? 3 : 1)
            .reduce(0, Integer::sum );

        System.out.printf("streams - max result of top %d: %d\n", sumTopThree ? 3 : 1, maxResult);
    }
}
