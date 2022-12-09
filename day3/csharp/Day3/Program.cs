namespace Day3;
class Program
{
    static void Main(string[] args)
    {
        const string filePath = "../../input.txt";
        var fileLines = File.ReadAllLines(filePath);

        Part1(fileLines);
        Part2(fileLines);
    }

    static void Part1(string[] inputLines){
        var result = 
            inputLines
                .AsParallel()
                .Select(GetSharedItem)
                .Select(GetPriority)
                .Aggregate(0, (acc, priority) => acc + priority);

        Console.WriteLine("part 1 result => " + result);
    }

    static void Part2(string[] inputLines){
        var result = 
            inputLines
                .AsParallel()
                .Chunk(3)
                .Select(GetSharedCharBetweenAllThreeStrings)
                .Select(GetPriority)
                .Aggregate(0, (acc, priority) => acc + priority);

        Console.WriteLine("part 2 result => " + result);
    }

    static char GetSharedCharBetweenAllThreeStrings(string[] inputs){
        if(inputs.Length != 3){
            Console.WriteLine("Groups should be of size 3 only");
            Environment.Exit(-1);
        }

        var intersectionResult = inputs[0].Intersect(inputs[1]).Intersect(inputs[2]).ToArray();

        if(intersectionResult.Length != 1){
            Console.WriteLine("Should only be one shared element");
            Environment.Exit(-1);
        }

        return intersectionResult.First();
    }

    static char GetSharedItem(string input){
        var firstHalf = input.Substring(0, (input.Length/2));
        var secondHalf = input.Substring((input.Length/2));
        var intersectionResult = firstHalf.Intersect(secondHalf).ToArray();

        if ( intersectionResult.Length > 1 ){
            Console.WriteLine("Should only be one shared element");
            Console.WriteLine("Failed Input => " + input);
            Environment.Exit(-1);
        }

        return intersectionResult.First();
    }

    static int GetPriority(char input){
        return input >= 'a' && input <= 'z' 
            ? input - 96 
            : 
                input >= 'A' && input <= 'Z' 
                ? input - 38 
                : throw new InvalidDataException("Got non-alpha character => " + input);
    }
}
