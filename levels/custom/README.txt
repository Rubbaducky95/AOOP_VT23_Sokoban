To create a map in sokoban use the words: 
"null" for a blank floor tile, "wall" for a wall, "redmarker" for a marked floor tile,
"box" for a crate, "boxmarked" for a marked crate, and "player" for the players starting position.

Write these words in a straight column followed by the word "next" when you wish to start crating a new row in the game.

For example:

wall	-> This will create a 4x3 room surrounded by walls		null	-> This will put the player on the left floor-tile in the room and a box on the right
wall											null
wall											null
wall											null
next											next
wall											null
null											player
null											box
wall											null
next											next
wall											null
wall											null
wall											null
wall											null

The program needs a "map.txt" file which must ONLY contain walls, and floor-tiles (marked and blank).
Furthermore it needs an "interactive.txt" file which must only contain the crates (marked and unmarked), and the player. Write "null" for empty spaces.
