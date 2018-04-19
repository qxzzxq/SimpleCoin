package com.qinxuzhou.simplecoin.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.qinxuzhou.simplecoin.{BlockData, JsonBlock}
import spray.json.{DefaultJsonProtocol, PrettyPrinter, RootJsonFormat}


final case class Transaction(from: String,
                             to: String,
                             amount: Float)

final case class BlockChain(blockChain: Array[JsonBlock])


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer: PrettyPrinter.type = PrettyPrinter
  implicit val transactionFormat: RootJsonFormat[Transaction] = jsonFormat3(Transaction)
  implicit val blockDataFormat: RootJsonFormat[BlockData] = jsonFormat2(BlockData)
  implicit val jsonBlockFormat: RootJsonFormat[JsonBlock] = jsonFormat6(JsonBlock)
  implicit val blockChainFormat: RootJsonFormat[BlockChain] = jsonFormat1(BlockChain)
}
