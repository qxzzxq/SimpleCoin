package com.qinxuzhou.simplecoin.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.qinxuzhou.simplecoin.{Block, BlockChain}
import spray.json.{DefaultJsonProtocol, PrettyPrinter, RootJsonFormat}

import scala.collection.mutable.ArrayBuffer


final case class Transaction(from: String,
                             to: String,
                             amount: Float) {
  override def toString: String = {
    s"""{"from": $from, "to": $to, "amount": $amount}"""
  }
}

final case class JsonBlockChain(blockChain: Array[JsonBlock]) {
  def toBlockChain: BlockChain = {
    val _blockChain = blockChain.map(block => block.toBlock)
    BlockChain(ArrayBuffer(_blockChain: _*))
  }

}


final case class JsonBlock(index: Int,
                           timestamp: Long,
                           nonce: Int,
                           data: BlockData,
                           hash: String,
                           previousHash: String) {
  def toBlock: Block = {
    Block(index, timestamp, nonce, data, hash, previousHash)
  }
}


final case class BlockData(transaction: Array[Transaction],
                           message: String) {
  override def toString: String = {
    s"""{"transaction": [${transaction.map(_.toString).mkString(", ")}], "message": $message}"""
  }
}


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer: PrettyPrinter.type = PrettyPrinter
  implicit val transactionFormat: RootJsonFormat[Transaction] = jsonFormat3(Transaction)
  implicit val blockDataFormat: RootJsonFormat[BlockData] = jsonFormat2(BlockData)
  implicit val jsonBlockFormat: RootJsonFormat[JsonBlock] = jsonFormat6(JsonBlock)
  implicit val blockChainFormat: RootJsonFormat[JsonBlockChain] = jsonFormat1(JsonBlockChain)
}
