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

package com.siigna.module.porter.PDF.contents

import com.siigna.module.porter.PDF.contents.OBJsections._
import com.siigna.util.geom.Vector2D
import com.siigna._
import com.siigna.Drawing

/**
 * an object containing code that parse Siigna shapes and text into a PDF stream
 */

object STREAMsection {

  var mm = 72/25.4
  var fontSize = 12
  var pageFontSize = 12

  //add a line. TODO: add width and color.
  def line(p1 : Vector2D, p2 : Vector2D) = {
    out(p1.x * mm + " " + p1.y  * mm + " m")
    out(p2.x * mm + " " + p2.y  * mm + " l")
    out("S")
  }

  def linesEvaluation = {
    if(Drawing.shapes.size != 0) {
      val shapes = Drawing.shapes.map (t => (t._2))
      shapes.foreach(s =>
        s match {
          case l : LineShape => {
            line(l.p1.transform(View.drawingTransformation),l.p2.transform(View.drawingTransformation))
          }
          case _ => println("no match on shapes: "+shapes)
        }
      )
    } else {
      println("no lines in the drawing")
    }
  }

  //add shapes to the stream
  def stream = {
    out("<</Length 35>>")
    out("stream")
    linesEvaluation
    out("endstream")
    out("endobj")
  }

  //add text
  def text(x : Int, y : Int, text : String) = {
    if(pageFontSize != fontSize) {   //evaluate the current page´s font size:
      //out("BT /F1 " + fontSize + ".00 Tf ET")
      pageFontSize = fontSize
    }
    var str = format("BT %.2f %.2f Td (%s) Tj ET", x * k, (pageHeight - y) * k, text)
    //var str = format("BT %.2f %.2f Td (%s) Tj ET", x * k, (pageHeight - y) * k, pdfEscape(text))
    //out(str)
  }
}
