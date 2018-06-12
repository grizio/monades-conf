package monad.initial

import monad.common._

import scala.concurrent.Future

class API {
  private val hostname: String = "http://localhost"
  private val language: Language = ???

  def post[I, O](path: String, input: I): Future[O] = {
    val fullPath = s"${hostname}${path}"
    val acceptLanguage = s"Accept-Language: ${language}"
    // Call some lib
    ???
  }
}

class OrderService(api: API) {
  def createOrder(order: Order): Future[Receipt] = {
    for {
      createdOrder <- api.post[Order, Order]("/orders", order)
      createdOrderId = createdOrder.id
      receipt <- api.post("/orders/receipt", createdOrderId)
    } yield receipt
  }
}

class OrderController(configuration: Configuration, orderService: OrderService) {
  def createOrder = Action { request =>
    orderService
      .createOrder(request.bodyAs[Order])
      .map(Views.orderCreated)
  }
}