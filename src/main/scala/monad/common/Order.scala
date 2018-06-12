package monad.common

trait Order {
  def id: OrderId
}
trait OrderId
trait Receipt