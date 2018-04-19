package com.qinxuzhou.simplecoin

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, StandardRoute}
import akka.http.scaladsl.{Http, server}
import akka.stream.ActorMaterializer
import com.qinxuzhou.simplecoin.Hasher.sha256Hash
import com.qinxuzhou.simplecoin.utils._
import com.typesafe.config.ConfigFactory

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


class Node extends Directives with JsonSupport {

  implicit val system: ActorSystem = ActorSystem("SimpleCoinNode", ConfigFactory.load())
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  var thisNodesTransactions: ArrayBuffer[Transaction] = ArrayBuffer[Transaction]()
  var minerAddress: String = scala.util.Random.alphanumeric.take(30).mkString
  var blockChain: ArrayBuffer[Block] = ArrayBuffer[Block](createGenesisBlock())

  val route: server.Route =
    path("") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>SimpleCoin Node v0.0.1</h1>"))
      }
    } ~
      path("address") {
        get {
          // get miner address
          complete(s"$minerAddress\n")
        }
      } ~
      path("tx") {
        post {
          entity(as[Transaction]) { transaction => addTransaction(thisNodesTransactions, transaction) }
        }
      } ~
      path("mine") {
        get {
          mine(msg = "qxz")
        }
      } ~
      path("blockchain") {
        get {
          val chain = blockChain.toArray.map(_.toJsonBlock)
          complete(BlockChain(chain))
        }
      }


  def addTransaction(nodesTransactions: ArrayBuffer[Transaction], newTx: Transaction): StandardRoute = {
    // Add tx to node's transaction list
    nodesTransactions += newTx

    println("New transaction:")
    println(s"From: ${newTx.from}")
    println(s"To: ${newTx.to}")
    println(s"Amount: ${newTx.amount}")

    complete(s"Transaction succeed. tx: $newTx\n")
  }


  def createGenesisBlock(): Block = {
    val index = 0
    val timestamp = System.currentTimeMillis / 1000
    val data = BlockData(ArrayBuffer[Transaction]().toArray, "Genesis block")
    val previousHash = "0"
    val nonce = proofOfWork(index, data, previousHash, 0)

    new Block(index, timestamp, nonce, data, previousHash)
  }


  @tailrec
  private def proofOfWork(index: Int, data: BlockData, previousHash: String, nonce: Int): Int = {
    val prefix = s"$index$data$previousHash"
    val newHash = sha256Hash(s"$prefix$nonce")
    newHash.take(4) match {
      case "0000" => nonce
      case _ => proofOfWork(index, data, previousHash, nonce + 1)
    }
  }


  def generateNewBlock(previousBlock: Block, newBlockData: BlockData): Block = {
    val newIndex = previousBlock.index + 1
    val previousHash = previousBlock.hash
    val newNonce = proofOfWork(newIndex, newBlockData, previousHash, 0)

    new Block(
      index = newIndex,
      timestamp = System.currentTimeMillis / 1000,
      nonce = newNonce,
      data = newBlockData,
      previousHash = previousHash
    )
  }


  def mine(msg: String = ""): StandardRoute = {
    val lastBlock = blockChain.last
    val miningReward = Transaction(from = "Network", to = minerAddress, amount = 10f)
    thisNodesTransactions += miningReward
    val newBlockData = BlockData(transaction = thisNodesTransactions.toArray, message = msg)

    val newBlock = generateNewBlock(lastBlock, newBlockData)
    thisNodesTransactions.clear()
    blockChain += newBlock

    val output = s"New block ${newBlock.index} (${newBlock.hash}) generate at ${newBlock.timestamp}\n"
    println(output)
    complete(newBlock.toJsonBlock)
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