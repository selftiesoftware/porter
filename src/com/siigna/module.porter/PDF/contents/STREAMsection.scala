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
import java.awt.Color

/**
 * an object containing code that parse Siigna shapes and text into a PDF stream
 */

object STREAMsection {

  var mm = 72/25.4

  //add a circle. TODO: add width and color.
  def circle(p : Vector2D, r : Double, a : Attributes) = {
    out(p.x + " " + p.y + " "+r+" "+r+" c")
    width(a)
    out("S")
  }

  //add colors to exported shapes
  def color(a : Attributes) {
    if(a.isDefinedAt("Color")) {
      val input = a.get("Color")
      val c = input.get.asInstanceOf[java.awt.Color]
      val r = c.getRed/100
      val g = c.getGreen/100
      val b = c.getBlue/100
      out(r +" "+ b +" "+ g + " RG") //R G B and (RG = stroke, rg = fill)
      }
    else out("0.0 0.0 0.0 RG")
  }

  //add a drawing header
  def header = {
    val header = new com.siigna.module.base.paperHeader
    val scaleText = header.scale
    val string = scaleText.text
    val pos = Vector2D(pageSize._1 - 90, 18)
    val size = 4
    text(pos,string,size)
    text(Vector2D(pageSize._1 - 420, 18),"created @ www.siigna.com - free online drawing app and library", 3)
  }

  //add a line. TODO: add width and color.
  def line(p1 : Vector2D, p2 : Vector2D, a : Attributes) = {
    color(a)
    out(p1.x + " " + p1.y + " m")
    out(p2.x + " " + p2.y + " l")
    width(a)
    out("S")
  }

  //add a polyline. TODO: add width and color.
  def polyline(s : Vector2D, points : Seq[InnerPolylineShape], a : Attributes) = {
    val pts = points.toList
    color(a)
    out(s.x + " " + s.y + " m")
    pts.foreach(p => out(rePos(p.point).x + " " + rePos(p.point).y + " l"))
    width(a)
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

  def shapesEvaluation = {
    if(Drawing.shapes.size != 0) {
      val shapes = Drawing.shapes.map (t => (t._2))
      shapes.foreach(s =>
        s match {
          case c : CircleShape => {
            //circle(rePos(c.center),c.radius, c.attributes)
          }
          case l : LineShape => {
            line(rePos(l.p1),rePos(l.p2),l.attributes)
          }
          case p : PolylineShape => {
            val points = p.innerShapes
            polyline(rePos(p.startPoint), points, p.attributes)
          }
          case t : TextShape => {
            val content = t.text
            val pos = t.position
            val size = t.fontSize.toInt
            text(rePos(pos), content, size)
          }
          case e => println("no match on shapes: "+shapes)
        }
      )
    } else {
      println("no lines in the drawing")
      None
    }
  }

  //add shapes to the stream
  def stream = {
    out("<</Length 35>>")
    out("stream")
    //CREATE THE HEADER:
    header
    shapesEvaluation //add shapes
    out("endstream")
    out("endobj")
  }

  def text(p : Vector2D, text : String, size : Int) {
    //out("0.57 w")
    //out("0 G")
    out("BT")
    out(p.x + " " + p.y + " TD")
    out("/F1 " + size + " Tf")
    //out(size + "TL")
    //out("0 g")
    //out(x + y + "Td")
    out("("+text + ")"+" Tj")
    out("ET")
  }

  def width(a : Attributes) {
    val s = a.get("StrokeWidth")
    if(!s.isEmpty) out(s.get + " w")
    else out(0.3 +" w")
  }
}
