package cn.elasticj.dmp.lang;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DmpCompiler {

    public DmpCompiler() {
    }

    public Bytecode[] compile(Reader reader) {
        try {
            DmpLexer lexer = new DmpLexer(CharStreams.fromReader(reader));
            DmpParser parser = new DmpParser(new CommonTokenStream(lexer));
            Visitor visitor = new Visitor();
            visitor.visitProgram(parser.program());
            return visitor.bytecodes.toArray(new Bytecode[0]);
        } catch (Throwable t) {
            throw new DmpException(t);
        }
    }

    private static final class Visitor extends DmpBaseVisitor<List<Bytecode>> {

        private final List<Bytecode> bytecodes = new ArrayList<>();

        @Override
        public List<Bytecode> visitProgram(DmpParser.ProgramContext ctx) {
            return visitExpr(ctx.expr());
        }

        @Override
        public List<Bytecode> visitMapping(DmpParser.MappingContext ctx) {
            String ident = ctx.IDENT().getText();
            visitExpr(ctx.expr());
            bytecodes.add(new Bytecode(Op.PUT_FIELD, ident));
            return bytecodes;
        }

        @Override
        public List<Bytecode> visitDot(DmpParser.DotContext ctx) {
            bytecodes.add(new Bytecode(Op.LOAD_ORIGIN));
            return bytecodes;
        }

        @Override
        public List<Bytecode> visitNumber(DmpParser.NumberContext ctx) {
            List<TerminalNode> numbers = ctx.NUMBER();
            Object value;
            if (numbers.size() == 1) {
                value = new BigInteger(ctx.getText());
            } else {
                value = new BigDecimal(ctx.getText());
            }
            bytecodes.add(new Bytecode(Op.PUSH, value));
            return bytecodes;
        }

        @Override
        public List<Bytecode> visitBool(DmpParser.BoolContext ctx) {
            bytecodes.add(new Bytecode(Op.PUSH, Boolean.valueOf(ctx.getText())));
            return bytecodes;
        }

        @Override
        public List<Bytecode> visitString(DmpParser.StringContext ctx) {
            String string = ctx.STRING().getText();
            if (string.startsWith("'")) {
                string = string.replaceAll("\\\\'", "'");
            } else {
                string = string.replaceAll("\\\\\"", "\"");
            }
            string = string.substring(1, string.length() - 1);
            bytecodes.add(new Bytecode(Op.PUSH, string));
            return bytecodes;
        }

        @Override
        public List<Bytecode> visitField(DmpParser.FieldContext ctx) {
            String ident = ctx.IDENT().getText();
            bytecodes.add(new Bytecode(Op.GET_FIELD, ident));
            return bytecodes;
        }

        @Override
        public List<Bytecode> visitObject(DmpParser.ObjectContext ctx) {
            bytecodes.add(new Bytecode(Op.NEW));
            bytecodes.add(new Bytecode(Op.FRAME_NEW, 1));
            for (DmpParser.MappingContext mappingContext : ctx.mapping()) {
                bytecodes.add(new Bytecode(Op.LOAD_SLOT, 0));
                visitMapping(mappingContext);
            }
            bytecodes.add(new Bytecode(Op.LOAD_SLOT, 0));
            bytecodes.add(new Bytecode(Op.FRAME_RETURN));
            return bytecodes;
        }

        @Override
        public List<Bytecode> visitExpr(DmpParser.ExprContext ctx) {
            if (ctx.dot() != null) {
                return visitDot(ctx.dot());
            } else if (ctx.number() != null) {
                return visitNumber(ctx.number());
            } else if (ctx.bool() != null) {
                return visitBool(ctx.bool());
            } else if (ctx.string() != null) {
                return visitString(ctx.string());
            } else if (ctx.field() != null && !ctx.field().isEmpty()) {
                bytecodes.add(new Bytecode(Op.LOAD_ORIGIN));
                for (DmpParser.FieldContext fieldContext : ctx.field()) {
                    visitField(fieldContext);
                }
                return bytecodes;
            } else if (ctx.object() != null) {
                return visitObject(ctx.object());
            }
            return bytecodes;
        }
    }
}
