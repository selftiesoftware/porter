/*
 * Copyright (c) 2008-2013. Siigna is released under the creative common license by-nc-sa. You are free
 * to Share — to copy, distribute and transmit the work,
 * to Remix — to adapt the work
 *
 * Under the following conditions:
 * Attribution —  You must attribute the work to http://siigna.com in the manner specified by the author or licensor (but not in any way that suggests that they endorse you or your use of the work).
 * Noncommercial — You may not use this work for commercial purposes.
 * Share Alike — If you alter, transform, or build upon this work, you may distribute the resulting work only under the same or similar license to this one.
 */

package com.siigna.module.porter.DXF

import com.siigna._
import java.io.InputStream

import org.kabeja.dxf._
import org.kabeja.parser.Parser
import org.kabeja.parser.DXFParser
import org.kabeja.parser.ParserBuilder
import main.scala.com.siigna.module.porter.DXF.ColorParser
import com.siigna.app.Siigna
import sun.misc.IOUtils
import java.awt.Color

object DXFImport {

  var shapes = List[Shape]()
  var pointsInImport = 0

  //evaluate what kind of
  def attributesAdd(color : Option[Color], width : Double) : Option[Attributes] = {
    if(color.isDefined && width != 0) Some(Attributes("StrokeWidth" -> width/100, "Color" -> color.get))
    else if(!color.isDefined && width != 0) Some(Attributes("StrokeWidth" -> width/100))
    else if(color.isDefined && width == 0) Some(Attributes("Color" -> color.get))
    else None
  }


