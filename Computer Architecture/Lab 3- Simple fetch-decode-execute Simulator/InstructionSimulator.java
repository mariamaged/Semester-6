import java.util.ArrayList;

class InstructionSimulator {

    static int[] RegisterFile = new int[32];
    static boolean RegisterWrite = false;
    static int PCounter = 0;
    static ArrayList<String> InstructionMemory = new ArrayList<>();
    static ArrayList<Integer> DataMemory = new ArrayList<>();

    public static void main(String[] args) {

        DataMemory.add(20);
        DataMemory.add(10);
        DataMemory.add(30);

        // Instruction 1: Load [rs = 10, rt = 3, immediate = 1]
        int instruction1 = 0b100011_01010_00011_0000_0000_0000_0001;
        // Instruction 2: Load [rs = 3, rt = 5, immediate = -8]
        int instruction2 = 0b100011_00011_00101_1111_1111_1111_1000;
        // Instruction 3: ADD [rs = 3, rd = 5, rt = 6]
        int instruction3 = 0b000000_00011_00101_00110_00000_100000;
        // Instruction 4: SUB [rs = 5, rd = 3, rt = 1]
        int instruction4 = 0b000000_00101_00011_00001_00000_100010;
        // Instruction 5: SUB [rs = 3, rs = 5, rt = 2]
        int instruction5 = 0b000000_00011_00101_00010_00000_100010;
        // Instruction 6: Store [rs = 1, rt = 2, immediate = -20]
        int instruction6 = 0b101011_00001_00010_1111_1111_1110_1100;
        // Instruction 7: Jump [address = 24]
        int instruction7 = 0b000011_00000_00000_00000_00000_011000;
        // Instruction 8: Branch if equal. [rs = 2, rt = 4, immediate = 24].
        int instruction8 = 0b000100_00010_00100_00000_00000_011000;


        // Program 1 [Starts from instruction 1 to 6 - without jump].
        int[] firstProgram = new int[]{instruction1, instruction2, instruction3, instruction4, instruction5, instruction6};
        loadInstructions(firstProgram);
        fetchAndExecute();

        // Program 2 [Starts from Instruction 1 to 6 as well as jump instruction 7 that jumps from instruction 4 to 6].
        int[] secondProgram = new int[]{instruction1, instruction2, instruction3, instruction4, instruction7, instruction5, instruction6};
        loadInstructions(secondProgram);
        fetchAndExecute();

        // Program 3 [Starts from Instruction 1 to 6 as well as branch if equal instruction 8 that jumps from instruction 4 to 6].
        int[] thirdProgram = new int[]{instruction1, instruction2, instruction3, instruction4, instruction8, instruction5, instruction6};
        loadInstructions(thirdProgram);
        fetchAndExecute();
    }


    static void loadInstructions(int[] instructions) {
        for (int instruction : instructions) {
            String parsedInstruction = String.format("%32s", Integer.toBinaryString(instruction)).replace(' ', '0');
            String byte1 = parsedInstruction.substring(0, 8);
            String byte2 = parsedInstruction.substring(8, 16);
            String byte3 = parsedInstruction.substring(16, 24);
            String byte4 = parsedInstruction.substring(24, 32);
            InstructionMemory.add(byte1);
            InstructionMemory.add(byte2);
            InstructionMemory.add(byte3);
            InstructionMemory.add(byte4);
        }
    }

    static void fetchAndExecute() {
        // Program termination.
        if (PCounter == InstructionMemory.size()) {
            RegisterFile = new int[32];
            RegisterWrite = false;
            PCounter = 0;
            InstructionMemory.clear();
            DataMemory.clear();
            DataMemory.add(20);
            DataMemory.add(10);
            DataMemory.add(30);
            return;
        }

        System.out.println("In instruction " + ((PCounter / 4) + 1));

        String byte1 = InstructionMemory.get(PCounter);
        String byte2 = InstructionMemory.get(PCounter + 1);
        String byte3 = InstructionMemory.get(PCounter + 2);
        String byte4 = InstructionMemory.get(PCounter + 3);
        PCounter += 4;
        String fullInstruction = byte1 + byte2 + byte3 + byte4;

        int opCode = Integer.parseInt(fullInstruction.substring(0, 6), 2);
        int rs, rt, rd, funct, address;
        byte immediate;

        switch (opCode) {
            case 0:
                RegisterWrite = true;
                rs = Integer.parseInt(fullInstruction.substring(6, 11), 2);
                rt = Integer.parseInt(fullInstruction.substring(11, 16), 2);
                rd = Integer.parseInt(fullInstruction.substring(16, 21), 2);
                funct = Integer.parseInt(fullInstruction.substring(26), 2);
                if (funct == 32) add(rs, rt, rd);
                else subtract(rs, rt, rd);
                break;
            case 35:
                RegisterWrite = true;
                rs = Integer.parseInt(fullInstruction.substring(6, 11), 2);
                rt = Integer.parseInt(fullInstruction.substring(11, 16), 2);
                immediate = (byte) Integer.parseInt(fullInstruction.substring(16), 2);
                loadW(rs, rt, immediate);
                break;
            case 43:
                rs = Integer.parseInt(fullInstruction.substring(6, 11), 2);
                rt = Integer.parseInt(fullInstruction.substring(11, 16), 2);
                immediate = (byte) Integer.parseInt(fullInstruction.substring(16), 2);
                storeW(rs, rt, immediate);
                break;
            case 3:
                address = Integer.parseInt(fullInstruction.substring(11), 2);
                jump(address);
                break;
            case 4:
                rs = Integer.parseInt(fullInstruction.substring(6, 11), 2);
                rt = Integer.parseInt(fullInstruction.substring(11, 16), 2);
                immediate = (byte) Integer.parseInt(fullInstruction.substring(16), 2);
                branchIfEqual(rs, rt, immediate);
                break;
        }
        System.out.println("Register file contents: ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < RegisterFile.length; i++) {
            if (i == 0) sb.append("Reg ZERO: " + RegisterFile[i]);
            else sb.append("Reg " + (i) + ": " + RegisterFile[i]);
            sb.append("\n");
        }
        System.out.println(sb.toString());

        System.out.println("Instruction memory contents: ");
        System.out.println(InstructionMemory);

        System.out.println("Data memory contents: ");
        System.out.println(DataMemory);
        System.out.println("---------------------------");

        // Continuing the program recursively.
        fetchAndExecute();
    }

