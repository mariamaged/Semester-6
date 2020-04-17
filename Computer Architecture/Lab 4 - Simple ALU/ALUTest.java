package Task4;

import java.util.Scanner;

public class ALUTest {
    public static void main(String[] args) {
        while (true) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Input:");
            System.out.print("1st Operand: ");
            String op1 = sc.next();
            System.out.println("");

            System.out.print("2nd Operand: ");
            String op2 = sc.next();
            System.out.println("");

            System.out.print("Operation: ");
            String op = sc.next();
            System.out.println("");

            ALU.ALUEvaluator(op, Integer.parseInt(op1), Integer.parseInt(op2));
            System.out.print(ALU.result);
        }
    }
}
