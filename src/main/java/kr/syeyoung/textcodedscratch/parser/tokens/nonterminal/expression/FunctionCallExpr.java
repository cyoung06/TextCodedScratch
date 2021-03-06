package kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression;

import kr.syeyoung.textcodedscratch.parser.*;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.FunctionExprCallMicroStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.FunctionExprCallStackClearingMicroStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.ReturnStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.Statements;
import kr.syeyoung.textcodedscratch.parser.util.ScriptBuilder;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.FunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.FunctionDeclaration;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.IdentifierToken;
import kr.syeyoung.textcodedscratch.parser.util.StackHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class FunctionCallExpr implements Expression, FunctionCall, StatementFormedListener, StackRequringOperation, ICodeContextConsumer {
    private IdentifierToken identifierToken;
    private Expression[] parameters;
    private FunctionDeclaration functionDeclaration;

    public FunctionCallExpr(IdentifierToken token, Expression[] expressions) {
        this.identifierToken = token;
        this.parameters = expressions;
    }

    private ICodeContext context;
    @Override
    public void setICodeContext(ICodeContext context) {
        this.context = context;
    }

    public IdentifierToken getFunctionName() {
        return identifierToken;
    }
    public Expression[] getParameters() {
        return parameters;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public FunctionParameter.ParameterType getReturnType() {
        return FunctionParameter.ParameterType.TEXT;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    public void setFunctionDeclaration(FunctionDeclaration functionDeclaration) {
        this.functionDeclaration = functionDeclaration;
    }

    @Override
    public ParserNode[] getChildren() {
        ParserNode[] parserNodes = new ParserNode[parameters.length + 1];
        parserNodes[0] = identifierToken;
        System.arraycopy(parameters,0,parserNodes, 1, parameters.length);
        return parserNodes;
    }

    @Override
    public String toString() {
        return "{Function Call: "+identifierToken+", parameters: ["+ Arrays.asList(parameters).stream().map(expr -> expr.toString()).collect(Collectors.joining(", "))+"]}";
    }

    @Override
    public Object[] buildJSON(String parentId, String nextId, ScriptBuilder builder) {
        String id= StackHelper.accessStack(builder, parentId, nextId, stmt.getStackCountAtExecution() - fecms.getCurrentStack() + (stmt instanceof StackAddingOperation ? -1 : stmt instanceof StackRemovingOperation ? 1 : 0), context);
        return new Object[] {id, id};
    }

    private Statements stmt;
    private FunctionExprCallMicroStatement fecms;
    private FunctionExprCallStackClearingMicroStatement fecscm;
    @Override
    public void process(Statements formed, ParserNode parent, LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        // TODO
        this.stmt = formed;
        int index = future.indexOf(formed);
        if (!(stmt instanceof ReturnStatement))
            future.add(index+1, fecscm = new FunctionExprCallStackClearingMicroStatement(formed));
        future.addFirst(fecms = new FunctionExprCallMicroStatement(identifierToken, parameters, formed));

        for (Expression param : parameters) {
            if (param instanceof StatementFormedListener)
                ((StatementFormedListener) param).process(fecms, parent, past, future);
        }
    }

    public void onStatementChange(Statements formed) {
        stmt = formed;
        fecms.setStmt(formed);
        if (fecscm != null)
            fecscm.setStmt(formed);


        for (Expression param : parameters) {
            if (param instanceof StatementFormedListener)
                ((StatementFormedListener) param).onStatementChange(fecms);
        }
    }


    private int stack;
    @Override
    public void setCurrentStack(int stackSize) {
        this.stack = stackSize;
    }

    @Override
    public int getCurrentStack() {
        return stack;
    }
}
