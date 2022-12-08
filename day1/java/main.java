import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class Day1{
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Pass path to input file as first argument.");
            System.exit(-1);
        }

        Day1.loop(args[0]);
        Day1.streams(args[0]);
    }

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

        System.out.println("loop - highest run: " + highestRun);
    }

    private static void streams(String filePath){
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
                    .reduce(0, (acc, newInt) ->  acc + newInt )
            )
            .max(Integer::compare)
            .get();

        System.out.println("streams - max result : " + maxResult);
    }
}
