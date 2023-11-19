file://<WORKSPACE>/src/main/scala/edu/colorado/csci3155/project2/Value.scala
### java.lang.AssertionError: assertion failed

occurred in the presentation compiler.

action parameters:
uri: file://<WORKSPACE>/src/main/scala/edu/colorado/csci3155/project2/Value.scala
text:
```scala
package edu.colorado.csci3155.project2

import scala.util.Failure
import scala.math.{min,

/* Define the values used in our interpreter */
sealed trait Value
case class NumValue(f: Double) extends Value
case class FigValue(c: MyCanvas) extends Value
case class Closure(x: String, e: Expr, env: Environment) extends Value
case class BoolValue(b: Boolean) extends Value

/* Create some utility functions to operate on values.  */
object ValueOps {
    def minus(v1: Value, v2: Value): Value = (v1, v2) match {
        case (NumValue(f1), NumValue(f2)) => NumValue(f1 - f2)
        case _ => throw new IllegalArgumentException("Cannot subtract figures, numbers and closures")
    }
    def div(v1: Value, v2: Value): Value = (v1, v2) match {
        case (NumValue(f1),NumValue(f2)) if f2 != 0 => NumValue(f1/f2)
        case (NumValue(_), NumValue(_)) => throw new IllegalArgumentException("Div try to divide by zero")
        case _ => throw new IllegalArgumentException("Div applied when at least one is not a NumValue")
    }
    def geq(v1: Value, v2: Value): Value = (v1, v2) match {
        case (NumValue(f1), NumValue(f2)) => BoolValue(f1 >= f2)
        case _ => throw new IllegalArgumentException("Cannot compare non numeric expressions with the geq comparator")
    }
    def gt(v1: Value, v2: Value): Value = (v1, v2) match {
        case (NumValue(f1), NumValue(f2)) => BoolValue(f1 > f2)
        case _ => throw new IllegalArgumentException("Cannot compare non numeric expressions with the eq comparator")
    }
    def equal(v1: Value, v2: Value): Value = (v1, v2) match {
        case (NumValue(f1), NumValue(f2)) => BoolValue(f1 == f2)
        case (BoolValue(b1), BoolValue(b2)) => BoolValue(b1 == b2)
        case _ =>  throw new IllegalArgumentException("Cannot compare non numeric/boolean expressions with the eq comparator")
    }
    def notEqual(v1: Value, v2: Value): Value = {
        val v = equal(v1, v2)
        v match {
            case BoolValue(b) => BoolValue(!b)
            case _ => throw new IllegalArgumentException("Internal error: something is really wrong") // This should never happen
        }
    }
}

```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:11)
	dotty.tools.dotc.core.Annotations$LazyAnnotation.tree(Annotations.scala:116)
	dotty.tools.dotc.core.Annotations$Annotation$Child$.unapply(Annotations.scala:226)
	dotty.tools.dotc.typer.Namer.insertInto$1(Namer.scala:477)
	dotty.tools.dotc.typer.Namer.addChild(Namer.scala:488)
	dotty.tools.dotc.typer.Namer$Completer.register$1(Namer.scala:899)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild$$anonfun$1(Namer.scala:908)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild(Namer.scala:908)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:811)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:173)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:187)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:189)
	dotty.tools.dotc.core.Types$NamedType.info(Types.scala:2299)
	dotty.tools.dotc.core.Types$TermLambda.dotty$tools$dotc$core$Types$TermLambda$$_$compute$1(Types.scala:3764)
	dotty.tools.dotc.core.Types$TermLambda.foldArgs$2(Types.scala:3771)
	dotty.tools.dotc.core.Types$TermLambda.dotty$tools$dotc$core$Types$TermLambda$$_$compute$1(Types.scala:4361)
	dotty.tools.dotc.core.Types$TermLambda.dotty$tools$dotc$core$Types$TermLambda$$depStatus(Types.scala:3791)
	dotty.tools.dotc.core.Types$TermLambda.dependencyStatus(Types.scala:3805)
	dotty.tools.dotc.core.Types$TermLambda.isResultDependent(Types.scala:3827)
	dotty.tools.dotc.core.Types$TermLambda.isResultDependent$(Types.scala:3721)
	dotty.tools.dotc.core.Types$MethodType.isResultDependent(Types.scala:3865)
	dotty.tools.dotc.typer.TypeAssigner.safeSubstMethodParams(TypeAssigner.scala:293)
	dotty.tools.dotc.typer.TypeAssigner.safeSubstMethodParams$(TypeAssigner.scala:16)
	dotty.tools.dotc.typer.Typer.safeSubstMethodParams(Typer.scala:121)
	dotty.tools.dotc.typer.TypeAssigner.assignType(TypeAssigner.scala:301)
	dotty.tools.dotc.typer.TypeAssigner.assignType$(TypeAssigner.scala:16)
	dotty.tools.dotc.typer.Typer.assignType(Typer.scala:121)
	dotty.tools.dotc.ast.tpd$.Apply(tpd.scala:49)
	dotty.tools.dotc.core.tasty.TreeUnpickler.dotty$tools$dotc$core$tasty$TreeUnpickler$TreeReader$$_$constructorApply$1(TreeUnpickler.scala:1198)
	dotty.tools.dotc.core.tasty.TreeUnpickler$TreeReader.readLengthTerm$1(TreeUnpickler.scala:1225)
	dotty.tools.dotc.core.tasty.TreeUnpickler$TreeReader.readTerm(TreeUnpickler.scala:1384)
	dotty.tools.dotc.core.tasty.TreeUnpickler.$anonfun$15$$anonfun$1(TreeUnpickler.scala:738)
	dotty.tools.dotc.core.tasty.TreeUnpickler$LazyReader.complete(TreeUnpickler.scala:1521)
	dotty.tools.dotc.core.tasty.TreeUnpickler.readAnnot$$anonfun$1$$anonfun$2(TreeUnpickler.scala:740)
	dotty.tools.dotc.core.Annotations$LazyAnnotation.tree(Annotations.scala:120)
	dotty.tools.dotc.core.Annotations$Annotation$Child$.unapply(Annotations.scala:226)
	dotty.tools.dotc.typer.Namer.insertInto$1(Namer.scala:477)
	dotty.tools.dotc.typer.Namer.addChild(Namer.scala:488)
	dotty.tools.dotc.typer.Namer$Completer.register$1(Namer.scala:899)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild$$anonfun$1(Namer.scala:908)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild(Namer.scala:908)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:811)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:173)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:187)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:189)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:380)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:2861)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:2886)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2982)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3050)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3054)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3076)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3126)
	dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:2687)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:2953)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2983)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3050)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3054)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3170)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$1(TyperPhase.scala:44)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$adapted$1(TyperPhase.scala:54)
	scala.Function0.apply$mcV$sp(Function0.scala:42)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:429)
	dotty.tools.dotc.typer.TyperPhase.typeCheck(TyperPhase.scala:54)
	dotty.tools.dotc.typer.TyperPhase.runOn$$anonfun$3(TyperPhase.scala:88)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.typer.TyperPhase.runOn(TyperPhase.scala:88)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:233)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1321)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:244)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:252)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:261)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:68)
	dotty.tools.dotc.Run.compileUnits(Run.scala:261)
	dotty.tools.dotc.Run.compileSources(Run.scala:185)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:165)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:42)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:81)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:99)
```
#### Short summary: 

java.lang.AssertionError: assertion failed