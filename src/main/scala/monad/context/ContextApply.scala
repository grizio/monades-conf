package monad.context

import scala.concurrent.Future

class ContextApply[A](op: ContextParameters => Future[A]) extends Context[A] {
  override def execute(parameters: ContextParameters): Future[A] = {
    op(parameters)
  }
}