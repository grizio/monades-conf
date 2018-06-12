package monad.common

import scala.concurrent.Future

trait Request {
  def extractLanguage: Language

  def bodyAs[A]: A
}

trait Result

trait Action

object Action {
  def apply(op: Request => Future[Any]): Action = ???

  def withLanguage(op: Request => Language => Future[Any]): Action = ???
}