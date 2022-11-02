package io.simple.inventory
package models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import sttp.tapir.Schema

final case class InventoryItem(id: String,
                               title: String,
                               description: Option[String],
                               itemTypeCode: String)

object InventoryItem {
  implicit val decoder: Decoder[InventoryItem] = deriveDecoder
  implicit val encoder: Encoder[InventoryItem] = deriveEncoder
  implicit val schema: Schema[InventoryItem] = Schema.derived
}
