package kr.syeyoung.textcodedscratch.parser.util;

import kr.syeyoung.textcodedscratch.parser.ScratchTransferable;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.NativeFunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.*;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.GroupedStatements;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.NativeFunctionCallStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.RepeatStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.NumberToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.constant.StringToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorGetName;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.operators.OperatorMinus;
import org.json.JSONObject;

public class StackHelper {
    public static String deallocateStack(ScriptBuilder builder, String prev, String next, int size) {
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("deleteIndex"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("index"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("{\"shadow\":false,\"inputs\":{\"INDEX\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_deleteoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}"), false);
        NativeFunctionCallStatement nfcs = new NativeFunctionCallStatement(new IdentifierToken("deleteIndex"), new Expression[] {
                new OneTermedExpression(new VariableExpression(new IdentifierToken("$THREAD_STACK$"), true), new OperatorGetName()),
                new StringToken("last")
        }, nfd);

        RepeatStatement stmt = new RepeatStatement(new NumberToken(size), nfcs);

        return (String) stmt.buildJSON(prev, next, builder);
    }
    public static String accessStack(ScriptBuilder builder, String prev, String next, int sizeDiff) {
        NativeFunctionDeclaration nfdSize = new NativeFunctionDeclaration(new IdentifierToken("size"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER)
        }, new StringToken( "{\"shadow\":false,\"inputs\":{},\"topLevel\":false,\"opcode\":\"data_lengthoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}"), true);
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("get"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("index"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("{\"shadow\":false,\"inputs\":{\"INDEX\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_itemoflist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}"), true);
        NativeFunctionCallExpr nfcs = new NativeFunctionCallExpr(new IdentifierToken("get"), new Expression[] {
                new OneTermedExpression(new VariableExpression(new IdentifierToken("$THREAD_STACK$"), true), new OperatorGetName()),
                new TwoTermedExpression(new NativeFunctionCallExpr(new IdentifierToken("size"), new Expression[] {
                        new OneTermedExpression(new VariableExpression(new IdentifierToken("$THREAD_STACK$"), true), new OperatorGetName())
                }, nfdSize), new OperatorMinus(), new NumberToken(sizeDiff))
        }, nfd);

        return (String) nfcs.buildJSON(prev, next, builder);

    }
    public static String putStack(ScriptBuilder builder, String prev, String next, Expression exprToPut) {
        NativeFunctionDeclaration nfd = new NativeFunctionDeclaration(new IdentifierToken("add"), new FunctionParameter[]{
                new FunctionParameter(new IdentifierToken("variable"), FunctionParameter.ParameterType.VARIABLE_POINTER),
                new FunctionParameter(new IdentifierToken("element"), FunctionParameter.ParameterType.TEXT),
        }, new StringToken("{\"shadow\":false,\"inputs\":{\"ITEM\":[1,\"$TCS$I1\"]},\"topLevel\":false,\"opcode\":\"data_addtolist\",\"fields\":{\"LIST\":\"$TCS$I0\"}}"), false);
        NativeFunctionCallStatement nfcs = new NativeFunctionCallStatement(new IdentifierToken("add"), new Expression[] {
                new OneTermedExpression(new VariableExpression(new IdentifierToken("$THREAD_STACK$"), true), new OperatorGetName()),
                exprToPut
        }, nfd);

        return (String) nfcs.buildJSON(prev, next, builder);
    }
}
