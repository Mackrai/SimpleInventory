package io.simple.inventory
package routers

import api.{AddItemTypeRequest, ErrorResponse, ListItemTypesResponse}
import models.ItemType
import services.ItemTypeService

import cats.effect.kernel.Async
import cats.implicits._
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ServerEndpoint.Full

final class ItemTypeRouter[F[_]: Async](itemTypeService: ItemTypeService[F]) extends Router[F] {
  private val base = api.in("itemType")

  private val listEndpoint: Endpoint[Unit, Unit, ErrorResponse, ListItemTypesResponse, Any] =
    base.get
      .in("list")
      .out(jsonBody[ListItemTypesResponse])

  private val findEndpoint: Endpoint[Unit, String, ErrorResponse, Option[ItemType], Any] =
    base.get
      .in("item")
      .in(query[String]("id"))
      .out(jsonBody[Option[ItemType]])

  private val addEndpoint: Endpoint[Unit, AddItemTypeRequest, ErrorResponse, ItemType, Any] =
    base.post
      .in("add")
      .in(jsonBody[AddItemTypeRequest])
      .out(jsonBody[ItemType])

  private val deleteEndpoint: Endpoint[Unit, String, ErrorResponse, String, Any] =
    base.delete
      .in("item")
      .in(query[String]("id"))
      .out(stringBody)

  private val list: Full[Unit, Unit, Unit, ErrorResponse, ListItemTypesResponse, Any, F] =
    listEndpoint.serverLogic { _ =>
      itemTypeService.list()
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
        .map(_.map(ListItemTypesResponse.apply))
    }

  private val find: Full[Unit, Unit, String, ErrorResponse, Option[ItemType], Any, F] =
    findEndpoint.serverLogic { itemId =>
      itemTypeService.find(itemId)
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
    }

  private val add: Full[Unit, Unit, AddItemTypeRequest, ErrorResponse, ItemType, Any, F] =
    addEndpoint.serverLogic { request =>
      itemTypeService.insert(request)
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
    }

  private val delete: Full[Unit, Unit, String, ErrorResponse, String, Any, F] =
    deleteEndpoint.serverLogic { itemId =>
      itemTypeService.delete(itemId)
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
        .map(_.map(_.toString))
    }

  override val endpoints: List[ServerEndpoint[Any, F]] =
    List(list, find, add, delete)
}
