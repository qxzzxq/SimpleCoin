package com.qinxuzhou.simplecoin


/**
  * @author qxzzxq
  */
object App {

  def main(args: Array[String]) {

    //    var simpleCoinBlockChain = ArrayBuffer[Block](createGenesisBlock())
    //    var previousBlock = simpleCoinBlockChain(0)
    //
    //    val chainRange = 1 to 20
    //
    //    chainRange
    //      .foreach(
    //        _ => {
    //          val blockToAdd = nextBlock(previousBlock)
    //          simpleCoinBlockChain += blockToAdd // Update blockchain
    //          previousBlock = blockToAdd
    //
    //          println(s"Add new block #${blockToAdd.index}")
    //          println(blockToAdd.toString)
    //        }
    //      )

    val simpleCoinNode: Node = new Node
    simpleCoinNode.runServer()
  }


  def createGenesisBlock(): Block = {
    new Block(
      index = 0,
      timestamp = System.currentTimeMillis / 1000,
      data = "Genesis block",
      previousHash = "0"
    )
  }


  def nextBlock(lastBlock: Block): Block = {
    val thisIndex = lastBlock.index + 1
    val thisTimeStamp = System.currentTimeMillis / 1000
    val thisData = s"This this block $thisIndex"
    val previousHash = lastBlock.hash

    // Create new block
    new Block(thisIndex, thisTimeStamp, thisData, previousHash)
  }
}
