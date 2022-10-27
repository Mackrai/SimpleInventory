package io.simple.inventory
package services

import api.AddItemRequest
import models.InventoryItem
import repositories.InventoryRepo

import cats.effect.MonadCancelThrow
import doobie._
import doobie.implicits._

import java.util.UUID

final case class InventoryService[F[_]: MonadCancelThrow](xa: Transactor[F],
                                                          repo: InventoryRepo) {
  def list(): F[Either[Throwable, List[InventoryItem]]] = repo.list.transact(xa)

  def find(id: String): F[Either[Throwable, Option[InventoryItem]]] = repo.find(id).transact(xa)

  def insert(addItemRequest: AddItemRequest): F[Either[Throwable, InventoryItem]] =
    repo.insert(
      InventoryItem(
        id = UUID.randomUUID().toString,
        title = addItemRequest.title,
        description = addItemRequest.description,
        itemType = addItemRequest.itemType
      )
    ).transact(xa)

  def delete(id: String): F[Either[Throwable, Int]] =
    repo.delete(id).transact(xa)
}
