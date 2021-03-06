/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.xtext.example.arithmetics.interpreter;

import types.TypedValue;
import inter.Env;
import inter.Interpreter;
import inter.returnables.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import lexer.Tag;
import main.OutputSystem;
import options.Options;

import org.eclipse.xtext.example.arithmetics.arithmetics.ArithmeticsPackage;
import org.eclipse.xtext.example.arithmetics.arithmetics.DeclaredParameter;
import org.eclipse.xtext.example.arithmetics.arithmetics.Definition;
import org.eclipse.xtext.example.arithmetics.arithmetics.Div;
import org.eclipse.xtext.example.arithmetics.arithmetics.Expression;
import org.eclipse.xtext.example.arithmetics.arithmetics.FunctionCall;
import org.eclipse.xtext.example.arithmetics.arithmetics.Minus;
import org.eclipse.xtext.example.arithmetics.arithmetics.Multi;
import org.eclipse.xtext.example.arithmetics.arithmetics.NumberLiteral;
import org.eclipse.xtext.example.arithmetics.arithmetics.Plus;
import org.eclipse.xtext.util.PolymorphicDispatcher;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import exceptions.MyException;

/**
 * an interpreter for instances of EClasses of the {@link ArithmeticsPackage}.
 * 
 * It internally uses a polymorphic dispatcher to dispatch between the implementations for the different EClasses.
 * 
 * @author Sven Efftinge - initial contribution and API
 */
public class Calculator {
	
	private PolymorphicDispatcher<TypedValue> dispatcher = PolymorphicDispatcher.createForSingleTarget("internalEvaluate", 2, 2, this);
	
	//@Inject
	//Env env;
	
	OutputSystem output;
	Options options;
	HashMap<String, TypedValue> table;
	Interpreter inter;
	
	// Конструктор инициализирует моё окружение из Executor
	public Calculator(){
		output = new OutputSystem();
		options = new Options(output);
		MyException.staticInit(options);
		table = new HashMap<String, TypedValue>();
		inter = new Interpreter(options, table, output);
		System.out.println("constructor Calculator"); // TODO не подходит - множественные вызовы(1)
	}
	
	// InterpreterAutoEdit начинает с него...
	public TypedValue evaluate(Expression obj) {
		return evaluate(obj, ImmutableMap.<String,TypedValue>of());
	}
	
	public TypedValue evaluate(Expression obj, ImmutableMap<String,TypedValue> values) {
		TypedValue invoke = dispatcher.invoke(obj, values);
		return invoke;
	}
	
	protected TypedValue internalEvaluate(Expression e, ImmutableMap<String,TypedValue> values) { 
		throw new UnsupportedOperationException(e.toString());
	}
	
	protected TypedValue internalEvaluate(NumberLiteral e, ImmutableMap<String,TypedValue> values) { 
		//return e.getValue();
		BigDecimal v = e.getValue();
		if(v.intValue()==v.doubleValue())
			return new TypedValue(v.intValue());
		else
			return new TypedValue(v.doubleValue());
	}
	
	protected TypedValue internalEvaluate(FunctionCall e, ImmutableMap<String,TypedValue> values) {
		if (e.getFunc() instanceof DeclaredParameter) {
			return values.get(e.getFunc().getName());
		} 
		Definition d = (Definition) e.getFunc();
		Map<String,TypedValue> params = Maps.newHashMap();
		for (int i=0; i<e.getArgs().size();i++) {
			DeclaredParameter declaredParameter = d.getArgs().get(i);
			TypedValue evaluate = evaluate(e.getArgs().get(i), values);
			params.put(declaredParameter.getName(), evaluate);
		}
		return evaluate(d.getExpr(),ImmutableMap.copyOf(params));
	}
	
	protected TypedValue internalEvaluate(Plus plus, ImmutableMap<String,TypedValue> values) throws Exception {
		//return evaluate(plus.getLeft(),values)
		//		.add(evaluate(plus.getRight(),values));
		TypedValue left = evaluate(plus.getLeft(),values);
		TypedValue right = evaluate(plus.getRight(),values);
		
		return inter.exec(new Expr(left, right, Tag.PLUS));
	}
	protected TypedValue internalEvaluate(Minus minus, ImmutableMap<String,TypedValue> values) throws Exception {
		//return evaluate(minus.getLeft(),values)
		//		.subtract(evaluate(minus.getRight(),values));
		
		TypedValue left = evaluate(minus.getLeft(),values);
		TypedValue right = evaluate(minus.getRight(),values);
		
		return inter.exec(new Expr(left, right, Tag.MINUS));
	}
	protected TypedValue internalEvaluate(Div div, ImmutableMap<String,TypedValue> values) throws Exception {
		TypedValue left = evaluate(div.getLeft(),values);
		TypedValue right = evaluate(div.getRight(),values);
		/*return left
				.divide(right,20,RoundingMode.HALF_UP);*/
		return inter.exec(new Term(left, right, Tag.DIV));
	}
	protected TypedValue internalEvaluate(Multi multi, ImmutableMap<String,TypedValue> values) throws Exception {
		/*return evaluate(multi.getLeft(),values)
				.multiply(evaluate(multi.getRight(),values));*/
		TypedValue left = evaluate(multi.getLeft(),values);
		TypedValue right = evaluate(multi.getRight(),values);
		return inter.exec(new Term(left, right, Tag.MUL));
	}
	
}
