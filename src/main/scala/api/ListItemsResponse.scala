package io.simple.inventory
package api

import models.InventoryItem

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema

final case class ListItemsResponse(items: List[InventoryItem])

object ListItemsResponse {
  implicit val decoder: Decoder[ListItemsResponse] = deriveDecoder
  implicit val encoder: Encoder[ListItemsResponse] = deriveEncoder
  implicit val schema: Schema[ListItemsResponse] = Schema.derived
}