  def parse(input : InputStream) {
    val parser : Parser = ParserBuilder.createDefaultParser()
    var shapesCount = 0
    var points : List[Vector2D] = List()
    val defaultColor = "java.awt.Color[r=0,g=0,b=0]"

    try {
      parser.parse(input, DXFParser.DEFAULT_ENCODING)//parse

      val doc : DXFDocument = parser.getDocument  //get the document and the layer
      val layers = doc.getDXFLayerIterator //get the layers in the DXF file
      //TODO: extract the layer name and give to the doc.getDXFLayer method

      while(layers.hasNext) {

        val l = layers.next().asInstanceOf[DXFLayer]
        val layer : DXFLayer = doc.getDXFLayer(l.getName)

        //get extractable objects:
        val lines = layer.getDXFEntities("LINE")
        val mLines = layer.getDXFEntities("MLINE")
        val polylines = layer.getDXFEntities("POLYLINE")
        val LwPolylines = layer.getDXFEntities("LWPOLYLINE")
        val arcs = layer.getDXFEntities("ARC")

        //add the entities to a list
        val entityList = List(lines,mLines,polylines,LwPolylines,arcs).toArray

        //iterate through the list and collect the shapes:

        for (i <- 0 to 4) {
          val entity = entityList(i)
          if (entity != null) {
            entity.toArray.collect {
              //Polylines
              case p : DXFPolyline => {
                //a function to set attributes
                val attributes : Option[Attributes] = {
                  val color = ColorParser.setColor(p.getColor.toDouble)
                  val lineWidth = p.getLineWeight.toDouble
                  val a = attributesAdd(color,lineWidth)
                   if(a.isDefined) a else None//check if attributes should be set
                }
                var size = p.getVertexCount

                for (i <- 0 until size) {
                  val point = (p.getVertex(i).getPoint)
                  val vector = Vector2D(point.getX,point.getY)
                  if (vector.length != 0) points = points :+ vector
                }

                if (pointsInImport < 50000 && points.length > 1) { // TODO: remove this restriction when performance improves.

                  if(attributes.isDefined) shapes = shapes :+ PolylineShape(points).addAttributes(attributes.get)
                  else shapes = shapes :+ PolylineShape(points)
                  pointsInImport += size
                  Siigna display ("points in import: "+ pointsInImport)
                  shapesCount += 1
                  Siigna display ("imported " + shapesCount +" shapes")
                } else Siigna display ("import limit exceeded")
                points = List()
              }
              //lines
              case p : DXFLine => {
                //a function to set attributes
                val attributes : Option[Attributes] = {
                  val color = ColorParser.setColor(p.getColor.toDouble)
                  val lineWidth = p.getLineWeight.toDouble
                  val a = attributesAdd(color,lineWidth)
                  if(a.isDefined) a else None//check if attributes should be set
                }

                val line = LineShape(Vector2D(p.getStartPoint.getX,p.getStartPoint.getY),Vector2D(p.getEndPoint.getX,p.getEndPoint.getY))

                if (pointsInImport < 50000) {// TODO: remove this restriction when performance improves.
                  if(attributes.isDefined) shapes = shapes :+ line.addAttributes(attributes.get)
                  else shapes = shapes :+ line
                  pointsInImport += 2
                  shapesCount += 1
                  Siigna display ("imported " + shapesCount +" shapes")
                } else Siigna display ("import limit exceeded")
              }
              case c : DXFCircle => {
                //a function to set attributes
                val attributes : Option[Attributes] = {
                  val color = ColorParser.setColor(c.getColor.toDouble)
                  val lineWidth = c.getLineWeight.toDouble
                  val a = attributesAdd(color,lineWidth)
                  if(a.isDefined) a else None//check if attributes should be set
                }
                val circle = CircleShape(Vector2D(c.getCenterPoint.getX,c.getCenterPoint.getY),c.getRadius)

                if (pointsInImport < 50000) { // TODO: remove this restriction when performance improves.
                  if(attributes.isDefined) shapes = shapes :+ circle.addAttributes(attributes.get)
                  else shapes = shapes :+ circle
                  pointsInImport += 2
                  shapesCount += 1
                  Siigna display ("imported " + shapesCount +" shapes")
                } else Siigna display ("import limit exceeded")
              }

              case a : DXFArc => {
                //a function to set attributes
                val attributes : Option[Attributes] = {
                  val color = ColorParser.setColor(a.getColor.toDouble)
                  val lineWidth = a.getLineWeight.toDouble
                  val attr = attributesAdd(color,lineWidth)
                  if(attr.isDefined) attr else None//check if attributes should be set
                }
                val arc = ArcShape(Vector2D(a.getCenterPoint.getX,a.getCenterPoint.getY),a.getRadius,a.getStartAngle,a.getTotalAngle)

                if (pointsInImport < 50000) {  // TODO: remove this restriction when performance improves.
                  if(attributes.isDefined) shapes = shapes :+ arc.addAttributes(attributes.get)
                  else shapes = shapes :+ arc
                  shapesCount += 1
                  Siigna display ("imported " + shapesCount +" shapes")
                } else Siigna display ("import limit exceeded")
              }

            }
          }
        }
      }
    } catch {
      case e : Throwable => {
        println("found error: "+ e)
        Nil
      }
    }
  }
  //create shapes from the imported DXF file content.
  //if the drawing has a certain complexity, do the Creation in four iterations to prevent server overload
  def createShapes(s : List[Shape]) = {
    if(pointsInImport > 400 && shapes.length > 10) {
      val a = shapes.take(shapes.length/2)
      val aFirst = a.take(a.length/2)
      val aLast = a.drop(a.length/2)

      val b = shapes.drop(shapes.length/2)
      val bFirst = b.take(b.length/2)
      val bLast = b.drop(b.length/2)

      Create(aFirst)
      Create(aLast)
      Create(bFirst)
      Create(bLast)

    } else if (!shapes.isEmpty) {
      Create(shapes)
    }
    //clear the shapes list
    shapes = List()
  }

  //a function to read a DXF file and create the shapes in it.
  def apply(input : InputStream) = {

    //val s = scala.io.Source.fromInputStream(input).mkString("")

    //run the parser
    parse(input)
    //create the shapes
    if(!shapes.isEmpty) createShapes(shapes)
    input.close()
  }
}