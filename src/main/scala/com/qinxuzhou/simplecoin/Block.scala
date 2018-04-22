package com.qinxuzhou.simplecoin

import com.qinxuzhou.simplecoin.HashAlgorithm.sha256Hash
import com.qinxuzhou.simplecoin.utils.{BlockData, JsonBlock}
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException


case class Block(index: Int,
                 timestamp: Long,
                 nonce: Int,
                 data: BlockData,
                 hash: String,
                 previousHash: String) {

  val difficulty: Int = 4
  private val _hash = hashOfBlock(index, data, previousHash, nonce, sha256Hash)
  private val validHashPrefix = "0" * difficulty

  if (hash != _hash || !hash.startsWith(validHashPrefix)) {
    throw new ValueException("Not a valid hash!")
  }

  def hashOfBlock(index: Int,
                  data: BlockData,
                  previousHash: String,
                  nonce: Int,
                  hashAlgorithm: String => String): String = {
    hashAlgorithm(s"$index$data$previousHash$nonce")
  }

  def toJsonBlock: JsonBlock = {
    JsonBlock(index, timestamp, nonce, data, hash, previousHash)
  }

}

