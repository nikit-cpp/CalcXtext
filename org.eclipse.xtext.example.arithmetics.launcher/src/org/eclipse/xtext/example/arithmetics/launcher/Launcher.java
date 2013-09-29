package org.eclipse.xtext.example.arithmetics.launcher;

import java.math.BigDecimal;

import org.eclipse.xtext.example.arithmetics.arithmetics.Evaluation;
import org.eclipse.xtext.example.arithmetics.arithmetics.Module;
import org.eclipse.xtext.example.arithmetics.arithmetics.Statement;
import org.eclipse.xtext.example.arithmetics.interpreter.Calculator;

public class Launcher {
	public static void main(String[] args){

        double result = evaluate(getStatement("module test 2+2*2;")).doubleValue();
        System.out.println(result);
	}
	
    protected static Statement getStatement(String string) throws Exception {
        Module model = (Module) getModel(string);
        Statement statement = model.getStatements().get(0);
        return statement;
    }

    private BigDecimal evaluate(Statement statement) {
        return new Calculator().evaluate(((Evaluation)statement).getExpression());
    }

}
