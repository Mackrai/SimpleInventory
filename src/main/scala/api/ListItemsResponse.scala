package io.simple.inventory
package api

import models.{InventoryItem, ItemType}

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class ListItemsItemResponse(item: InventoryItem,
                                       itemType: ItemType)

object ListItemsItemResponse {
  implicit val decoder: Decoder[ListItemsItemResponse] = deriveDecoder
  implicit val encoder: Encoder[ListItemsItemResponse] = deriveEncoder
  implicit val schema: Schema[ListItemsItemResponse] = Schema.derived
}

final case class ListItemsResponse(items: List[ListItemsItemResponse])

object ListItemsResponse {
  implicit val decoder: Decoder[ListItemsResponse] = deriveDecoder
  implicit val encoder: Encoder[ListItemsResponse] = deriveEncoder
  implicit val schema: Schema[ListItemsResponse] = Schema.derived
}

