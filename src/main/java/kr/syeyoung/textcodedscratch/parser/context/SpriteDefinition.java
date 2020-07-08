package kr.syeyoung.textcodedscratch.parser.context;

import kr.syeyoung.textcodedscratch.parser.tokens.nonterminal.declaration.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SpriteDefinition implements ICodeContext {
    private SpriteDeclaration spriteName;
    private HashMap<String, VariableDeclaration> variables = new HashMap<>();
    private HashMap<String, ListDeclaration> lists = new HashMap<>();  // LISTS
    private HashMap<String, CostumeDeclaration> costumes = new HashMap<>();
    private HashMap<String, SoundDeclaration> sounds = new HashMap<>();
    private HashMap<String, FunctionDeclaration> functions = new HashMap<>();
    private HashMap<String, NativeEventDeclaration> eventsDefined = new HashMap<>();
    private List<EventDeclaration> events = new ArrayList<>();
    private List<ExtensionDeclaration> extensionDeclarations = new ArrayList<>();

    public HashMap<String, NativeEventDeclaration> getEventsDefined() {
        return eventsDefined;
    }

    public void setEventsDefined(HashMap<String, NativeEventDeclaration> eventsDefined) {
        this.eventsDefined = eventsDefined;
    }

    public SpriteDeclaration getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(SpriteDeclaration spriteName) {
        this.spriteName = spriteName;
    }

    public HashMap<String, VariableDeclaration> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, VariableDeclaration> variables) {
        this.variables = variables;
    }

    public HashMap<String, ListDeclaration> getLists() {
        return lists;
    }

    public void setLists(HashMap<String, ListDeclaration> lists) {
        this.lists = lists;
    }

    public HashMap<String, CostumeDeclaration> getCostumes() {
        return costumes;
    }

    public void setCostumes(HashMap<String, CostumeDeclaration> costumes) {
        this.costumes = costumes;
    }

    public HashMap<String, SoundDeclaration> getSounds() {
        return sounds;
    }

    public void setSounds(HashMap<String, SoundDeclaration> sounds) {
        this.sounds = sounds;
    }

    public HashMap<String, FunctionDeclaration> getFunctions() {
        return functions;
    }

    public void setFunctions(HashMap<String, FunctionDeclaration> functions) {
        this.functions = functions;
    }

    public List<EventDeclaration> getEvents() {
        return events;
    }

    public void setEvents(List<EventDeclaration> events) {
        this.events = events;
    }

    public List<ExtensionDeclaration> getExtensionDeclarations() {
        return extensionDeclarations;
    }

    public void setExtensionDeclarations(List<ExtensionDeclaration> extensionDeclarations) {
        this.extensionDeclarations = extensionDeclarations;
    }

    @Override
    public boolean isVarialbeDefined(String variable) {
        return variables.containsKey(variable);
    }

    @Override
    public VariableDeclaration getVariable(String variable) {
        return variables.get(variable);
    }

    @Override
    public void putVariable(VariableDeclaration variableDeclaration) {
        variables.put(Objects.requireNonNull(variableDeclaration).getName().getMatchedStr(), variableDeclaration);
    }

    @Override
    public int incrementStackCount() {
        throw new UnsupportedOperationException("Sprite Definition does not have stack defined");
    }

    @Override
    public int decrementStackCount() {
        throw new UnsupportedOperationException("Sprite Definition does not have stack defined");
    }

    @Override
    public int getLocalStackSize() {
        return 0;
    }

    @Override
    public int getTotalStackSize() {
        return 0;
    }
}
