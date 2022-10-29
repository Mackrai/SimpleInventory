package io.simple.inventory
package api

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

case class AddItemTypeRequest(code: String,
                              title: String,
                              description: Option[String])

object AddItemTypeRequest {
  implicit val decoder: Decoder[AddItemTypeRequest] = deriveDecoder
  implicit val encoder: Encoder[AddItemTypeRequest] = deriveEncoder
  implicit val schema: Schema[AddItemTypeRequest] = Schema.derived
}
