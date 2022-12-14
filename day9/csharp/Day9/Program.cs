using System;
using System.Collections.Generic;
using System.IO;

namespace Day9
{
    class SimState{
        public HashSet<String> VisitedTailLocations = new HashSet<string> { RenderCoordinate(0,0) };
        
        public int HeadPosX { get; set; } = 0;
        public int HeadPosY { get; set; } = 0;
        public int TailPosX { get; set; } = 0;
        public int TailPosY { get; set; } = 0;

        private void MoveHead(String direction){
            switch(direction){
                case "U":
                    HeadPosY += 1;
                    break;
                case "D":
                    HeadPosY -= 1;
                    break;
                case "L":
                    HeadPosX -= 1;
                    break;
                case "R":
                    HeadPosX += 1;
                    break;
            }
        }

        private void MoveTail(){
            var xDiff = HeadPosX - TailPosX;
            var yDiff = HeadPosY - TailPosY;

            var headAndTailTouching = (Math.Abs(xDiff) <= 1 && Math.Abs(yDiff) <= 1);
            if(!headAndTailTouching){
                TailPosX += Math.Sign(xDiff);
                TailPosY += Math.Sign(yDiff);
            }

            VisitedTailLocations.Add(RenderCoordinate(TailPosX, TailPosY));
        }

        private static String RenderCoordinate(int x, int y){
            return "("+x+","+y+")";
        }

        public void PerformMove(String direction, int magnitude){
            for(int i=0; i< magnitude; i++){
                MoveHead(direction);
                MoveTail();
            }
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            const string filePath = "../../input.txt";
            var fileLines = File.ReadAllLines(filePath);

            var state = new SimState();

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
