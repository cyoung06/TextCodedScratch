package kr.syeyoung.textcodedscratch.parser.util;

import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.*;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.NativeFunctionCallStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.RepeatStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorGetName;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorMinus;
import org.json.JSONObject;

public class StackHelper {
    public static String deallocateStack(ScriptBuilder builder, String prev, String next, int size, ICodeContext context) {
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("deleteIndex"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("index"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{\"INDEX\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_deleteoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), false);
        NativeFunctionCallStatement nfcs = new NativeFunctionCallStatement(new IdentifierToken("deleteIndex"), new Expression[] {
                new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName()),
                new StringToken("\"last\"")
        }, nfd);
        nfcs.setICodeContext(context);

        Statements stmts;
        if (size == 1) stmts = nfcs;
        else stmts = new RepeatStatement(new NumberToken(size), nfcs);

        return (String) stmts.buildJSON(prev, next, builder)[0];
    }
    public static String deallocateStackOffset(ScriptBuilder builder, String prev, String next, int offset, ICodeContext context) {
        NativeFunctionCallExpr nfce = null;
        NativeFunctionDeclaration nfdSize = new NativeFunctionDeclaration(new IdentifierToken("size"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER)
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{},\"topLevel\":false,\"opcode\":\"data_lengthoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), true);
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("deleteIndex"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("index"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{\"INDEX\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_deleteoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), false);
        NativeFunctionCallStatement nfcs = new NativeFunctionCallStatement(new IdentifierToken("deleteIndex"), new Expression[] {
                new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName()),
                new TwoTermedExpression(nfce = new NativeFunctionCallExpr(new IdentifierToken("size"), new Expression[] {
                        new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName())
                }, nfdSize), new OperatorMinus(), new NumberToken(offset))
        }, nfd);
        if (nfce != null) nfce.setICodeContext(context);
        nfcs.setICodeContext(context);

        return (String) nfcs.buildJSON(prev, next, builder)[0];
    }
    public static String deallocateStackOffset(ScriptBuilder builder, String prev, String next, int offset, int size, ICodeContext context) {
        NativeFunctionCallExpr nfce = null;
        NativeFunctionDeclaration nfdSize = new NativeFunctionDeclaration(new IdentifierToken("size"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER)
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{},\"topLevel\":false,\"opcode\":\"data_lengthoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), true);
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("deleteIndex"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("index"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{\"INDEX\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_deleteoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), false);
        NativeFunctionCallStatement nfcs = new NativeFunctionCallStatement(new IdentifierToken("deleteIndex"), new Expression[] {
                new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName()),
                new TwoTermedExpression(nfce = new NativeFunctionCallExpr(new IdentifierToken("size"), new Expression[] {
                        new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName())
                }, nfdSize), new OperatorMinus(), new NumberToken(offset))
        }, nfd);
        if (nfce != null) nfce.setICodeContext(context);
        nfcs.setICodeContext(context);

        Statements stmts;
        if (size == 1) stmts = nfcs;
        else stmts = new RepeatStatement(new NumberToken(size), nfcs);

        return (String) stmts.buildJSON(prev, next, builder)[0];
    }
    public static String accessStack(ScriptBuilder builder, String prev, String next, int sizeDiff, ICodeContext context) {
        NativeFunctionCallExpr nfce = null;
        NativeFunctionDeclaration nfdSize = new NativeFunctionDeclaration(new IdentifierToken("size"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER)
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{},\"topLevel\":false,\"opcode\":\"data_lengthoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), true);
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("get"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("index"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{\"INDEX\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_itemoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), true);
        NativeFunctionCallExpr nfcs = new NativeFunctionCallExpr(new IdentifierToken("get"), new Expression[] {
                new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName()),
                sizeDiff == 0 ?  nfce = new NativeFunctionCallExpr(new IdentifierToken("size"), new Expression[] {
                        new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName())
                }, nfdSize) : new TwoTermedExpression(nfce = new NativeFunctionCallExpr(new IdentifierToken("size"), new Expression[] {
                        new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName())
                }, nfdSize), new OperatorMinus(), new NumberToken(sizeDiff))
        }, nfd);
        if (nfce != null) nfce.setICodeContext(context);
        nfcs.setICodeContext(context);

        return (String) nfcs.buildJSON(prev, next, builder)[0];
    }
    public static String replaceStack(ScriptBuilder builder, String prev, String next, int sizeDiff, Expression exprToPut, ICodeContext context) {
        NativeFunctionDeclaration nfdSize = new NativeFunctionDeclaration(new IdentifierToken("size"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER)
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{},\"topLevel\":false,\"opcode\":\"data_lengthoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), true);
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("get"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("index"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{\"ITEM\":[1,\"$TCS$I2\"],\"INDEX\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_replaceitemoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), false);
        NativeFunctionCallExpr nfce = null;
        NativeFunctionCallStatement nfcs = new NativeFunctionCallStatement(new IdentifierToken("replace"), new Expression[] {
                new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName()),
                sizeDiff == 0 ?  new NativeFunctionCallExpr(new IdentifierToken("size"), new Expression[] {
                        new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName())
                }, nfdSize) : new TwoTermedExpression(nfce = new NativeFunctionCallExpr(new IdentifierToken("size"), new Expression[] {
                        new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true,false, context), new OperatorGetName())
                }, nfdSize), new OperatorMinus(), new NumberToken(sizeDiff)),
                exprToPut
        }, nfd);
        if (nfce != null) nfce.setICodeContext(context);
        nfcs.setICodeContext(context);

        return (String) nfcs.buildJSON(prev, next, builder)[0];
    }
    public static String putStack(ScriptBuilder builder, String prev, String next, Expression exprToPut, ICodeContext context) {
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("add"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("element"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("\"{\"shadow\":false,\"inputs\":{\"ITEM\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_addtolist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}\""), false);
        NativeFunctionCallStatement nfcs = new NativeFunctionCallStatement(new IdentifierToken("add"), new Expression[] {
                new OneTermedExpression(createVarExprWithContext(new IdentifierToken("$THREAD_STACK$"), true, false, context), new OperatorGetName()),
                exprToPut
        }, nfd);
        nfcs.setICodeContext(context);

        return (String) nfcs.buildJSON(prev, next, builder)[0];
    }
    
    private static VariableExpression createVarExprWithContext(IdentifierToken id, boolean list, boolean global, ICodeContext context) {
        VariableExpression variableExpression = new VariableExpression(new IdentifierToken("$THREAD_STACK$"), true, false);
        variableExpression.setICodeContext(context);
        return variableExpression;
    }
}
