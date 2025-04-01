package com.craftinginterpreters.lox;

class AstPrinter implements Expr.Visitor<String> {

  String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.operator.lexeme, expr.left, expr.right);
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);
  }

  @Override
  public String visitTernaryExpr(Expr.Ternary expr) {
    return parenthesize("?:", expr.condition, expr.thenBranch, expr.elseBranch);
  }

  private String parenthesize(String name, Expr... exprs) {
    StringBuilder builder = new StringBuilder();

    builder.append("(").append(name);
    for (Expr expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");

    return builder.toString();
  }

  public static void main(String[] args) {
    // Expr expression = new Expr.Binary(
    //   new Expr.Unary(
    //     new Token(TokenType.MINUS, "-", null, 1),
    //     new Expr.Literal(123)
    //   ),
    //   new Token(TokenType.STAR, "*", null, 1),
    //   new Expr.Grouping(new Expr.Literal(45.67))
    // );

    // comma operator
    // Expr expression = new Expr.Binary(
    //   new Expr.Binary(
    //     new Expr.Literal(1),
    //     new Token(TokenType.COMMA, ",", null, 1),
    //     new Expr.Literal(2)
    //   ),
    //   new Token(TokenType.COMMA, ",", null, 1),
    //   new Expr.Literal(3)
    // );

    // ternary operator
    // Expr expression = new Expr.Ternary(
    //   new Expr.Literal(true),
    //   new Expr.Literal(1),
    //   new Expr.Ternary(
    //     new Expr.Literal(false),
    //     new Expr.Literal(2),
    //     new Expr.Literal(3)
    //   )
    // );

    // ternary precidence is just higher than assignment

    // associativity: right associative

    // comma no right operand
    Expr expression = new Expr.Binary(
      new Expr.Binary(
        new Expr.Literal(1),
        new Token(TokenType.COMMA, ",", null, 1),
        new Expr.Literal(2)
      ),
      new Token(TokenType.COMMA, ",", null, 1),
      new Expr.Literal(3)
    );

    System.out.println(new AstPrinter().print(expression));
  }
}
