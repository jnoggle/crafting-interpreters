package com.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements LoxCallable {

  private final Stmt.Function declaration;
  private final Environment closure;
  private final Expr.Lambda lambdaDeclaration;

  LoxFunction(Stmt.Function declaration, Environment closure) {
    this.closure = closure;
    this.declaration = declaration;
    this.lambdaDeclaration = null;
  }

  LoxFunction(Expr.Lambda lambdaDeclaration, Environment closure) {
    this.closure = closure;
    this.declaration = null;
    this.lambdaDeclaration = lambdaDeclaration;
  }

  @Override
  public int arity() {
    if (declaration != null) {
      return declaration.params.size();
    } else if (lambdaDeclaration != null) {
      return lambdaDeclaration.params.size();
    }
    return 0;
  }

  @Override
  public String toString() {
    if (declaration != null) {
      return "<fn " + declaration.name.lexeme + ">";
    }
    return "<anonymous fn>";
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    Environment environment = new Environment(closure);

    if (declaration != null) {
      for (int i = 0; i < declaration.params.size(); i++) {
        environment.define(declaration.params.get(i).lexeme, arguments.get(i));
      }

      try {
        interpreter.executeBlock(declaration.body, environment);
      } catch (Return returnValue) {
        return returnValue.value;
      }
    } else if (lambdaDeclaration != null) {
      for (int i = 0; i < lambdaDeclaration.params.size(); i++) {
        environment.define(
          lambdaDeclaration.params.get(i).lexeme,
          arguments.get(i)
        );
      }

      try {
        interpreter.executeBlock(lambdaDeclaration.body, environment);
      } catch (Return returnValue) {
        return returnValue.value;
      }
    }
    return null;
  }
}