    static void add(int rs, int rt, int rd) {
        if (rd == 0) {
            System.out.println("Attempting to write in Zero register.");
            return;
        }
        int toRead1 = RegisterFile[rs];
        int toRead2 = RegisterFile[rt];
        int result = toRead1 + toRead2;
        if (RegisterWrite) RegisterFile[rd] = result;
        RegisterWrite = false;

        String readData1 = String.format("%32s", Integer.toBinaryString(RegisterFile[rs])).replace(' ', '0');
        String readData2 = String.format("%32s", Integer.toBinaryString(RegisterFile[rt])).replace(' ', '0');
        String writeData = String.format("%32s", Integer.toBinaryString(RegisterFile[rd])).replace(' ', '0');
        System.out.println("In add --> " +
                "[Read Register #1: " + rs + ", Read Data #1: " + readData1 +
                ", Read Register #2: " + rt + ", Read Data #2: " + readData2 +
                ", Write Register: " + rd + ", Write Data: " + writeData + "]");
    }

    static void subtract(int rs, int rt, int rd) {
        if (rd == 0) {
            System.out.println("Attempting to write in Zero register.");
            return;
        }
        int toRead1 = RegisterFile[rs];
        int toRead2 = RegisterFile[rt];
        int result = toRead1 - toRead2;
        if (RegisterWrite) RegisterFile[rd] = result;
        RegisterWrite = false;

        String readData1 = String.format("%32s", Integer.toBinaryString(RegisterFile[rs])).replace(' ', '0');
        String readData2 = String.format("%32s", Integer.toBinaryString(RegisterFile[rt])).replace(' ', '0');
        String writeData = String.format("%32s", Integer.toBinaryString(RegisterFile[rd])).replace(' ', '0');
        System.out.println("In subtract -->" +
                "[Read Register #1: " + rs + ", Read Data #1: " + readData1 +
                ", Read Register #2: " + rt + ", Read Data #2: " + readData2 +
                ", Write Register: " + rd + ", Write Data: " + writeData + "]");
    }

    static void loadW(int rs, int rt, byte immediate) {
        if (rt == 0) {
            System.out.println("Attempting to write in Zero register.");
            return;
        }
        int address = immediate + RegisterFile[rs];
        if (RegisterWrite) RegisterFile[rt] = DataMemory.get(address);
        RegisterWrite = false;

        String readData = String.format("%32s", Integer.toBinaryString(RegisterFile[rs])).replace(' ', '0');
        String writeData = String.format("%32s", Integer.toBinaryString(DataMemory.get(address))).replace(' ', '0');
        System.out.println("In load --> " +
                "[Read Register: " + rs + ", Read Data: " + readData +
                ", Write Register: " + rt + ", Write Data: " + writeData + "]");
    }

    static void storeW(int rs, int rt, int immediate) {
        String readData1 = String.format("%32s", Integer.toBinaryString(RegisterFile[rs])).replace(' ', '0');
        String readData2 = String.format("%32s", Integer.toBinaryString(RegisterFile[rt])).replace(' ', '0');
        System.out.println("In store --> " +
                "[Read Register #1: " + rs + ", Read Data #1: " + readData1 +
                ", Read Register #2: " + rt + ", Read Data #2: " + readData2 + "]");
        int address = immediate + RegisterFile[rs];
        DataMemory.set(address, RegisterFile[rt]);
    }

    static void jump(int address) {
        System.out.println("In jump --> address = " + address);
        PCounter = address;
    }

    static void branchIfEqual(int rs, int rt, byte immediate) {
        String readData1 = String.format("%32s", Integer.toBinaryString(RegisterFile[rs])).replace(' ', '0');
        String readData2 = String.format("%32s", Integer.toBinaryString(RegisterFile[rt])).replace(' ', '0');
        System.out.println("In branch if equal --> " +
                "[Read Register #1: " + rs + ", Read Data #1: " + readData1 +
                ", Read Register #2: " + rt + ", Read Data #2: " + readData2 + "]");

        if(RegisterFile[rs] == RegisterFile[rt]) PCounter = immediate;
    }
}
