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
import java.io.{FileInputStream, File, InputStream}

import org.kabeja.dxf._
import org.kabeja.parser.Parser
import org.kabeja.parser.DXFParser
import org.kabeja.parser.ParserBuilder

object DXFImport {

  //a function to read a DXF file and create the shapes in it.
  def apply(input : InputStream) = {
    val parser : Parser = ParserBuilder.createDefaultParser()
    var shapesCount = 0
    var points : List[Vector2D] = List()
    var pointsInImport = 0

    try {
      parser.parse(input, DXFParser.DEFAULT_ENCODING)//parse

      val doc : DXFDocument = parser.getDocument  //get the document and the layer
      val layers = doc.getDXFLayerIterator //get the layers in the DXF file
      //TODO: extract the layer name and give to the doc.getDXFLayer method
      var shapes = List[Shape]()

      while(layers.hasNext) {
        val l = layers.next().asInstanceOf[DXFLayer]
        val layer : DXFLayer = doc.getDXFLayer(l.getName)

        //get extractable objects:
        val lines = layer.getDXFEntities("LINE")
        val mLines = layer.getDXFEntities("MLINE")
        val polylines = layer.getDXFEntities("POLYLINE")
        val LwPolylines = layer.getDXFEntities("LWPOLYLINE")

        //add the entities to a list
        val entityList = List(lines,mLines,polylines,LwPolylines).toArray

        //iterate through the list and collect the shapes:
        for (i <- 0 to 3) {
          val entity = entityList(i)
          if (entity != null) {
            entity.toArray.collect {
              //Polylines
              case p : DXFPolyline => {
                var size = p.getVertexCount
                var width = p.getLineWeight.toDouble
                for (i <- 0 until size) {
                  var point = (p.getVertex(i).getPoint)
                  var vector = Vector2D(point.getX,point.getY)
                  if (vector.length != 0) points = points :+ vector
                }
                // TODO: remove this restriction when performance improves.
                if (pointsInImport < 1000) {
                  shapes = shapes :+ PolylineShape(points).addAttribute("StrokeWidth" -> width/100)
                  pointsInImport += size
                  shapesCount += 1
                  Siigna display ("imported " + shapesCount +" shapes")
                } else Siigna display ("import limit exceeded")
                points = List()
              }
              //lines
              case p : DXFLine => {
                var width = p.getLineWeight.toDouble
                var line = LineShape(Vector2D(p.getStartPoint.getX,p.getStartPoint.getY),Vector2D(p.getEndPoint.getX,p.getEndPoint.getY)).addAttribute("StrokeWidth" -> width/100)
                // TODO: remove this restriction when performance improves.
                if (pointsInImport < 1000) {
                  shapes = shapes :+ line.addAttribute("StrokeWidth" -> width/100)
                  pointsInImport += 2
                  shapesCount += 1
                  Siigna display ("imported " + shapesCount +" shapes")
                } else Siigna display ("import limit exceeded")
              }
              case c : DXFCircle => {
                var circle = CircleShape(Vector2D(c.getCenterPoint.getX,c.getCenterPoint.getY),c.getRadius)
                // TODO: remove this restriction when performance improves.
                if (pointsInImport < 1000) {
                  shapes = shapes :+circle
                  pointsInImport += 2
                  shapesCount += 1
                  Siigna display ("imported " + shapesCount +" shapes")
                } else Siigna display ("import limit exceeded")
              }
            }
          }
        }
        //if the drawing has a certain complexity, do the Creation in four iterations to prevent server overload
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

        } else Create(shapes)
        //clear the shapes list
        shapes = List()
      }


      //var vertex : DXFVertex = line.getVertex(2)
      //iterate over all vertex of the polyline
      //for (i <- line) {
      //var vertex = line.getVertex(i)
      //}
    } catch {
      case e : Throwable => {
      input.close()
      println("found error: "+ e)
      Nil
      }
    }
    input.close()
  }
}