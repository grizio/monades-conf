package monad.context

import monad.common._

class API {
  def post[I, O](path: String, input: I): Context[O] = Context { parameters =>
    val fullPath = s"${parameters.hostname}${path}"
    val acceptLanguage = s"Accept-Language: ${parameters.language}"
    // Call some lib
    ???
  }
}

class OrderService(api: API) {
  def createOrder(order: Order): Context[Receipt] = {
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
      .execute(ContextParameters(
        hostname = configuration.get("hostname"),
        language = request.extractLanguage
      ))
  }
}