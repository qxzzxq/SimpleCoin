package com.qinxuzhou.simplecoin

import com.qinxuzhou.simplecoin.Hasher.hashOfBlock
import com.qinxuzhou.simplecoin.utils.Transaction
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException


class Block(val index: Int,
            val timestamp: Long,
            val nonce: Int = 0,
            val data: BlockData,
            val previousHash: String) {

  val hash: String = validateHash()

  def validateHash(): String = {
    val newHash = hashOfBlock(index, data, previousHash, nonce)

    newHash.take(4) match {
      case "0000" => newHash
      case _ => throw new ValueException("Not a valid hash!")
    }
  }

  def toJsonBlock: JsonBlock = {
    JsonBlock(index, timestamp, nonce, data, hash, previousHash)
  }

}


final case class JsonBlock(index: Int,
                           timestamp: Long,
                           nonce: Int,
                           data: BlockData,
                           hash: String,
                           previousHash: String
                          )


final case class BlockData(transaction: Array[Transaction],
                           message: String)
