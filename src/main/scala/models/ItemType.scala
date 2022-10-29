package io.simple.inventory
package models

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class ItemType(id: String,
                          code: String,
                          title: String,
                          description: Option[String])

object ItemType {
  implicit val decoder: Decoder[ItemType] = deriveDecoder
  implicit val encoder: Encoder[ItemType] = deriveEncoder
  implicit val schema: Schema[ItemType] = Schema.derived
}
