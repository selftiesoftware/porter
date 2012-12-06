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
import DXF._
import java.awt.Frame
import java.awt.FileDialog
import java.awt.Color
import scala.Some
import java.io.File

/**
 * An import module for Siigna.
 * Currently supports the following file types: DXF
 */

class Import {
  val frame = new Frame

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

        // Import! TODO: when a paper stack is implemented in Siigna, then import each layer to its own paper.
        val readDXF = new DXFExtractor

        readDXF.read(file)

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
