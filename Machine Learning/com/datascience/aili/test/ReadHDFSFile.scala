package com.datascience.aili.test
import org.apache.hadoop.fs.FSDataInputStream

/**
    * 用scala读取一个只有字符的文件，该文件是已经清洗完的数据，有规律，一行是一条数据。
    * 现在的需求是一次读取文件中的一部分，依次读取完。
    * Created by wx on 2017/7/21.
    */
  object ReadHDFSFile {
    def main(args: Array[String]) {
      var inputStream: FSDataInputStream = null
      try {
        inputStream = HDFSUtil.getFSDataInputStream("hdfs://master:9000/csw/TestData/aviation9/part-00000")
        //每次读取的字节长度
        val readLength = 1024
        //创建一个字节数组
        var ioBuffer: Array[Byte] = new Array[Byte](readLength)
        //先读取一个1024字节的数据
        var readLen = inputStream.read(ioBuffer)
        //因为每次读取1024个字节，会遇到一行读取一半，剩下的一半下一次读取。
        //在这设置个临时变量来存放前面读取的一半，然后和后面读取到的拼接起来
        var tmp = ""
        //只做一个计数
        var count = 1
        while (readLen != -1) {
          //将读取到字节转成字符串
          var str = new String(ioBuffer, 0, readLen)
          /*这里是处理，当读取中文时，因为中文是两个字节，回读取一半，
          这样在转码的时候就会报错，当遇到这样的情况，需要再往后读取一个字节，然后和之前的组成新的字节数组
          这样可以避免出现读取一半的情况，但是我的数据量很多，即使这样处理，还是会偶尔遇到乱码，
          最后我又改进了，一次读取一行。*/
          val b2: Array[Byte] = str.getBytes("utf8")
          if (b2(b2.length - 1) == -67) {
            var b3: Array[Byte] = new Array[Byte](1)
            inputStream.read(b3);
            val temp: Byte = b3(0)
            b3 = java.util.Arrays.copyOf(ioBuffer, b3.length + ioBuffer.length)
            b3(b3.length - 1) = temp;
            str = new String(b3)
          }
          //按换行符切割
          val arr = str.split("\n")
          val arrLength = arr.length
          //获取数据还剩多少字节可读
          var available = inputStream.available()
          println(available + "剩余可读字节数")
          //在这个里进行数据的判断，将一整行数据输出，将不完整的数据进行存放和拼接
          for (i <- 0 until arrLength) {
            if (i == 0) {
              println(count + "\t" + tmp + arr(i) + "\t我再这")
              count += 1
            } else if (i == arrLength - 1 && available != 0) {
              tmp = arr(arrLength - 1)
            } else {
              println(count + "\t" + arr(i) + "\t我再这")
              count += 1
            }
          }
          //判断当可读字节小于一开始设定的字节
          if (available < readLength) {
            //最后一次读取，按照未读的字节的长度新建一个byte数组，这样可以避免最后会重复读取数据。
            ioBuffer = new Array[Byte](available)
            readLen = inputStream.read(ioBuffer)
          } else {
            readLen = inputStream.read(ioBuffer)
          }
          //让程序暂停睡眠一下
          Thread.sleep(500)
        }
      } catch {
        case e: Exception => {
          e.printStackTrace()
        }
      } finally {
        if (inputStream != null) {
          HDFSUtil.close
        }
      }
    }
  }

