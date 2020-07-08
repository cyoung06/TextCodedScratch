package kr.syeyoung.textcodedscratch.parser.rule;

import kr.syeyoung.textcodedscratch.parser.ParserNode;
import kr.syeyoung.textcodedscratch.parser.StackAddingOperation;
import kr.syeyoung.textcodedscratch.parser.StackRemovingOperation;
import kr.syeyoung.textcodedscratch.parser.StackRequringOperation;
import kr.syeyoung.textcodedscratch.parser.context.ICodeContext;
import kr.syeyoung.textcodedscratch.parser.context.SpriteDefinition;
import kr.syeyoung.textcodedscratch.parser.exception.ParsingGrammarException;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.FunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.NativeFunctionCall;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.*;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.expression.*;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.function.FunctionParameter;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.FunctionCallStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.statements.NativeFunctionCallStatement;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.CBCloseToken;
import kr.syeyoung.textcodedscratch.parser.tokens.terminal.brackets.CBOpenToken;

import java.util.LinkedList;

public class SyntexCheckerRule implements ParserRule {
    private SpriteDefinition definition = new SpriteDefinition();

    private LinkedList<ICodeContext> variableContextQueue = new LinkedList<>();
    private ICodeContext lastContext = definition;

    public SpriteDefinition getDefinition() {
        return definition;
    }

    public SyntexCheckerRule() {
        variableContextQueue.add(definition);
    }

