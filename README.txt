README for Project 2 - Automated Stock Trader
Students: Kevin Mc Inerney & Noreen Lenihan
Student IDs: u15402569 & u15402437 respectively

=============================================================================

To run our GA, cd into the 'src' folder from where you downloaded the project.
First, compile all necessary files by typing the following command;

javac -cp . AI/*.java TradingFloor/*.java

Next, run the 'Genetic Algorithm' class with all the necessary parameters. The order
of parameters is as follows:

int popSize - desired population size
int maxGen - maximum generations
int selectionType - selection strategy (1 = random, 2 = tournament, 3 = fitness proportional)
int crossoverType - crossover strategy (1 = one-point, 2 = two-point)
double mutationRate - desired mutation rate
double crossOverP - desired crossover probability
double GenGap - desired generation gap
double tournamentP - desired tournament size
boolean changeCrossOverType - whether you want to switch from one to two-point crossover strategy after 500 generations
boolean lowToMed - 'true' indicates that the parameter settings will gradually change from low pressure to medium pressure settings
boolean lowToHigh - 'true' indicates that the parameter settings will gradually change from low pressure to high selection pressure settings

Now, to execute the program, type the following command with sample parameters:

java -cp . AI/GeneticAlgorithm 100 1000 2 1 0.01 0.6 0.1 0.1 false false false

Note: The most profitable trading strategy from each generation will be printed to stdout

================================================================================

To run the Hill-Climber, simply type the following command, since it was compiled above:

java -cp . AI/HillClimber

Note: In our main for the Hill-Climber class, we have a loop to instantiate 10 instances of the HC,
and print out the best strategy and resultant profit for each.

=================================================================================

