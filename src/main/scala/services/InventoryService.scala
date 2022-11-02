package io.simple.inventory
package services

import api.AddItemRequest
import models.InventoryItem
import repositories.InventoryRepo

import cats.effect.MonadCancelThrow
import cats.implicits._
import doobie._
import doobie.implicits._

import java.util.UUID

final case class InventoryService[F[_]: MonadCancelThrow](xa: Transactor[F],
                                                          repo: InventoryRepo,
                                                          itemTypeService: ItemTypeService[F]) {
  def list() =
    repo.list.transact(xa)

  def find(id: String) =
    repo.find(id).transact(xa)

  def insert(addItemRequest: AddItemRequest) =
    itemTypeService.findBy(addItemRequest.itemTypeCode)
      .flatMap { x =>
        repo.insert(
          InventoryItem(
            id = UUID.randomUUID().toString,
            title = addItemRequest.title,
            description = addItemRequest.description,
            itemTypeCode = x.get.code
          )
        ).transact(xa)
      }

  def delete(id: String) =
    repo.delete(id).transact(xa)
}
