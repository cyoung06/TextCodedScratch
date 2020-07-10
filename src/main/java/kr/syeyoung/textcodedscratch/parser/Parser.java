package kr.syeyoung.textcodedscratch.parser;

import kr.syeyoung.textcodedscratch.parser.rule.*;

import java.io.File;
import java.util.*;

public class Parser {
    private LinkedList<ParserNode> input;
    private LinkedList<ParserNode> out = new LinkedList<>();

    private static final List<ParserRule> rules = new ArrayList<>();

    private SyntexCheckerRule syntexCheckerRule;

    public SyntexCheckerRule getSyntexCheckerRule() {
        return syntexCheckerRule;
    }

    private File f;
    public Parser(LinkedList<ParserNode> stack, File f) {
        this.input = stack;
        this.f = f;
        initRules();
    }

    public void parse() {
        while (input.size() != 0) {
            out.add(input.removeFirst());
            for (ParserRule rule : rules) {
                try {
                    if (rule.process(out, input)) break;
                } catch (NoSuchElementException exception) {} catch (Exception e) {
                    for (ParserNode node : out) {
                        System.out.println(node.getClass().getName() + " - " + node);
                    }
                    throw e;
                }
            }
        }
    }

    public LinkedList<ParserNode> getOutput() {
        return out;
    }

    private void initRules() {
        rules.add(syntexCheckerRule = new SyntexCheckerRule(f));
        rules.add(new IncludeRule(f));


        // dec group
        rules.add(new EOSConcatRule());
        rules.add(new SpriteDeclarationRule());
        rules.add(new RequireDeclarationRule());
        rules.add(new CostumeDeclarationRule());
        rules.add(new SoundDeclarationRule());
        rules.add(new VariableDeclarationRule());
        rules.add(new ListDeclarationRule());
        rules.add(new StatementGroupingRule());
        rules.add(new IdentifierAccessorConcatRule());
        rules.add(new EventDeclarationRule(syntexCheckerRule));
        rules.add(new FunctionDeclarationRule(syntexCheckerRule));
        rules.add(new ExtensionDeclarationRule());
        rules.add(new NativeEventDeclarationRule());

        // expre
        rules.add(new VariableExpresionRule());
        rules.add(new FunctionParameterRule());

        // stmts
        rules.add(new IfStatementRule());
        rules.add(new IfElseStatementRule());
        rules.add(new WhileStatementRule());
        rules.add(new RepeatStatementRule());
        rules.add(new ReturnStatementRule());

        rules.add(new FunctionCallRule());
        rules.add(new VariableAssignmentRule());
        rules.add(new FunctionExprCallRule());


        // expr red
        rules.add(new ExpressionReducingRule());


    }
}
