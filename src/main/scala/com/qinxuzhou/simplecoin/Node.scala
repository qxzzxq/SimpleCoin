package com.qinxuzhou.simplecoin

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, StandardRoute}
import akka.http.scaladsl.{Http, server}
import akka.stream.ActorMaterializer
import com.qinxuzhou.simplecoin.utils.JsonSupport
import com.typesafe.config.ConfigFactory

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


class Node extends Directives with JsonSupport {

  implicit val system: ActorSystem = ActorSystem("SimpleCoinNode", ConfigFactory.load())
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val route: server.Route =
    path("") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>SimpleCoin Node v0.0.1</h1>"))
      }
    } ~
      path("address") {
        get {
          // get miner address
          complete(s"$address\n")
        }
      } ~
      path("tx") {
        post {
          entity(as[Transaction]) { transaction => addTransaction(thisNodesTransactions, transaction) }
        }
      } ~
      path("mine") {
        get {
          complete("mining")
        }
      }
  var thisNodesTransactions: ArrayBuffer[Transaction] = ArrayBuffer[Transaction]()
  var address: String = scala.util.Random.alphanumeric.take(30).mkString

  def addTransaction(nodesTransactions: ArrayBuffer[Transaction], newTx: Transaction): StandardRoute = {
    // Add tx to node's transaction list
    nodesTransactions += newTx

    println("New transaction:")
    println(s"From: ${newTx.from}")
    println(s"To: ${newTx.to}")
    println(s"Amount: ${newTx.amount}")

    complete(s"Transaction succeed. tx: $newTx\n")
  }


  def proofOfWork(lastProof: Int): Int = {
    var incrementer = lastProof + 1

    var stop = incrementer % 9 == 0 && incrementer % lastProof == 0

    while (!stop) {
      incrementer += 1
      stop = incrementer % 9 == 0 && incrementer % lastProof == 0
    }

    incrementer
  }


  def runServer(): Unit = {
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}