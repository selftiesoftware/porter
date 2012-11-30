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

import com.siigna.module.Module
import com.siigna._
import java.awt.{FileDialog, Frame, Color}
import module.io.dxf._
import java.io.{FileInputStream, File}

import java.io.{FileInputStream, File, InputStream}
import java.util.ArrayList
import java.util.Iterator
import java.util.List
import org.kabeja.dxf.DXFConstants
import org.kabeja.dxf.DXFDocument
import org.kabeja.dxf.DXFLayer
import org.kabeja.dxf.DXFLine
import org.kabeja.dxf.DXFPolyline
import org.kabeja.dxf.DXFVertex
import org.kabeja.dxf.DXFConstants
import org.kabeja.dxf.helpers.Point
import java.awt.FileDialog
import com.siigna._
import scala.Some
import sun.security.provider.certpath.Vertex

//import org.kabeja.parser.DXFParseException
import org.kabeja.parser.Parser
import org.kabeja.parser.DXFParser
import org.kabeja.parser.ParserBuilder

/**
 * An import module using Kabeja 0.4 DXF import JAR library.
 * Kabeja version 0.4 is released under the apache 2.0 license.
 * See the Kabeja 0.4 licence for more information.
 */
class DXFImport extends Module{
  lazy val anthracite = new Color(0.25f, 0.25f, 0.25f, 1.00f)
  val color = "Color" -> "#AAAAAA".color

  val frame = new Frame

  var fileLength: Int = 0

  //get the individual polylines from a list of polylines and create them in Siigna
  def createPolylines(plines : List[_]) {
    var lines = plines.size
    //iterate over all polylines in the list
    for (i <- 1 to lines) {
     var polyline = plines.get(i)
      println(polyline)//iterate over all vertex of the polyline
      //for (i <- polyline) {
      //  val vertex : Vertex = polyline.getVertex(i)
      //  println("Vertex: "+vertex)
      //}
    }
  }

  //graphics to show the loading progress
  def loadBar(point: Int): Shape = PolylineShape(Rectangle2D(Vector2D(103, 297), Vector2D(point + 103, 303))).setAttribute("raster" -> anthracite)

  def loadFrame: Shape = PolylineShape(Rectangle2D(Vector2D(100, 294), Vector2D(500, 306))).setAttribute(color)

  private var polylines : Option[List[_]] = None

  private var startTime: Option[Long] = None





  def stateMap = Map(
    'Start -> {
      case _ => {

        try {
          //opens a file dialog
          val dialog = new FileDialog(frame)
          dialog.setVisible(true)

          val fileName = dialog.getFile
          val fileDir = dialog.getDirectory
          val file = new File(fileDir + fileName)

          // Can we import the file-type?
          val extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase
          if (extension == "dxf") {
            //these two lines are needed to draw a loading bar
            startTime = Some(System.currentTimeMillis().toLong)
            //TODO: find the correct scaling factor to make loading bar fit large DXF files.
            fileLength = file.length().toInt * 4

            //var data: Int = input.read();
            //while(data != -1) {
            //do something with data...
            //   println(data)

            //data = input.read()
            //}

            // Import!
            val readDXF = new DXFExtractor
            polylines = Some(readDXF.read(file, "0").get)

            //run the
            createPolylines(polylines.get)

            Siigna display "Loading completed."
          } else Siigna display "please select a .dxf file"

        } catch {
          case e => {
            Siigna display "Import cancelled."
          }
        }
        End
      }
    }
  )
  override def paint(g: Graphics, t: TransformationMatrix) {
    g draw loadFrame
    if (fileLength > 0 && ((System.currentTimeMillis() - startTime.get) / (fileLength / 30000)) < 394) {
      g draw loadBar(((System.currentTimeMillis() - startTime.get) / (fileLength / 30000)).toInt)
    } else if (fileLength > 0 && ((System.currentTimeMillis() - startTime.get) / (fileLength / 30000)) > 394)
      g draw loadBar(390)

  }
}

class DXFExtractor{
  def read(file : File, layerid : String) : Option[List[_]] = {
    val input : InputStream = new FileInputStream(file)
    val plines = None
    val parser : Parser = ParserBuilder.createDefaultParser()
    try {
      //parse
      parser.parse(input, DXFParser.DEFAULT_ENCODING)

      //get the document and the layer
      val doc : DXFDocument = parser.getDocument()
      val layer : DXFLayer = doc.getDXFLayer(layerid)
      //get all polylines from the layer
      val plines : List[_] = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE)
      //work with the first polyline
      input.close()
      Some(plines)
      //doSomething((DXFPolyline) plines.get(0));
      } catch {case e => {
      input.close()
      println("found error: "+ e)
      None
      }
    }
  }
}
