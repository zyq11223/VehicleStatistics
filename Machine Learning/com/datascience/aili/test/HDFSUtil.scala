package com.datascience.aili.test

import java.io.IOException
import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, FileSystem, Path}

object HDFSUtil {
  val conf: Configuration = new Configuration
  var fs: FileSystem = null
  var hdfsInStream: FSDataInputStream = null

  def getFSDataInputStream(path: String): FSDataInputStream = {
    try {
      fs = FileSystem.get(URI.create(path), conf)
      hdfsInStream = fs.open(new Path(path))
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
    return hdfsInStream
  }

  def close {
    try {
      if (hdfsInStream != null) {
        hdfsInStream.close
      }
      if (fs != null) {
        fs.close
      }
    }
    catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
  }
}
