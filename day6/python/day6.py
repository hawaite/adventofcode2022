def main():
    with open("../input.txt", "r") as fp:
        input = fp.readline();
        windowStart, windowEnd = 0,3
        while windowEnd < len(input):
            if len(set(input[windowStart:windowEnd+1])) == 4:
                break
            windowStart = windowStart + 1
            windowEnd = windowEnd + 1

        print(f'found packet header at => ({windowStart}, {windowEnd+1})')

if __name__ == "__main__":
    main()