package monad.context
import scala.concurrent.Future

class ContextPure[A](value: A) extends Context[A] {
  override def execute(parameters: ContextParameters): Future[A] = {
    Future.successful(value)
  }
}