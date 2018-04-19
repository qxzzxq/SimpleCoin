package com.qinxuzhou.simplecoin


object Hasher {

  def sha256Hash(text: String): String = {
    String.format(
      "%064x",
      new java.math.BigInteger(
        1,
        java.security
          .MessageDigest
          .getInstance("SHA-256")
          .digest(text.getBytes("UTF-8"))))
  }

  def hashOfBlock(index: Int, data: BlockData, previousHash: String, nonce: Int): String = {
    sha256Hash(s"$index$data$previousHash$nonce")
  }

}
