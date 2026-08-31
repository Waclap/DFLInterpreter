execute if score @s dfl_example matches 20.. run return
scoreboard players add @s dfl_example 1
particle $(type) ~ ~ ~ 0.1 0.1 0.1 0.5 10
function waclap:dfl/recurse {type:"angry_villager"}
