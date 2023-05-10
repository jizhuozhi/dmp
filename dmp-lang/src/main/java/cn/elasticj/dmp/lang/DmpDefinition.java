package cn.elasticj.dmp.lang;

public class DmpDefinition {

    final int slots;
    final Bytecode[] bytecodes;

    DmpDefinition(int slots, Bytecode[] bytecodes) {
        this.slots = slots;
        this.bytecodes = bytecodes;
    }
}