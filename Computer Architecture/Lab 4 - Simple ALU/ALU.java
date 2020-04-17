package Task4;

class ALU {
    static StringBuilder result;
    private static int Zflag;
    private static int outputInt;

    public static void ALUEvaluator(String op, int Operand1, int Operand2) {
        result = new StringBuilder("Output:\nOperation Name: ");
        switch (op) {
            case "0000":
                ANDOp(Operand1, Operand2);
                constructResult("AND", Operand1, Operand2);
                break;
            case "0001":
                OROp(Operand1, Operand2);
                constructResult("OR", Operand1, Operand2);
                break;
            case "0010":
                addOp(Operand1, Operand2);
                constructResult("ADD", Operand1, Operand2);
                break;
            case "0110":
                subOp(Operand1, Operand2);
                constructResult("SUB", Operand1, Operand2);
                break;
            case "0111":
                sltOp(Operand1, Operand2);
                constructResult("SLT", Operand1, Operand2);
                break;
            case "1100":
                NOR(Operand1, Operand2);
                constructResult("NOR", Operand1, Operand2);
        }
    }

    public static int ANDOp(int Operand1, int Operand2) {
        outputInt = Operand1 & Operand2;
        changeZFlag();
        return outputInt;
    }

    public static int OROp(int Operand1, int Operand2) {
        outputInt = Operand1 | Operand2;
        changeZFlag();
        return outputInt;
    }

    public static int addOp(int Operand1, int Operand2) {
        outputInt = Operand1 + Operand2;
        changeZFlag();
        return outputInt;
    }

    public static int subOp(int Operand1, int Operand2) {
        outputInt = Operand1 - Operand2;
        changeZFlag();
        return outputInt;
    }

    public static int sltOp(int Operand1, int Operand2) {
        outputInt = (Operand1 < Operand2) ? 1 : 0;
        changeZFlag();
        return outputInt;
    }

    public static int NOR(int Operand1, int Operand2) {
        outputInt = ~(Operand1|Operand2);
        changeZFlag();
        return outputInt;
    }

    private static void constructResult(String opName, int Operand1, int Operand2) {
        result.append(opName + "\n");
        String op1 = String.format("%32s", Integer.toBinaryString(Operand1)).replace(' ', '0');
        String op2 = String.format("%32s", Integer.toBinaryString(Operand2)).replace(' ', '0');
        String output = String.format("%32s", Integer.toBinaryString(outputInt)).replace(' ', '0');
        result.append("1st Operand: " + op1 + "/" + Operand1 + "\n");
        result.append("1st Operand: " + op2 + "/" + Operand2 + "\n");
        result.append("Output: " + output + "/" + outputInt + "\n");
        result.append("Z-flag Value: " + Zflag + "\n\n\n");
    }

    private static void changeZFlag() {
        if(outputInt == 0) Zflag = 1; else Zflag = 0;
    }
}
