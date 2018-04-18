package com.qinxuzhou.simplecoin.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.qinxuzhou.simplecoin.Transaction
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val transactionFormat: RootJsonFormat[Transaction] = jsonFormat3(Transaction)
}
