Java Spring Boot application that simulates a 10 pin bowling game.

To start the game, enter names of all the players and call the start API. This will return the game id.
curl -X POST "http://localhost:9300/bowling/v1/game/start?playerNames=abc&playerNames=xyz" -H "accept: */*" -d ""

To play the next set/frame call the play API with the game id.
curl -X POST "http://localhost:9300/bowling/v1/game/play/9" -H "accept: */*" -d ""

To get the score of the game, call the score api
curl -X GET "http://localhost:9300/bowling/v1/game/score/9" -H "accept: */*"
Sample response:
[
    {
        "name": "a",
        "score": 16,
        "lane": 1,
        "frames": [
            {
                "frameNumber": 1,
                "noOfTry": 1,
                "score": 7
            },
            {
                "frameNumber": 1,
                "noOfTry": 2,
                "score": 2
            },
            {
                "frameNumber": 2,
                "noOfTry": 1,
                "score": 6
            },
            {
                "frameNumber": 2,
                "noOfTry": 2,
                "score": 1
            }
        ]
    },
    {
        "name": "b",
        "score": 13,
        "lane": 1,
        "frames": [
            {
                "frameNumber": 1,
                "noOfTry": 1,
                "score": 5
            },
            {
                "frameNumber": 1,
                "noOfTry": 2,
                "score": 3
            },
            {
                "frameNumber": 2,
                "noOfTry": 1,
                "score": 3
            },
            {
                "frameNumber": 2,
                "noOfTry": 2,
                "score": 2
            }
        ]
    }
  ]

Number of players allowed in a single lane and number of lanes is configurable via database.

Tables:

create table CONFIG(
CONFIG_ID varchar(128) primary key,
VALUE text
);

create table GAME(
ID int primary key auto_increment,
FRAME_COMPLETED int
)auto_increment=1;

create table PLAYER(
ID int primary key auto_increment,
NAME varchar(128),
GAME_ID int,
SCORE int default 0,
LANE int,
foreign key(GAME_ID) references GAME(ID)
)auto_increment=1;

create table FRAME(
ID int primary key auto_increment,
GAME_ID int,
PLAYER_ID int,
FRAME_NUMBER int,
TRY int,
SCORE int,
foreign key(GAME_ID) references GAME(ID),
foreign key(PLAYER_ID) references PLAYER(ID)
);

