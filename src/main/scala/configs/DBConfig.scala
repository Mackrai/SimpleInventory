package io.simple.inventory
package configs

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

case class DBConfig(driver: String,
                    url: String,
                    user: String,
                    pass: String)

object DBConfig {
  implicit val reader: ConfigReader[DBConfig] = deriveReader
}