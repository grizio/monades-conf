package monad.context

import scala.concurrent.Future

trait Context[A] {
  def execute(parameters: ContextParameters): Future[A]

  def map[B](op: A => B): Context[B] = {
    new ContextMap(this, op)
  }

  def flatMap[B](op: A => Context[B]): Context[B] = {
    new ContextFlatMap(this, op)
  }
}

object Context {
  def apply[A](op: ContextParameters => Future[A]): Context[A] = {
    new ContextApply(op)
  }

  def pure[A](value: A): Context[A] = {
    new ContextPure(value)
  }
}