    @Override
    public boolean process(LinkedList<ParserNode> past, LinkedList<ParserNode> future) {
        ParserNode node = past.getLast();
        if (node instanceof SpriteDeclaration) {
            if (definition.getSpriteName() == null) {
                definition.setSpriteName((SpriteDeclaration) node);
            } else {
                throw new ParsingGrammarException("Duplicate Sprite Declaration. Use Only ONE");
            }
        } else if (node instanceof VariableDeclaration) {
            String varName = ((VariableDeclaration)node).getName().getMatchedStr();
            if (checkDuplicate(varName)) throw new ParsingGrammarException("Same Identifier used as variable twice" + varName);
            if (node instanceof CostumeDeclaration) {
                definition.getCostumes().put(varName, (CostumeDeclaration) node);
            }
            if (node instanceof SoundDeclaration) {
                definition.getSounds().put(varName, (SoundDeclaration) node);
            }
            if (node instanceof FunctionParameter) return false;
            lastContext.putVariable((VariableDeclaration) node);
        } else if (node instanceof ListDeclaration) {
            String varName = ((ListDeclaration)node).getName().getMatchedStr();
            if (checkDuplicate(varName)) throw new ParsingGrammarException("Same Identifier used as variable twice" + varName);
            definition.getLists().put(varName, (ListDeclaration) node);
        } else if (node instanceof FunctionDeclaration) {
            String funcName = ((FunctionDeclaration) node).getName().getMatchedStr();
            if (definition.getFunctions().containsKey(funcName)) throw new ParsingGrammarException("Same Identifier used as function twice" + funcName);
            definition.getFunctions().put(funcName, (FunctionDeclaration) node);
        } else if (node instanceof ExtensionDeclaration) {
            definition.getExtensionDeclarations().add((ExtensionDeclaration) node);
        } else if (node instanceof FunctionCall) {
            FunctionCall fcs = (FunctionCall) node;
            FunctionDeclaration fd = definition.getFunctions().get(fcs.getFunctionName().getMatchedStr());
            if (fd == null) throw new ParsingGrammarException("Not defined function used" + fcs.getFunctionName().getMatchedStr());
            if (fd.getParameters().length != fcs.getParameters().length) throw new ParsingGrammarException("Parameter size mismatch" + fcs.getFunctionName().getMatchedStr());
            fcs.setFunctionDeclaration(fd);
            for (int i = 0; i < fd.getParameters().length; i++) {
                if (fd.getParameters()[i].getType() == FunctionParameter.ParameterType.VARIABLE_POINTER) {
                    if (!(fcs.getParameters()[i] instanceof OneTermedExpression && fcs.getParameters()[i].getReturnType() == FunctionParameter.ParameterType.VARIABLE_POINTER)) {
                        throw new ParsingGrammarException("Variable pointer expected on function call to " + fcs.getFunctionName().getMatchedStr() +" But found non-variable pointer");
                    }
                } else {
                    if (fcs.getParameters()[i] instanceof OneTermedExpression && fcs.getParameters()[i].getReturnType() == FunctionParameter.ParameterType.VARIABLE_POINTER) {
                        throw new ParsingGrammarException("Non-variable pointer expected on function call to " + fcs.getFunctionName().getMatchedStr() +" But found variable pointer");
                    }
                }
            }
            if (fd instanceof NativeFunctionDeclaration && !(fcs instanceof NativeFunctionCall)) {
                past.removeLast();
                if (fcs instanceof FunctionCallStatement) {
                    past.addLast(new NativeFunctionCallStatement(fcs.getFunctionName(), fcs.getParameters(), (NativeFunctionDeclaration) fd));
                } else if (fcs instanceof FunctionCallExpr) {
                    past.addLast(new NativeFunctionCallExpr(fcs.getFunctionName(), fcs.getParameters(), (NativeFunctionDeclaration) fd));
                } else {
                    throw new AssertionError("What the heck just happened");
                }
            }
        } else if (node instanceof NativeEventDeclaration) {
            NativeEventDeclaration fcs = (NativeEventDeclaration) node;
            NativeEventDeclaration fd = definition.getEventsDefined().get(fcs.getName().getMatchedStr());
            if (fd != null) throw new ParsingGrammarException("Native Event Defined Twice" + fcs.getName().getMatchedStr());
            definition.getEventsDefined().put(fcs.getName().getMatchedStr(), fcs);
        }else if (node instanceof EventDeclaration) {
            EventDeclaration fcs = (EventDeclaration) node;
            NativeEventDeclaration fd = definition.getEventsDefined().get(fcs.getEvent().getMatchedStr());
            if (fd == null) throw new ParsingGrammarException("Not defined event used" + fcs.getEvent().getMatchedStr());
            fcs.setEventJsonDeclaration(fd);

            definition.getEvents().add(fcs);
        } else if (node instanceof VariableExpression) {
            String name = ((VariableExpression) node).getVariableName().getMatchedStr();
            if (lastContext.isVarialbeDefined(name)) {
                VariableDeclaration varDec = lastContext.getVariable(name);
                if (varDec instanceof CostumeDeclaration || varDec instanceof SoundDeclaration) {
                    past.removeLast();
                    past.addLast(new ConstantVariableExpression(varDec.getName(), varDec.getDefaultValue()));
                } else if (varDec instanceof LocalVariableDeclaration) {
                    past.removeLast();
                    past.addLast(node = new LocalVariableExpression(varDec.getName(), (LocalVariableDeclaration) varDec));
                }
            } else if (definition.getLists().containsKey(name)) {
                ((VariableExpression) node).setList(true);
            } else {
                throw new ParsingGrammarException("Variable or List not defined" + name);
            }
        } else if (node instanceof CBOpenToken) {
            variableContextQueue.add(lastContext = ((CBOpenToken) node).createContext(lastContext));
        } else if (node instanceof CBCloseToken) {
            ((CBCloseToken) node).setICodeContext(lastContext);
            variableContextQueue.removeLast();
            lastContext = variableContextQueue.getLast();
        }
        if (node instanceof StackAddingOperation) {
            lastContext.incrementStackCount();
        }
        if (node instanceof StackRemovingOperation) {
            lastContext.decrementStackCount();
        }
        if (node instanceof StackRequringOperation) {
            ((StackRequringOperation) node).setCurrentStack(lastContext.getTotalStackSize());
        }

        return false;
    }

    public boolean checkDuplicate(String name) {
        if (lastContext.isVarialbeDefined(name)) {
            return true;
        } else if (definition.getLists().containsKey(name)) {
            return true;
        }
        return false;
    }
}
