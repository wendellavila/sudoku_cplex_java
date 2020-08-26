package v1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Data {
    public static int ins[][];
    
    public static void readInstance() throws IOException{
        ins = new int[10][10];
        //-------------------------read environment-----------------------------
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Path path = Paths.get("instancia0.csv");
        Scanner scan = new Scanner(path);
        scan.nextLine();
        
        while(scan.hasNextLine()){
            String temp[] = scan.nextLine().split(",");
            ins[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = Integer.parseInt(temp[2]);
        }
    }
    
    public static void readInstance2() throws IOException{
        ins = new int[10][10];
        //-------------------------read environment-----------------------------
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Instancia: ");
        String instancia = br.readLine();
        System.out.println();
        Path path = Paths.get(instancia + ".txt");
        Scanner scan = new Scanner(path);
        
        int i = 0;
        while(scan.hasNextLine()){
            String temp[] = scan.nextLine().split(" ");
            for(int j = 0; j < 9; j++){
                System.out.println(temp[j]);
                //ins[i][j] = Integer.parseInt(temp[j]);
            }
            i++;
        }
    }
    
    public static void printInstance(){
        System.out.println("========================== Instance =========================");
        for(int i = 1; i < 10; i++){
            System.out.print("|   ");
            for (int j = 1; j < 10; j++){
                if(ins[i][j] < 1){
                    System.out.print("    ");
                }
                else {
                    System.out.print(ins[i][j] + "   ");
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
        System.out.println("=============================================================");
    }
}
