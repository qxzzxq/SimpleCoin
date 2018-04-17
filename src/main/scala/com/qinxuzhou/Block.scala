package com.qinxuzhou


import com.qinxuzhou.Hasher.sha256Hash

class Block(val index: Int,
            val timestamp: Long,
            val data: String,
            val previousHash: String) {

  val hash: String = sha256Hash(s"$index$timestamp$data$previousHash")

  override def toString: String = {
    s"Hash of block #$index: $hash"
  }

}
