package it.unibz.parser;

import it.unibz.aom.typesquare.EntityType;

import java.util.Stack;

public class ParserStack {

    private final Stack<EntityType> stack;

    public ParserStack() {
        stack = new Stack<>();
    }

    public void push(EntityType entityType) {
        stack.push(entityType);
    }

    public EntityType pop() {
        return stack.pop();
    }

    public EntityType peek() {
        if (stack.isEmpty())
            return null;
        return stack.peek();
    }

    public void debug() {
        System.out.print ("ParserStack:");
        stack.forEach(entityType -> System.out.print(entityType.getName() + ", "));
        System.out.println();
    }

}
