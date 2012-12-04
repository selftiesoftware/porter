/*
 * Copyright (c) 2012. Siigna is released under the creative common license by-nc-sa. You are free
 * to Share — to copy, distribute and transmit the work,
 * to Remix — to adapt the work
 *
 * Under the following conditions:
 * Attribution —  You must attribute the work to http://siigna.com in the manner specified by the author or licensor (but not in any way that suggests that they endorse you or your use of the work).
 * Noncommercial — You may not use this work for commercial purposes.
 * Share Alike — If you alter, transform, or build upon this work, you may distribute the resulting work only under the same or similar license to this one.
 */

package com.siigna.module.porter.PDF

import com.siigna.module.porter.PDF.contents.OBJsections._
import sun.misc.BASE64Encoder

//TODO: implement lines, rectangles, arcs, and circles generation as done in https://github.com/MrRio/jsPDF/blob/master/jspdf.js

class PDF {
  var defaultPageFormat = "a4"
  var k = 72/25.4 // Scale factor - hardcoded to mm.
  var textColor = "0 g"
  var unit = "mm"
  var fontSize = 12
  var pageFontSize = 12

  /*
    this function adds the obligatory OBJsections to the buffer (by callig functions in the OBJsections object:
    - the header
    - the page format (size, orinetation)
    - the resources (fonts and images)
    - the catalog
    - the trailer
   */

  def parseDocument = {
    state = 1
    header //%PDF-1.4
    newObject //create the first obj tag
    catalog
    newObject
    outlines
    newObject
    pageDefinition
    newObject
    stream
    newObject
    out("[ /PDF ] ")
    out("endobj")

    //cross-reference table
    var endBufferTag = buffer.size.toString //prepare to write the buffersize before EOF.
    out("xref")
    out("0 " + (objectNumber + 1))
    out("0000000000 65535 f ")

    //create the offset tables (a description of the buffer size at each of the objects:
    //for (i <- 0 to objectNumber) out(offsetDefinition(i))
    "offsetdefs: " +offsetDefinition

    //out(format("%010d 00000 n ", offsets(i)))

    //Trailer
    out("trailer")
    out("<<")
    trailer
    out(">>")
    out("startxref")
    out(endBufferTag)
    out("%%EOF")
    state = 3
  }

  //create the PDF output
  def output(inputType : Option[String]) = {
    //run the main paring function
    parseDocument
    if(inputType == None) {
      buffer
    }
    if(inputType == Some("datauri")) {
      //var encodedBuffer = new BASE64Encoder().encode(buffer.reverse.mkString.getBytes())
      //println("encoded buffer: "+encodedBuffer)
      println(buffer.reverse.mkString)
      buffer.reverse.mkString
      //encodedBuffer
    } else "PDF FILE INVALID"
  }

  def setFontSize(size : Int) =  {
    fontSize = size
  }

  //TODO: activate: add text to the PDF document: (x : position, y : position, text string)
  def text(x : Int, y : Int, text : String) = {
    if(pageFontSize != fontSize) {   //evaluate the current page´s font size:
      //out("BT /F1 " + fontSize + ".00 Tf ET")
      pageFontSize = fontSize
    }
    var str = format("BT %.2f %.2f Td (%s) Tj ET", x * k, (pageHeight - y) * k, pdfEscape(text))
    //out(str)
  }
}




