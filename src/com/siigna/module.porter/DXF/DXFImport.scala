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

package com.siigna.module.porter.DXF

import com.siigna._
import java.io.{FileInputStream, File, InputStream}

import org.kabeja.dxf._
import org.kabeja.parser.Parser
import org.kabeja.parser.DXFParser
import org.kabeja.parser.ParserBuilder

class DXFExtractor{
  import scala.collection.immutable.List

  var points : List[Vector2D] = List()

  //a function to read a DXF file and create the shapes in it.
  def read(file : File) = {
    val input : InputStream = new FileInputStream(file)
    val parser : Parser = ParserBuilder.createDefaultParser()

    try {
      parser.parse(input, DXFParser.DEFAULT_ENCODING)//parse

      val doc : DXFDocument = parser.getDocument  //get the document and the layer
      val layers = doc.getDXFLayerIterator //get the layers in the DXF file
      //TODO: extract the layer name and give to the doc.getDXFLayer method

      while(layers.hasNext) {
        val l = layers.next().asInstanceOf[DXFLayer]
        println("LAYER: "+l)
        val layer : DXFLayer = doc.getDXFLayer(l.getName)
        println("layer name: "+layer)

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
                for (i <- 0 until size) {
                  var point = (p.getVertex(i).getPoint)
                  var vector = Vector2D(point.getX,point.getY)
                  if (vector.length != 0) points = points :+ vector
                }
                Create(PolylineShape(points))
                points = List()
              }
              //lines
              case p : DXFLine => {
                var line = LineShape(Vector2D(p.getStartPoint.getX,p.getStartPoint.getY),Vector2D(p.getEndPoint.getX,p.getEndPoint.getY))
                Create(line)
              }
              case c : DXFCircle => {
                var circle = CircleShape(Vector2D(c.getCenterPoint.getX,c.getCenterPoint.getY),c.getRadius)
                Create(circle)
              }
            }
          }
        }
      }


      //var vertex : DXFVertex = line.getVertex(2)
      //iterate over all vertex of the polyline
      //for (i <- line) {
      //var vertex = line.getVertex(i)
      //}
    } catch {
      case e => {
      input.close()
      println("found error: "+ e)
      Nil
      }
    }
    input.close()
  }
}