boards = ['03.txt', '04.txt', '05.txt', '06.txt', '07.txt', '08.txt', '09.txt'] + [str(i) + '.txt' for i in range(11, 51)]

for boardfile in boards:
    board = []
    linecount = 0
    with open(boardfile, 'r') as textfile:
        for line in textfile:
            if linecount < 9:
                board.append(line.strip(' \n'))
                linecount += 1
    with open(boardfile, 'w') as textfile:
        textfile.write('\n'.join(board))
