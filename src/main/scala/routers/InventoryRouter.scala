package io.simple.inventory
package routers

import api.{AddItemRequest, ErrorResponse, ListItemsResponse}
import models.InventoryItem
import services.InventoryService

import cats.effect.kernel.Async
import cats.implicits._
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ServerEndpoint.Full

final class InventoryRouter[F[_]: Async](inventoryService: InventoryService[F]) extends Router[F] {
  private val base = api.in("items")

  private val listEndpoint: Endpoint[Unit, Unit, ErrorResponse, ListItemsResponse, Any] =
    base.get
      .in("list")
      .out(jsonBody[ListItemsResponse])

  private val findItemEndpoint: Endpoint[Unit, String, ErrorResponse, Option[InventoryItem], Any] =
    base.get
      .in("item")
      .in(query[String]("id"))
      .out(jsonBody[Option[InventoryItem]])

  private val addItemEndpoint: Endpoint[Unit, AddItemRequest, ErrorResponse, InventoryItem, Any] =
    base.post
      .in("add")
      .in(jsonBody[AddItemRequest])
      .out(jsonBody[InventoryItem])

  private val deleteItemEndpoint: Endpoint[Unit, String, ErrorResponse, String, Any] =
    base.delete
      .in("item")
      .in(query[String]("id"))
      .out(stringBody)

  private val list: Full[Unit, Unit, Unit, ErrorResponse, ListItemsResponse, Any, F] =
    listEndpoint.serverLogic { _ =>
      inventoryService.list()
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
        .map(_.map(ListItemsResponse.apply))
    }

  private val find: Full[Unit, Unit, String, ErrorResponse, Option[InventoryItem], Any, F] =
    findItemEndpoint.serverLogic { itemId =>
      inventoryService.find(itemId)
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
    }

  private val add: Full[Unit, Unit, AddItemRequest, ErrorResponse, InventoryItem, Any, F] =
    addItemEndpoint.serverLogic { request =>
      inventoryService.insert(request)
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
    }

  private val delete: Full[Unit, Unit, String, ErrorResponse, String, Any, F] =
    deleteItemEndpoint.serverLogic { itemId =>
      inventoryService.delete(itemId)
        .map(_.leftMap(e => ErrorResponse("500", e.getMessage)))
        .map(_.map(_.toString))
    }

  override val endpoints: List[ServerEndpoint[Any, F]] =
    List(list, find, add, delete)
}
