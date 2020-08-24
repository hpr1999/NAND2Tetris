package base;

import parsing.AssemblerFile;

public class Assembler {

    public static void main(String[] args) {
        ArgumentProcessor processor = new ArgumentProcessor(args);
        processor.process();
        AssemblerFile assemblerFile = new AssemblerFile(processor.getAsmFilePath());
    }
}
