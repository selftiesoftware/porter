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
import app.model.shape.InnerPolylineShape
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
    out(p1.x + " " + p1.y + " m")
    out(p2.x + " " + p2.y + " l")
    out("S")
  }

  //add a polyline. TODO: add width and color.
  def polyline(s : Vector2D, points : Seq[InnerPolylineShape]) = {
    val pts = points.toList
    out(s.x + " " + s.y + " m")
    pts.foreach(p => out(rePos(p.point).x + " " + rePos(p.point).y + " l"))
    out("S")
  }

  //TODO: test if scaling is right across different zoom scales
  def rePos(v : Vector2D) = {
    //find the bounding box of the drawing
    val box = Drawing.boundary.center
    val landscape = OBJsections.pageSize._3
    //transform all objects so that the center point of the bounding box matches that of the PDF.
    val paperCenter = if (landscape == true) Vector2D(420.5, 298.5) else Vector2D(298.5,420.5)
    //move the center of the shapes to 0,0, then scale them to millimeters. (PDF is in inches per default)
    val t = TransformationMatrix(-box,1) //move to 0,0
    val t2 = TransformationMatrix(Vector2D(0,0),mm / Siigna.paperScale) //scale to mm and divide by the paper scale.
    val t3 = TransformationMatrix(paperCenter,1) // move the shapes to the center of the paper
    val transformed = v.transform(t)  //perform the first transformation
    val transformed2 = transformed.transform(t2) //perform the second.
    val transformed3 = transformed2.transform(t3) //perform the third.
    transformed3
  }

  def linesEvaluation = {
    if(Drawing.shapes.size != 0) {
      val shapes = Drawing.shapes.map (t => (t._2))
      shapes.foreach(s =>
        s match {
          case l : LineShape => {
            line(rePos(l.p1),rePos(l.p2))
          }
          case p : PolylineShape => {
            val points = p.innerShapes
            polyline(rePos(p.startPoint), points)
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
    //var str = format("BT %.2f %.2f Td (%s) Tj ET", x, (pageHeight - y), text)
    //var str = format("BT %.2f %.2f Td (%s) Tj ET", x * k, (pageHeight - y) * k, pdfEscape(text))
    //out(str)
  }
}
