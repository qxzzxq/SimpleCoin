package com.qinxuzhou.simplecoin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import spray.json.DefaultJsonProtocol

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


object Node {

  def main(args: Array[String]) {

    val simpleCoinNode: SimpleCoinNode = new SimpleCoinNode

    simpleCoinNode.runServer()

  }

  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val transactionFormat = jsonFormat3(Transaction)
  }

  final case class Transaction(from: String, to: String, amount: Float)

  class SimpleCoinNode extends Directives with JsonSupport {

    implicit val system: ActorSystem = ActorSystem("SimpleCoinNode", ConfigFactory.load())
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    val route =
      path("") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>SimpleCoin Node v0.0.1</h1>"))
        }
      } ~
        path("tx") {
          post {
            entity(as[Transaction]) { transaction =>
              thisNodesTransactions += transaction

              println("New transaction:")
              println(s"From: ${transaction.from}")
              println(s"To: ${transaction.to}")
              println(s"Amount: ${transaction.amount}")

              complete(s"Transaction succeed. tx: $transaction")
            }
          }
        }
    var thisNodesTransactions: ArrayBuffer[Transaction] = ArrayBuffer[Transaction]()

    def runServer(): Unit = {
      val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      StdIn.readLine() // let it run until user presses return
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    }

  }

}