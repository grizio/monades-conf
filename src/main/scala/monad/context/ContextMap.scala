package monad.context
import scala.concurrent.Future

class ContextMap[A, B](inner: Context[A], op: A => B) extends Context[B] {
  override def execute(parameters: ContextParameters): Future[B] = {
    val innerResult = inner.execute(parameters)
    val result = innerResult.map(op)
    result
  }
}