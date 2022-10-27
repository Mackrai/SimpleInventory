package io.simple.inventory
package api

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class ErrorResponse(status: String,
                               message: String)

object ErrorResponse {
  implicit val decoder: Decoder[ErrorResponse] = deriveDecoder
  implicit val encoder: Encoder[ErrorResponse] = deriveEncoder
  implicit val schema: Schema[ErrorResponse] = Schema.derived
}