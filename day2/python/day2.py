plays = {
    "rock": {
        "rock" : "draw",
        "paper" : "win",
        "scissors": "lose"
    },
    "paper": {
        "rock" : "lose",
        "paper" : "draw",
        "scissors": "win"
    },
    "scissors": {
        "rock" : "win",
        "paper" : "lose",
        "scissors": "draw"
    }
}

points = {
    "win": 6,
    "lose": 0,
    "draw": 3,
    "rock": 1,
    "paper": 2,
    "scissors": 3
}

decode_map = {
    "A": "rock",
    "X": "rock",
    "B": "paper",
    "Y": "paper",
    "C": "scissors",
    "Z": "scissors"
}

def main():
    with open("../input.txt", "r") as fp:
        my_total = 0
        for single_play in fp:
            [theirs, mine] = single_play.split(" ")
            their_play = decode_map[theirs.strip()]
            my_play = decode_map[mine.strip()]

            # add my unconditional play points
            my_total = my_total + points[my_play]
            my_total = my_total + points[plays[their_play][my_play]]

        print("total points was ", my_total)

if __name__ == "__main__":
    main()