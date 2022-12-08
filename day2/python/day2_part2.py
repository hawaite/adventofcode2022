plays = {
    "rock": {
        "draw" : "rock",
        "win" : "paper",
        "lose": "scissors"
    },
    "paper": {
        "lose" : "rock",
        "draw" : "paper",
        "win": "scissors"
    },
    "scissors": {
        "win" : "rock",
        "lose" : "paper",
        "draw": "scissors"
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
    "B": "paper",
    "C": "scissors",
    "X": "lose",
    "Y": "draw",
    "Z": "win"
}

def main():
    with open("../input.txt", "r") as fp:
        my_total = 0
        for single_play in fp:
            [theirs, result] = single_play.split(" ")
            their_play = decode_map[theirs.strip()]
            desired_result = decode_map[result.strip()]

            my_play = plays[their_play][desired_result]

            # add my unconditional play points
            my_total = my_total + points[my_play]
            my_total = my_total + points[desired_result]

        print("total points was ", my_total)

if __name__ == "__main__":
    main()