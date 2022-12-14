using System;
using System.Collections.Generic;
using System.IO;

namespace Day9
{
    class Knot{
            public int x { get; set; } = 0;
            public int y { get; set; } = 0;
    }

    class SimState{

        private int tailIdx;
        private List<Knot> rope = new List<Knot>();

        public HashSet<String> VisitedTailLocations {get; private set;} = new HashSet<string> { RenderCoordinate(0,0) } ;

        public SimState(int nodeCount){
            for(var i = 0; i < nodeCount; i++){
                rope.Add(new Knot());
            }

            this.tailIdx = rope.Count -1;
        }

        private void MoveHead(String direction){
            switch(direction){
                case "U":
                    rope[0].y += 1;
                    break;
                case "D":
                    rope[0].y -= 1;
                    break;
                case "L":
                    rope[0].x -= 1;
                    break;
                case "R":
                    rope[0].x += 1;
                    break;
            }
        }

        private void MoveNode(Knot current, Knot previous){
            var xDiff = previous.x - current.x;
            var yDiff = previous.y - current.y;

            var previousAndCurrentTouching = (Math.Abs(xDiff) <= 1 && Math.Abs(yDiff) <= 1);
            if(!previousAndCurrentTouching){
                current.x += Math.Sign(xDiff);
                current.y += Math.Sign(yDiff);
            }
        }

        private static String RenderCoordinate(int x, int y){
            return "("+x+","+y+")";
        }

        public void PerformMove(String direction, int magnitude){
            for(int i=0; i< magnitude; i++){
                // move the head node according to command
                MoveHead(direction);

                // move all other nodes to catch up
                for(int j=1; j<rope.Count; j++){
                    MoveNode(rope[j], rope[j-1]);
                }

                // stash location of last node
                VisitedTailLocations.Add(RenderCoordinate(rope[tailIdx].x, rope[tailIdx].y));
            }
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            const string filePath = "../../input.txt";
            var fileLines = File.ReadAllLines(filePath);

            var state = new SimState(10);
            // state for part 1
            //var state = new SimState(2);

            foreach (var item in fileLines)
            {
                var parts = item.Split(' ');
                var direction = parts[0];
                var size = Int32.Parse(parts[1]);
                
                state.PerformMove(direction, size);
            }

            Console.WriteLine("Number of visited positions for tail => " + state.VisitedTailLocations.Count);
        }
    }
}
