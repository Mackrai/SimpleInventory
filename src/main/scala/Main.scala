package io.simple.inventory

import configs.{AppConfig, DBConfig, ServerConfig}
import repositories.{InventoryRepo, ItemTypeRepo}
import routers.{InventoryRouter, ItemTypeRouter, Router}
import services.{InventoryService, ItemTypeService}

import cats.effect.kernel.Async
import cats.effect.{ExitCode, IO, IOApp}
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import org.http4s.{HttpApp, Uri}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.headers.Origin
import org.http4s.server.middleware.CORS
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import sttp.tapir.server.http4s.Http4sServerInterpreter

object Main extends IOApp  {

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def makeTransactor[F[_] : Async](conf: DBConfig): Aux[F, Unit] =
    Transactor.fromDriverManager[F](conf.driver, conf.url, conf.user, conf.pass)

  def makeRouters[F[_] : Async](tx: Transactor[F]): List[Router[F]] = {
    val itemTypeRepo = new ItemTypeRepo
    val inventoryRepo = new InventoryRepo

    val itemTypeService = new ItemTypeService[F](tx, itemTypeRepo)
    val inventoryService = new InventoryService[F](tx, inventoryRepo, itemTypeService)

    List(
      new InventoryRouter[F](inventoryService, itemTypeService),
      new ItemTypeRouter[F](itemTypeService)
    )
  }

  def startServer[F[_] : Async](serverConf: ServerConfig, httpApp: HttpApp[F]) = {
    val withCORS =
      CORS.policy
        .withAllowOriginAll
//        .withAllowOriginHost(Set(
//          Origin.Host(Uri.Scheme.http, Uri.RegName("localhost"), None)
//        ))

      BlazeServerBuilder[F]
        .bindHttp(serverConf.port, serverConf.host)
        .withHttpApp(withCORS(httpApp))
        .serve
        .compile
        .drain
  }


  override def run(args: List[String]): IO[ExitCode] =
    for {
      config <- IO.delay(ConfigSource.default.loadOrThrow[AppConfig]) <* logger.info("Loaded app config")
      tx = makeTransactor[IO](config.db)
      routers = makeRouters[IO](tx)
      routes = routers.flatMap(_.endpoints)
//      swaggerEndpoints = SwaggerInterpreter().fromServerEndpoints[IO](routes, "My App", "1.0")
      httpApp = Http4sServerInterpreter[IO].toRoutes(routes).orNotFound
      _ <- startServer(config.server, httpApp)
    } yield ExitCode.Success
}
