package io.simple.inventory
package repositories

import models.InventoryItem

import cats.implicits._
import doobie.ConnectionIO
import doobie.implicits._

final class InventoryRepo extends Repository {
  override val defaultTable: String = "InventoryItem"

  def list =
    sql"""select * from "InventoryItem"""".query[InventoryItem].to[List]

  def find(id: String) =
    sql"""select * from "InventoryItem" where id = $id""".query[InventoryItem].option.attempt

  def insert(item: InventoryItem): ConnectionIO[Either[Throwable, InventoryItem]] =
    sql"""
      insert into "InventoryItem" (id, title, description, "itemType")
      values (${item.id}, ${item.title}, ${item.description}, ${item.itemTypeCode})
    """.update.withUniqueGeneratedKeys[InventoryItem]("id", "title", "description", "itemType").attempt

  def delete(id: String): ConnectionIO[Either[Throwable, Int]] =
    sql"""delete from "InventoryItem" where id = $id""".update.run.attempt
}
