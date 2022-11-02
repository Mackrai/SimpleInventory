package io.simple.inventory
package api

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema

case class AddItemRequest(title: String,
                          description: Option[String],
                          itemTypeCode: String)

object AddItemRequest {
  implicit val decoder: Decoder[AddItemRequest] = deriveDecoder
  implicit val encoder: Encoder[AddItemRequest] = deriveEncoder
  implicit val schema: Schema[AddItemRequest] = Schema.derived
}
