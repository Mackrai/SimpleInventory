package io.simple.inventory
package repositories

import models.ItemType

import cats.implicits._
import doobie.ConnectionIO
import doobie.implicits._

final class ItemTypeRepo extends Repository {
  override val defaultTable: String = "ItemType"

  def list: ConnectionIO[Either[Throwable, List[ItemType]]] =
    sql"""select * from "ItemType"""".query[ItemType].to[List].attempt

  def find(id: String): ConnectionIO[Either[Throwable, Option[ItemType]]] =
    sql"""select * from "ItemType" where id = $id""".query[ItemType].option.attempt

  def findByCode(code: String) =
    sql"""select * from "ItemType" where code = $code""".query[ItemType].option

  def insert(itemType: ItemType): ConnectionIO[Either[Throwable, ItemType]] =
    sql"""
      insert into "ItemType" (id, code, title, description)
      values (${itemType.id}, ${itemType.code}, ${itemType.title}, ${itemType.description})
    """.update.withUniqueGeneratedKeys[ItemType]("id", "code", "title", "description").attempt

  def delete(id: String): ConnectionIO[Either[Throwable, Int]] =
    sql"""delete from "ItemType" where id = $id""".update.run.attempt
}
