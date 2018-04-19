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

}
