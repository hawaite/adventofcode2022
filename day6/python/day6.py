def main(windowSize):
    with open("../input.txt", "r") as fp:
        input = fp.readline();
        windowStart, windowEnd = 0, windowSize
        while windowEnd < len(input):
            if len(set(input[windowStart:windowEnd])) == windowSize:
                break
            windowStart = windowStart + 1
            windowEnd = windowEnd + 1

        print(f'found packet header at => ({windowStart}, {windowEnd})')
        print(f'result is {windowEnd}')

if __name__ == "__main__":
    # part 1
    main(4)
    # part 2
    main(14)