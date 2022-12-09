import re

def main():
    with open("../input.txt", "r") as fp:
        containsCount = 0
        overlapCount = 0
        lines = fp.readlines()

        for line in lines:
            [elf1Start, elf1End, elf2Start, elf2End] = re.split('-|,',line.strip())

            elf1 = set(range(int(elf1Start), int(elf1End)+1))
            elf2 = set(range(int(elf2Start), int(elf2End)+1))

            # if we get an empty set when taking set difference of one range from the other 
            # then one range is contained in the other
            if(elf1 - elf2 == set() or elf2 - elf1 == set()):
                containsCount = containsCount + 1
                overlapCount = overlapCount + 1
            # if there are any common elements then the ranges overlap
            elif(elf1 & elf2 != set()):
                overlapCount = overlapCount + 1
        
    print("contains count = " + str(containsCount))
    print("overlap count  = " + str(overlapCount))

if __name__ == "__main__":
    main()