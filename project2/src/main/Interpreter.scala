package edu.colorado.csci3155.project2

import scala.math.sqrt

/* A Lettuce interpreter with evalExpr function
 * that has missing cases to be handled.
 */

object Interpreter {
    def binaryExprEval(e1: Expr, e2: Expr, env: Environment)(f: (Value, Value) => Value): Value = {
        val v1 = evalExpr(e1, env)
        val v2 = evalExpr(e2, env)
        f(v1, v2)
    }

    def evalExpr(e: Expr, env: Environment): Value = e match {
        case Const(d) => NumValue(d)
        case ConstBool(b) => BoolValue(b)
        case Ident(s) => env.lookup(s)
        case Line(l) => {
            val v = evalExpr(l, env)
            v match {
                case NumValue(n) => {
                    FigValue(
                        new MyCanvas(List(
                            new Polygon(List(
                                (0.0,0.0), (n,0.0)
                            ))
                        ))
                    )
                }
                case _ => throw new IllegalArgumentException("Line side length is not a number.")
            }
        }
        case EquiTriangle(sideLength) => {
            val v = evalExpr(sideLength, env)
            v match {
                case NumValue(n) => {
                    FigValue(
                        new MyCanvas(List(
                            new Polygon(List(
                                (0.0,0.0), (n,0.0), (n/2.0,n*sqrt(3.0)/2.0)
                            ))
                        ))
                    )
                }
                case _ => throw new IllegalArgumentException("EquiTriangle side length is not a number.")
            }
        }
        case Rectangle(sideLength) => {
            val v = evalExpr(sideLength, env)
            v match {
                case NumValue(n) => {
                    FigValue(
                        new MyCanvas(List(
                            new Polygon(List(
                                (0.0,0.0), (0.0,n), (n,n), (n,0.0)
                            ))
                        ))
                    )
                }
                case _ => throw new IllegalArgumentException("Rectangle side length is not a number.")
            }
        }
        case Circle(rad) => {
            val v = evalExpr(rad, env)
            v match {
                case NumValue(n) => {
                    FigValue(
                        new MyCanvas(List(
                            new MyCircle((n,n), n)
                        ))
                    )
                }
                case _ => throw new IllegalArgumentException("Circle radius is not a number.")
            }
        }
        case Plus(e1, e2) => {
            val v1 = evalExpr(e1, env)
            val v2 = evalExpr(e2, env)
            (v1,v2) match {
                case (NumValue(n1),NumValue(n2)) => NumValue(n1 + n2)
                case (FigValue(f1),FigValue(f2)) => FigValue(f1.overlap(f2))
                case _ => throw new IllegalArgumentException("Plus trying to add with not both being figures or numbers.")
            }
        }
        case Minus(e1, e2) => binaryExprEval(e1, e2, env) (ValueOps.minus)
        case Mult(e1, e2) => {
            val v1 = evalExpr(e1, env)
            val v2 = evalExpr(e2, env)
            (v1,v2) match {
                case (NumValue(n1),NumValue(n2)) => NumValue(n1 * n2)
                case (FigValue(f1),FigValue(f2)) => FigValue(f1.placeRight(f2))
                case _ => throw new IllegalArgumentException("Mult trying to multiply with not both being figures or numbers.")
            }
        }
        case Div(e1, e2) => {
            val v1 = evalExpr(e1, env)
            val v2 = evalExpr(e2, env)
            (v1,v2) match {
                case (NumValue(n1),NumValue(n2)) => {
                    if (n2 == 0) throw new IllegalArgumentException("Div try to divide by zero")
                    NumValue(n1/n2)
                }
                case (FigValue(f1),FigValue(f2)) => FigValue(f2.placeTop(f1))
                case (FigValue(f1),NumValue(n2)) => FigValue(f1.rotate(n2))
                case _ => throw new IllegalArgumentException("Div trying to divide with not number or figures.")
            }
        }
        case Geq(e1, e2) => binaryExprEval(e1, e2, env) (ValueOps.geq)
        case Gt(e1, e2) => binaryExprEval(e1, e2, env) (ValueOps.gt)
        case Eq(e1, e2) => binaryExprEval(e1, e2, env) (ValueOps.equal)
        case Neq(e1, e2) => binaryExprEval(e1, e2, env) (ValueOps.notEqual)
        case And(e1, e2) => {
            val v1 = evalExpr(e1, env)
            v1 match {
                case BoolValue(true) => {
                    val v2 = evalExpr(e2, env)
                    v2 match {
                        case BoolValue(_) => v2
                        case _ => throw new IllegalArgumentException("And applied to a non-Boolean value")
                    }
                }
                case BoolValue(false) => BoolValue(false)
                case _ => throw new IllegalArgumentException("And applied to a non-boolean value")
            }
        }
        case Or(e1, e2) => {
            val v1 = evalExpr(e1, env)
            v1 match {
                case BoolValue(true) => BoolValue(true)
                case BoolValue(false) => {
                    val v2 = evalExpr(e2, env)
                    v2 match {
                        case BoolValue(_) => v2
                        case _ => throw new IllegalArgumentException("Or Applied to a non-Boolean value")
                    }
                }
                case _ => throw new IllegalArgumentException("Or Applied to a non-Boolean Value")
            }
        }
        case Not(e) => {
            val v = evalExpr(e, env)
            v match {
                case BoolValue(b) => BoolValue(!b)
                case _ => throw new IllegalArgumentException("Not applied to a non-Boolean Value")
            }
        }
        case IfThenElse(e, e1, e2) => {
            val v = evalExpr(e, env)
            v match {
                case BoolValue(true) => evalExpr(e1, env)
                case BoolValue(false) => evalExpr(e2,env)
                case _ => throw new IllegalArgumentException("If then else condition is not a Boolean value")
            }
        }
        case Let(x, e1, e2) => {
            val v1 = evalExpr(e1, env)
            val env2 = Extend(x, v1, env)
            evalExpr(e2, env2)
        }
        case FunDef(x, e) => Closure(x, e, env)
        case LetRec(f, x, e1, e2) => {
            val envp = ExtendREC(f, x, e1, env)
            evalExpr(e2,envp)
        }
        case FunCall(fCallExpr, arg) => evalExpr(fCallExpr, env) match {
            case Closure(x, e, envOld) => evalExpr(e, Extend(x, evalExpr(arg, env), envOld))
            case v => throw new IllegalArgumentException(s"Expected Closure, found $v")
        }
    }

    def evalProgram(p: Program): Value = p match {
        case TopLevel(e) => evalExpr(e, EmptyEnvironment)
    }
}
