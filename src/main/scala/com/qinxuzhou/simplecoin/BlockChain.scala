package com.qinxuzhou.simplecoin

import com.qinxuzhou.simplecoin.utils.JsonBlockChain

import scala.collection.mutable.ArrayBuffer

case class BlockChain(var blockChain: ArrayBuffer[Block]) {

  def add(block: Block): ArrayBuffer[Block] = {
    blockChain += block
  }

  def last: Block = {
    blockChain.last
  }

  def length: Int = {
    blockChain.length
  }

  def isDifferentFrom(otherChain: BlockChain): Boolean = {
    if (blockChain.length != otherChain.length) {
      true
    } else false
  }

  def toJsonBlockChain: JsonBlockChain = {
    val _jsonFormatChain = blockChain
      .map(block => block.toJsonBlock)
      .toArray

    JsonBlockChain(_jsonFormatChain)
  }
}
