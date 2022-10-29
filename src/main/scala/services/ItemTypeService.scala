package io.simple.inventory
package services

import api.AddItemTypeRequest
import models.ItemType
import repositories.ItemTypeRepo

import cats.effect.MonadCancelThrow
import doobie._
import doobie.implicits._

import java.util.UUID

final case class ItemTypeService[F[_]: MonadCancelThrow](xa: Transactor[F],
                                                         repo: ItemTypeRepo) {
  def list(): F[Either[Throwable, List[ItemType]]] = repo.list.transact(xa)

  def find(id: String): F[Either[Throwable, Option[ItemType]]] = repo.find(id).transact(xa)

  def insert(dto: AddItemTypeRequest): F[Either[Throwable, ItemType]] =
    repo.insert(
      ItemType(
        id = UUID.randomUUID().toString,
        code = dto.code,
        title = dto.title,
        description = dto.description
      )
    ).transact(xa)

  def delete(id: String): F[Either[Throwable, Int]] =
    repo.delete(id).transact(xa)
}
