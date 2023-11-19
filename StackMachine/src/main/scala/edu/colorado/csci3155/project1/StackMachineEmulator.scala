package edu.colorado.csci3155.project1
import scala.math.{exp,log,cos,sin}

sealed trait StackMachineInstruction
case object AddI extends StackMachineInstruction
case object SubI extends StackMachineInstruction
case object MultI extends StackMachineInstruction
case object DivI extends StackMachineInstruction
case object ExpI extends StackMachineInstruction
case object LogI extends StackMachineInstruction
case object SinI extends StackMachineInstruction
case object CosI extends StackMachineInstruction
case class PushI(f: Double) extends StackMachineInstruction
case object PopI extends StackMachineInstruction

case class DivideByZero() extends Exception
case class LogOfNegativeNum() extends Exception
case class StackSize() extends Exception

object StackMachineEmulator {
    /* Function emulateSingleInstruction
        Given a list of doubles to represent a stack and a single instruction of type StackMachineInstruction
        Return a stack that results when the instruction is executed from the stack.
        Make sure you handle the error cases: eg., stack size must be appropriate for the instruction
        being executed. Division by zero, log of a non negative number
        Throw an exception or assertion violation when error happens.
     */
    def emulateSingleInstruction(stack: List[Double], ins: StackMachineInstruction): List[Double] = {
        // Returns (stack[0:-n], stack[-n:]) if stack.size >= n. Otherwise, throw an exception
        def get_n_from_stack(stack: List[Double], n: Int): (List[Double], List[Double]) = {
            if (stack.size < n) throw new StackSize
            (stack.take(stack.size - n), stack.takeRight(n))
        }

        // Perform op in the last n elements of stack
        def op_in_n(stack: List[Double], n: Int, op: List[Double] => Double): List[Double] = {
            val ret = get_n_from_stack(stack,n)
            ret._1 :+ op(ret._2)
        }

        ins match {
            case AddI  => op_in_n(stack, 2, (l: List[Double]) => l.head + l.last)
            case SubI  => op_in_n(stack, 2, (l: List[Double]) => l.head - l.last)
            case MultI => op_in_n(stack, 2, (l: List[Double]) => l.head * l.last)
            case DivI  => op_in_n(stack, 2, (l: List[Double]) => if (l.last == 0.0) throw new DivideByZero else l.head / l.last)
            case ExpI  => op_in_n(stack, 1, (l: List[Double]) => exp(l.head))
            case LogI  => op_in_n(stack, 1, (l: List[Double]) => if (l.head < 0.0) throw new LogOfNegativeNum else log(l.head))
            case SinI  => op_in_n(stack, 1, (l: List[Double]) => sin(l.head))
            case CosI  => op_in_n(stack, 1, (l: List[Double]) => cos(l.head))
            case PushI(f) => stack :+ f
            case PopI  => get_n_from_stack(stack,1)._1
        }
    }

    /* Function emulateStackMachine
       Execute the list of instructions provided as inputs using the
       emulateSingleInstruction function.
       Use foldLeft over list of instruction rather than a for loop if you can.
       Return value must be a double that is the top of the stack after all instructions
       are executed.
     */
    def emulateStackMachine(instructionList: List[StackMachineInstruction]): Double = {
        instructionList.foldLeft(List[Double]())((stack,ins) => emulateSingleInstruction(stack, ins)).last
    }
}
