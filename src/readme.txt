Mancala Solver

How to run (requires a GUI):
# compile
make

# where alg is "human", "smart", or "random"
make runProg p1=[alg] p2=[alg] depth=[minimax depth]

# examples:

    # random vs random
    make runProg p1=random p2=random

    # human vs random
    make runProg p1=human p2=random

    # human vs smartbot (depth defaults to 3)
    make runProg p1=human p2=smart

    # random vs smartbot with depth 6
    make runProg p1=random p2=smart depth=6
    
    # human vs human
    make runProg p1=human p2=human


Citations:
Mancala code from: https://code.google.com/p/mancala/ (Apache License)
ObjectCloner from: http://www.javaworld.com/article/2077578/learn-java/java-tip-76--an-alternative-to-the-deep-copy-technique.html (to simulate future board states)

Classes added that were written by us:
    SmartBot.java - code for the minimax w/ alphabeta pruning solver
    Random.java - code for the brainless algorithm that chooses at random
    
Classes modified by us:
    Mancala.java - enums to help initialize the new robot players
    Pit.java - minor things to help the AI know what's going on
    
Issues:
- The game may crash sometimes upon launch, restarting it usually fixes it.
- When playing with bots sometimes the New Game option doesn't work, and the game will need to be restarted