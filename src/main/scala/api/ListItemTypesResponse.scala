package io.simple.inventory
package api

import models.ItemType

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

final case class ListItemTypesResponse(items: List[ItemType])

object ListItemTypesResponse {
  implicit val decoder: Decoder[ListItemTypesResponse] = deriveDecoder
  implicit val encoder: Encoder[ListItemTypesResponse] = deriveEncoder
  implicit val schema: Schema[ListItemTypesResponse] = Schema.derived
}

