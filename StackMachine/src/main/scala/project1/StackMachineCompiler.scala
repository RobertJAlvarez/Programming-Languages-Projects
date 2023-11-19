package project1

object StackMachineCompiler {
    /* Function compileToStackMachineCode
        Given expression e as input, return a corresponding list of stack machine instructions.
        The type of stackmachine instructions are in the file StackMachineEmulator.scala in this same directory
        The type of Expr is in the file Expr.scala in this directory.
     */
    def compileToStackMachineCode(e: Expr): List[StackMachineInstruction] = {
        def add_to_stack(exprs: List[Expr], inst: StackMachineInstruction): List[StackMachineInstruction] = {
            exprs.foldLeft(List[StackMachineInstruction]())((ls,expr) => ls ++ compileToStackMachineCode(expr)) ++ List(inst)
        }

        e match {
            case Const(f) => List[StackMachineInstruction](PushI(f))
            case Plus(e1, e2) => add_to_stack(List(e1, e2), AddI)
            case Minus(e1, e2) => add_to_stack(List(e1, e2), SubI)
            case Mult(e1, e2) => add_to_stack(List(e1, e2), MultI)
            case Div(e1, e2) => add_to_stack(List(e1, e2), DivI)
            case Exp(e) => add_to_stack(List(e), ExpI)
            case Log(e) => add_to_stack(List(e), LogI)
            case Sine(e) => add_to_stack(List(e), SinI)
            case Cosine(e) => add_to_stack(List(e), CosI)
        }
    }
}
