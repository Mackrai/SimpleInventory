package io.simple.inventory
package routers

import api.ErrorResponse

import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ServerEndpoint

trait Router[F[_]] {
  protected val api = endpoint.in("api").errorOut(jsonBody[ErrorResponse])

  val endpoints: List[ServerEndpoint[Any, F]]
}
