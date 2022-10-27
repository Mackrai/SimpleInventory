package io.simple.inventory
package repositories

import models.InventoryItem

import cats.implicits._
import doobie.ConnectionIO
import doobie.implicits._

final class InventoryRepo extends Repository {
  override val defaultTable: String = "InventoryItem"

  def list: doobie.ConnectionIO[Either[Throwable, List[InventoryItem]]] =
    sql"""select * from "InventoryItem"""".query[InventoryItem].to[List].attempt

  def find(id: String): doobie.ConnectionIO[Either[Throwable, Option[InventoryItem]]] =
    sql"""select * from "InventoryItem" where id = $id""".query[InventoryItem].option.attempt

  def insert(item: InventoryItem): ConnectionIO[Either[Throwable, InventoryItem]] =
    sql"""
      insert into "InventoryItem" (id, title, description, "itemType")
      values (${item.id}, ${item.title}, ${item.description}, ${item.itemType})
    """.update.withUniqueGeneratedKeys[InventoryItem]("id", "title", "description", "itemType").attempt

  def delete(id: String): doobie.ConnectionIO[Either[Throwable, Int]] =
    sql"""delete from "InventoryItem" where id = $id""".update.run.attempt
}
