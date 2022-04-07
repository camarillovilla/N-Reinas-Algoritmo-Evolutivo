import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Queens {

  public static int[][] mutateBoard(int[][] board, int boardSize) {
    int[][] mutatedBoard = new int[boardSize][boardSize];

    int queenNumber = (int) (Math.random() * 8 + 1);
    int queensCounter = 0;
    int rowToMutate = 0;
    int columnToMutate = 0;
    
    // Find queen to mutate
    for (int rows = 0; rows < boardSize; rows++) {
      for (int columns = 0; columns < boardSize; columns++) {
        if (board[rows][columns] == 1) {
          if (queensCounter == queenNumber) {
            rowToMutate = rows;
            columnToMutate = columns;
            break;
          } else {
            queensCounter++;
          }
        }
      }
    }

    // System.out.print("| Position to be eliminated: ");
    // System.out.println("[" + rowToMutate + "]" + "[" + columnToMutate + "]   |");
    // System.out.println("+++++++++++++++++++++++++++++++++++++++\n");
    
    // Copy board
    for (int rows = 0; rows < boardSize; rows++) {
      for (int columns = 0; columns < boardSize; columns++) {
        if (rowToMutate == rows && columnToMutate == columns) {
          mutatedBoard[rows][columns] = 0;
        } else if (board[rows][columns] == 1) {
          mutatedBoard[rows][columns] = 1;
        } else {
          mutatedBoard[rows][columns] = 0;
        }
      }
    }
    
    // Mutate
    boolean isMutated = false;

    while (!isMutated) {
      int newColumn = (int) (Math.random() * 8);
      int newRow = (int) (Math.random() * 8); 
  
      // Move queen
      for (int rows = 0; rows < boardSize; rows++) {
        for (int columns = 0; columns < boardSize; columns++) {
          if (newRow == rows && newColumn == columns) {
            if (mutatedBoard[rows][columns] == 0) {
              mutatedBoard[rows][columns] = 1;
              isMutated = true;
            }
          }
        }
      }
    }

    return mutatedBoard;
  }

  public static int[][] generateDescendant(int[][] motherBoard, int[][] fatherBoard, int boardSize) {
    int[][] descendant = new int[boardSize][boardSize];
    int motherQueens = 0;
    int fatherQueens = 0;
    int queensPerBoard = boardSize / 2;

    // Copy first 4 queens from mother board
    for (int rows = 0; rows < boardSize; rows++) {
      for (int columns = 0; columns < boardSize; columns++) {  
        if (motherQueens < queensPerBoard) {
          if (motherBoard[rows][columns] == 1) {
            if (descendant[rows][columns] == 0) {
              motherQueens++;
              descendant[rows][columns] = motherBoard[rows][columns];
            }
          }
        }     
      }  
    }

    // Copy first 4 queens from father board
    for (int rows = 0; rows < boardSize; rows++) {
      for (int columns = 0; columns < boardSize; columns++) {
        if (fatherQueens < queensPerBoard) {
          if (fatherBoard[rows][columns]  == 1) {
            if (descendant[rows][columns] == 0) {
              fatherQueens++;
              descendant[rows][columns] = fatherBoard[rows][columns];
            }
          }
        }   
      }  
    }

    return descendant;
  }

  // Based on the positions obtained from the population assign boards
  public static ArrayList<int[][]> assignRandomBoards(int numberOfParents, int populationSize, int[] positionsForParents, ArrayList<int[][]> boards) {
    ArrayList<int[][]> randomSelection = new ArrayList<>();

    for (int i = 0; i < populationSize; i++) {
      for (int j = 0; j < numberOfParents; j++) {
        if (i == positionsForParents[j]) {
          randomSelection.add(j, boards.get(i));
        }
      } 
    }

    return randomSelection;
  }
  
  public static int[] getPositionsForParents(int numberOfParents) {
    int[] positionsForParents = new int[numberOfParents];
    
    // Random parent positions
    for (int i = 0; i < numberOfParents; i++) {
      int randomNumber = (int) (Math.random() * 100);
      boolean isExisting = false;
      
      // Check that the position does not exist
      for (int j = 0; j < numberOfParents; j++) {
        if (positionsForParents[j] == randomNumber) {
          isExisting = true;
        }
      }
  
      if (!isExisting) {
        positionsForParents[i] = randomNumber;
      }
    }

    Arrays.sort(positionsForParents);

    return positionsForParents;
  }

  public static int getNumberOfAttacks(int boardSize, int[][] board) {
    int numberOfAttacks = 0;
    // Total Board Diagonals
    int numberOfDiagonals = ((boardSize * 2) - 1) - 2;
    // Diagonal number that will iterate
    int diagonalNumber = 1;

    // Horizontal Attacks
    for (int rows = 0; rows < boardSize; rows++) {
      int numberOFQueens = 0;
      for (int columns = 0; columns < boardSize; columns++) {
        if (board[rows][columns] == 1) {
          numberOFQueens++;             
        }
      }
      numberOFQueens -= 1;
      if (numberOFQueens > 0) {
        numberOfAttacks += numberOFQueens;
      }
    }  

    // Vertical Attacks
    for (int columns = 0; columns < boardSize; columns++) {
      int numberOFQueens = 0;
      for (int rows = 0; rows < boardSize; rows++) {
        if (board[rows][columns] == 1) {
          numberOFQueens++;             
        }
      }
      numberOFQueens -= 1;
      if (numberOFQueens > 0) {
        numberOfAttacks += numberOFQueens;
      }
    }

    // Right Diagonal Attacks, secondary diagons
    while (diagonalNumber <= numberOfDiagonals) {
      int numberOFQueens = 0;
      for (int rows = 0; rows < boardSize; rows++) {
        for (int columns = 0; columns < boardSize; columns++) {
          if ((rows + columns) == diagonalNumber) {
            if (board[rows][columns] == 1) {
              numberOFQueens++;     
            }            
          }        
        }   
      }
      numberOFQueens -= 1;
      if (numberOFQueens > 0) {
        numberOfAttacks += numberOFQueens;
      }
      diagonalNumber++;    
    }
    
    // Diagonal number is redefined
    diagonalNumber = 1;

    // Left Diagonal Attack, main diagonals
    while (diagonalNumber <= numberOfDiagonals) {
      int numberOFQueens = 0;
      for (int rows = 0; rows < boardSize; rows++) {
        for (int columns = 0; columns < boardSize; columns++) {
          if ((columns - rows) == ((boardSize - diagonalNumber) - 1)) {
            if (board[rows][columns] == 1) {
              numberOFQueens++;     
            }            
          }        
        }   
      }
      numberOFQueens -= 1;
      if (numberOFQueens > 0) {
        numberOfAttacks += numberOFQueens;
      }
      diagonalNumber++;    
    }

    return numberOfAttacks;
  }

  public static void showBoard(int boardSize, int[][] board) {
    for (int rows = 0; rows < boardSize; rows++) {
      for (int columns = 0; columns < boardSize; columns++) {
        // Print position value
        System.out.print(board[rows][columns] + " ");
      }    
      // Row jump
      System.out.println("");
    } 
  }

  public static int[][] fillBoard(int boardSize) {
    // int numberOfQueens = 0;
    int[][] board = new int[boardSize][boardSize];

    // Add queens in any position
    // for (int rows = 0; rows < boardSize; rows++) {
    //   for (int columns = 0; columns < boardSize; columns++) {
    //     int piece = (int) (Math.random() * 2);
    //     // The number one represents a queen
    //     if (piece == 1) {
    //       numberOfQueens++;
    //     }
           // // Verify that there are no more than 8 queens
    //     if (numberOfQueens <= 8) {
    //       board[rows][columns] = piece;
    //     } else {
    //       board[rows][columns] = 0;
    //     }
    //   } 
    // }

    // Since each queen can threaten all queens in the same row, each queen must be placed in a different row.
    for (int columns = 0; columns < boardSize; columns++) {
      // Columns random row number
      int index = (int) (Math.random() * 7 + 1); 
      
      for (int rows = 0; rows < boardSize; rows++) {
        if (rows == index) {
          board[rows][columns] = 1;
        } else {
          board[rows][columns] = 0;
        }      
      }
    }    

    return board;
  }

  public static void main(String[] args) {
    // Constants
    int boardSize = 8;
    int populationSize = 100;
    int numberOfParents = 5;

    int numberOfEvaluations = 0;

    // Will store the population
    ArrayList<int[][]> boards = new ArrayList<>();
    // Will store the children
    ArrayList<int[][]> children = new ArrayList<>();
    
    // Create population
    for (int i = 0; i < populationSize; i++) {
      boards.add(i, fillBoard(boardSize));
      // System.out.println(" > Board " + (i + 1));
      // showBoard(boardSize, boards.get(i));
      // String attacks = getNumberOfAttacks(boardSize, boards.get(i)) + " ";
      // System.out.println(" > Ataques: " + attacks + "\n");
    }

    // System.out.println("+++++++++++++++++++++++++++++++++++++++");
    // System.out.println("| Creating population of boards...    |");
    // System.out.println("+++++++++++++++++++++++++++++++++++++++");
    // System.out.println("| Population created!                 |");
    // System.out.println("+++++++++++++++++++++++++++++++++++++++" + "\n\n");
    
    // Board that stores the solution in case it is found
    int[][] solutionBoard = new int[boardSize][boardSize];
    
    boolean isFound = false;
    // Stop conditions
    while (numberOfEvaluations < 10000 && !isFound) {
      int childrenSize = 0;
      numberOfEvaluations++;        
      // System.out.println("\t > Evaluation: " + (numberOfEvaluations + " "));
      
      // Generate 10 descendants
      while (childrenSize < 10) {
        ArrayList<int[][]> randomSelection = new ArrayList<>();
        ArrayList<int[][]> parents = new ArrayList<>();
        int[] positionsForParents = new int[numberOfParents];
        int[][] childBoard = new int[boardSize][boardSize];


        // Assign random positions from the population
        positionsForParents = getPositionsForParents(numberOfParents);
        // Assign boards of the population based on the positions obtained
        randomSelection = assignRandomBoards(numberOfParents, populationSize, positionsForParents, boards);
         
        // System.out.println("+++++++++++++++++++++++++++++++++++++++");
        // System.out.println( "| Generation " +  (childrenSize + 1) + "                        |" );
        // System.out.println("+++++++++++++++++++++++++++++++++++++++");
        // System.out.println("| 5 Random boards                     |");
        // System.out.println("+++++++++++++++++++++++++++++++++++++++" + "\n");
        
        /* 
        // for (int i = 0; i < populationSize; i++) {
        //   for (int j = 0; j < numberOfParents; j++) {
        //     if (i == positionsForParents[j]) {
        //       randomSelection.set(j, boards.get(i));
        //     }
        //   } 
        // }
        */

        // System.out.println("+++++++++++++++++++++++++++++++++++++++");
        // System.out.println("| 2 Best boards                       |");
        // System.out.println("+++++++++++++++++++++++++++++++++++++++" + "\n");
        // System.out.println(randomSelection.size() + "");

        // Sort random selection to find the two best tablets
        for (int i = 0; i < numberOfParents; i++) {
          for (int j = 0; j < numberOfParents - 1; j++) {
            int numberOfAttacks = getNumberOfAttacks(boardSize, randomSelection.get(j));   
            int auxiliaryNumberOfAttacks = getNumberOfAttacks(boardSize, randomSelection.get(j + 1));  
  
            if (numberOfAttacks > auxiliaryNumberOfAttacks) {
              int[][] auxiliaryBoard = randomSelection.get(j);
              randomSelection.set(j, randomSelection.get(j + 1));
              randomSelection.set(j + 1, auxiliaryBoard);
            }
          }
        }

        parents.add(0, randomSelection.get(0));
        parents.add(1, randomSelection.get(1));
        
        // for (int i = 0; i <= (parents.size() - 1); i++) {
        //   System.out.println(" > Board " + (i + 1));
        //   // showBoard(boardSize, parents.get(i));
        //   String attacks = getNumberOfAttacks(boardSize, parents.get(i)) + " ";
        //   System.out.println(" > Ataques: " + attacks + "\n");
        // }
        // System.out.println("");

        // System.out.println("+++++++++++++++++++++++++++++++++++++++");
        // System.out.println("| Creating child Board...             |");
        // System.out.println("+++++++++++++++++++++++++++++++++++++++");
        
        // Generate Descendant based on the best boards of the random selection
        childBoard = generateDescendant(parents.get(0), parents.get(1), boardSize);

        // System.out.println("| Child Board                         |");
        // System.out.println("+++++++++++++++++++++++++++++++++++++++" + "\n");
        // // showBoard(boardSize, childBoard);
        // String attacks = getNumberOfAttacks(boardSize, childBoard) + " ";
        // System.out.println(" > Ataques: " + attacks + "\n");

        // System.out.println("");
        
        // Probability of the board to mutate or not
        // It matters whether or not to mutate add it to the child container
        int[][] mutatedChildBoard = new int[boardSize][boardSize];
        boolean isMutated = new Random().nextInt(80) == 0;
        int mutation = isMutated ? 1 : 0;

        // System.out.println("Mutation: " + isMutated + "" );

        switch (mutation) {
          case 0:
            //System.out.println("HOLLLLLLLLLLLLLLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            // System.out.println("+++++++++++++++++++++++++++++++++++++++");
            // System.out.println("| The board has mutated               |");
            // System.out.println("+++++++++++++++++++++++++++++++++++++++");
            mutatedChildBoard = mutateBoard(childBoard, boardSize);
            // showBoard(boardSize, mutatedChildBoard);
            // String childBoardAttacks = getNumberOfAttacks(boardSize, childBoard) + " ";
            // System.out.println(" > Ataques: " + childBoardAttacks + "\n");
            children.add(mutatedChildBoard);
          break;
          
          case 1:
            // System.out.println("+++++++++++++++++++++++++++++++++++++++");
            // System.out.println("| The board has not changed           |");
            // System.out.println("+++++++++++++++++++++++++++++++++++++++");
            children.add(childBoard);            
            // System.out.println(" > Ataques: " + attacks + "\n");
          break;

          default:        
            break;
        }
        // System.out.println("\n\n");
        childrenSize++;
      }

      // System.out.println("+++++++++++++++++++++++++++++++++++++++");
      // System.out.println("| Population ordered (attacks)        |");
      // System.out.println("+++++++++++++++++++++++++++++++++++++++" + "\n");

      // Order population based on their attacks to replace the worst individuals
      for (int i = 0; i < populationSize; i++) {
        for (int j = 0; j < populationSize - 1; j++) {
          int numberOfAttacks = getNumberOfAttacks(boardSize, boards.get(j));   
          int auxiliaryNumberOfAttacks = getNumberOfAttacks(boardSize, boards.get(j + 1));  
          if (numberOfAttacks > auxiliaryNumberOfAttacks) {
            int[][] auxiliaryBoard = new int[boardSize][boardSize];
            auxiliaryBoard = boards.get(j);
            boards.set(j, boards.get(j + 1));
            boards.set(j + 1, auxiliaryBoard);
          }
        }
      }

      // for (int i = 0; i < populationSize; i++) {
      //   System.out.println(" > Board " + (i + 1));
      //   showBoard(boardSize, boards.get(i));
      //   String attacks = getNumberOfAttacks(boardSize, boards.get(i)) + " ";
      //   System.out.println(" > Ataques: " + attacks + "\n");
      // }      
      
      // Replace worst individuals of the board population with the created offspring
      for (int i = 99; i >= 90; i--) {
        boards.remove(i);
      }

      for (int i = 0; i < childrenSize; i++) {
        boards.add(children.get(i));
      }

      // System.out.println("\n");
      // System.out.println("+++++++++++++++++++++++++++++++++++++++");
      // System.out.println("| Replaced population                 |");
      // System.out.println("+++++++++++++++++++++++++++++++++++++++" + "\n");

      for (int i = 0; i < populationSize; i++) {
        int attacks = getNumberOfAttacks(boardSize, boards.get(i));

        if (attacks == 0) {
          isFound = true;
          solutionBoard = boards.get(i);
        }
      }  
    }

    // Show evaluation results
    if (isFound) {
      System.out.println("\n");
      System.out.println("+++++++++++++++++++++++++++++++++++++++");
      System.out.println("| > Solution Board                      |");
      System.out.println("+++++++++++++++++++++++++++++++++++++++");
      System.out.println("| > Evaluations: " + numberOfEvaluations + "                   |");
      System.out.println("+++++++++++++++++++++++++++++++++++++++" + "\n");
      showBoard(boardSize, solutionBoard);
    } else {
      System.out.println("+++++++++++++++++++++++++++++++++++++++");
      System.out.println("|    Maximum number                   |");
      System.out.println("|         of                          |");
      System.out.println("| evaluations reached!                |");
      System.out.println("+++++++++++++++++++++++++++++++++++++++");
      System.out.println("| > Evaluations: " + numberOfEvaluations + "                |");
      System.out.println("+++++++++++++++++++++++++++++++++++++++");
    }
  }
}
