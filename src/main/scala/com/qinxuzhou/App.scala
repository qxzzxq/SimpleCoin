package com.qinxuzhou


/**
  * @author ${user.name}
  */
object App {

  def main(args: Array[String]) {
    println(createGenesisBlock().toString)
  }

  def createGenesisBlock(): Block = {
    new Block(
      index = 0,
      timestamp = System.currentTimeMillis / 1000,
      data = "Genesis block",
      previousHash = "0"
    )
  }

}
