import random

def getAnswer():
    answerNumber = random.randint(1, 9)

    if answerNumber == 1:
        return 'It is certain'
    elif answerNumber == 2:
        return 'It is decidedly so'
    elif answerNumber == 3:
        return 'Yes'
    elif answerNumber == 4:
        return 'Reply hazy try again'
    elif answerNumber == 5:
        return 'Ask again later'
    elif answerNumber == 6:
        return 'Concentrate and ask again'
    elif answerNumber == 7:
        return 'My reply is no'
    elif answerNumber == 8:
        return 'Outlook not so good'
    elif answerNumber == 9:
        return 'Very doubtful'

def getAnswerFromList():
    messages = [
        'It is certain',
        'It is decidedly so',
        'Yes definitely',
        'Reply hazy try again',
        'Ask again later',
        'Concentrate and ask again',
        'My reply is no',
        'Outlook not so good',
        'Very doubtful'
    ]

    return messages[random.randint(0, len(messages) - 1)]

for i in range(10):
    answer = getAnswer()
    answerList = getAnswerFromList()

    print(str(answer == answerList) + ' - [' + answer + ", " + answerList + "]")
