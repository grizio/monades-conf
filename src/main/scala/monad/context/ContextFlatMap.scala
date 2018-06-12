package monad.context

import scala.concurrent.Future

class ContextFlatMap[A, B](inner: Context[A], op: A => Context[B]) extends Context[B] {
  override def execute(parameters: ContextParameters): Future[B] = {
    val innerResult = inner.execute(parameters)
    innerResult.flatMap { innerResult =>
      val nextContext = op(innerResult)
      val nextResult = nextContext.execute(parameters)
      nextResult
    }
  }
}