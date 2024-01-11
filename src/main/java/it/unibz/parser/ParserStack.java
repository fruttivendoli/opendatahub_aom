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

    public String getScope() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < stack.size(); i++) {
            if (i == 0)
                sb.append(stack.get(i).getName());
            else
                sb.append("/" + stack.get(i).getName());
        }

        return sb.toString();
    }

    public void debug() {
        System.out.print ("ParserStack:");
        if (stack.isEmpty())
            System.out.print(" empty");

        for (int i = 0; i < stack.size(); i++) {
            if (i == 0)
                System.out.print(stack.get(i).getName());
            else
                System.out.print(" -> " + stack.get(i).getName());
        }

        System.out.println();
    }

}
