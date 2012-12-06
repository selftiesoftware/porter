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

package com.siigna.module.porter

import com.siigna.module.Module
import java.awt.{FileDialog, Frame, Color}
import com.siigna._
import app.Siigna
import scala.Some
import java.io.File
import com.siigna.module.porter.DXF._

/**
 * An import module for Siigna.
 * Currently supports the following file types:
 *
 *  -- DXF
 *  --
 *
 */

class Import {
  lazy val anthracite = new Color(0.25f, 0.25f, 0.25f, 1.00f)
  val color = "Color" -> "#AAAAAA".color

  val frame = new Frame
  var fileLength: Int = 0

  //graphics to show the loading progress
  def loadBar(point: Int): Shape = PolylineShape(Rectangle2D(Vector2D(103, 297), Vector2D(point + 103, 303))).setAttribute("raster" -> anthracite)
  def loadFrame: Shape = PolylineShape(Rectangle2D(Vector2D(100, 294), Vector2D(500, 306))).setAttribute(color)

  private var startTime: Option[Long] = None

  def importer = {

    try {
      //opens a file dialog
      val dialog = new FileDialog(frame)
      dialog.setVisible(true)

      val fileName = dialog.getFile
      val fileDir = dialog.getDirectory
      val file = new File(fileDir + fileName)

      // Can we import the file-type?
      val extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase

      //DXF IMPORT
      if (extension == "dxf") {
        //these two lines are needed to draw a loading bar
        startTime = Some(System.currentTimeMillis())
        //TODO: find the correct scaling factor to make loading bar fit large DXF files.
        fileLength = file.length().toInt * 4

        // Import!
        //todo: when a paper stack is implemented in Siigna, then import each layer to its own paper.
        val readDXF = new DXFExtractor

        //todo: find all layers in the DXF file generically and import them.
        //readDXF.read(file, "0")
        //readDXF.read(file, "default")
        readDXF.read(file, "Default")


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
