package v1;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Modelo {
    public static IloCplex model;
    
    public static void main(String[] args) {
        try {
            Data.readInstance();
            Data.printInstance();
        } catch (IOException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            long startTime = System.currentTimeMillis();
            model = new IloCplex();
            
            //time limit = 60 sec
            model.setParam(IloCplex.DoubleParam.TiLim, 60.0);
            
            //decision variables
            IloIntVar[][][] s = new IloIntVar[10][10][10];
            for(int i = 1; i < 10; i++){
                for(int j = 1; j < 10; j++){
                    for(int k = 1; k < 10; k++){
                        s[i][j][k] = model.boolVar();
                    }
                }
            }
            
            //objective function
            IloLinearNumExpr objFunction = model.linearNumExpr();
            for(int i = 1; i < 10; i++){
                for(int j = 1; j < 10; j++){
                    for(int k = 1; k < 10; k++){
                        objFunction.addTerm(1, s[i][j][k]); 
                    }
                }
            }
            model.addMinimize(objFunction);
            
            //constraints
            //adding values I[i][j] = k from the instance
            //to matrix s (s[i][j][k] = 1) only when 1 <= k <= 9
            for(int j = 1; j < 10; j++){
                for(int i = 1; i < 10; i++){
                    if(Data.ins[i][j] > 0){
                        model.addEq(s[i][j][Data.ins[i][j]], 1);
                    };
                }
            }
            
            //sum of all k values in ij must be equal to 1
            for(int i = 1; i < 10; i++){
                for(int j = 1; j < 10; j++){
                    IloLinearNumExpr oneValuePerPosition = model.linearNumExpr();
                    for(int k = 1; k < 10; k++){
                        oneValuePerPosition.addTerm(1.0, s[i][j][k]);
                    }
                    //adding equation
                    model.addEq(oneValuePerPosition, 1);
                }
            }

            //no repetition of values in the same row
            for(int i = 1; i < 10; i++){
                for(int k = 1; k < 10; k++){
                    IloLinearNumExpr noRowRep = model.linearNumExpr();
                    for(int j = 1; j < 10; j++){
                        noRowRep.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addEq(noRowRep, 1);
                }
            }
            
            //no repetition of values in the same column
            for(int j = 1; j < 10; j++){
                for(int k = 1; k < 10; k++){
                    IloLinearNumExpr noColRep = model.linearNumExpr();
                    for(int i = 1; i < 10; i++){
                        noColRep.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addEq(noColRep, 1);
                }
            }
            
            //no repetition of values in the same subgrid
            //forall k shared between all constraints
            for(int k = 1; k < 10; k++){
                
                //for subgrid [1-3][1-3]
                IloLinearNumExpr noQuadRep_13_13 = model.linearNumExpr();
                for(int i = 1; i < 4; i++){
                    for(int j = 1; j < 4; j++){
                        noQuadRep_13_13.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_13_13, 1);
                }
                
                //for subgrid [1-3][4-6]
                IloLinearNumExpr noQuadRep_13_46 = model.linearNumExpr();
                for(int i = 1; i < 4; i++){
                    for(int j = 4; j < 7; j++){
                        noQuadRep_13_46.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_13_46, 1);
                }
                
                //for subgrid [1-3][7-9]
                IloLinearNumExpr noQuadRep_13_79 = model.linearNumExpr();
                for(int i = 1; i < 4; i++){
                    for(int j = 7; j < 10; j++){
                        noQuadRep_13_79.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_13_79, 1);
                }
                
                //for subgrid [4-6][1-3]
                IloLinearNumExpr noQuadRep_46_13 = model.linearNumExpr();
                for(int i = 4; i < 7; i++){
                    for(int j = 1; j < 4; j++){
                        noQuadRep_46_13.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_46_13, 1);
                }
                
                //for subgrid [4-6][4-6]
                IloLinearNumExpr noQuadRep_46_46 = model.linearNumExpr();
                for(int i = 4; i < 7; i++){
                    for(int j = 4; j < 7; j++){
                        noQuadRep_46_46.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_46_46, 1);
                }
                
                //for subgrid [4-6][7-9]
                IloLinearNumExpr noQuadRep_46_79 = model.linearNumExpr();
                for(int i = 4; i < 7; i++){
                    for(int j = 7; j < 10; j++){
                        noQuadRep_46_79.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_46_79, 1);
                }
                
                //for subgrid [7-9][1-3]
                IloLinearNumExpr noQuadRep_79_13 = model.linearNumExpr();
                for(int i = 7; i < 10; i++){
                    for(int j = 1; j < 4; j++){
                        noQuadRep_79_13.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_79_13, 1);
                }
                
                //for subgrid [7-9][4-6]
                IloLinearNumExpr noQuadRep_79_46 = model.linearNumExpr();
                for(int i = 7; i < 10; i++){
                    for(int j = 4; j < 7; j++){
                        noQuadRep_79_46.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_79_46, 1);
                }
                
                //for subgrid [7-9][7-9]
                IloLinearNumExpr noQuadRep_79_79 = model.linearNumExpr();
                for(int i = 7; i < 9; i++){
                    for(int j = 7; j < 10; j++){
                        noQuadRep_79_79.addTerm(1, s[i][j][k]);
                    }
                    //adding equation
                    model.addLe(noQuadRep_79_79, 1);
                }
                
            }
            
            if(model.solve()){
                System.out.println("=============================================================");
                System.out.println(model.getStatus());
                System.out.println(model.getObjValue());
                System.out.println("=============================================================");
                System.out.println("========================= Solution ==========================");
                for(int i = 1; i < 10; i++){
                    System.out.print("|   ");
                    for(int j = 1; j < 10; j++){
                        for(int k = 1; k < 10; k++){
                            if(model.getValue(s[i][j][k]) > 0.9){
                               System.out.print(k + "   ");
                            }
                        }
                        if(j % 3 == 0){
                            System.out.print("|   ");
                        }
                    }
                    System.out.println();
                    if(i % 3 == 0){
                        System.out.println("-------------------------------------------------");
                    }
                }
            }
            else {
               System.out.println(model.getStatus()); 
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");
            
        } catch (IloException ex) {
            Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
