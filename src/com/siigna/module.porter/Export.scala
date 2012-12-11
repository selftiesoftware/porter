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

import DXF.DXFExporter
import java.awt.{FileDialog, Frame}
import com.siigna._
import io.Codec
import PDF.PDFFile
import scala.Some
import java.io.FileOutputStream

class Export {

  // The exporters that can export a given extension (string)
  private var frameIsLoaded: Boolean = false

  def exporter(extension : String) = {
    //create a testshape used to evaluate scaling and positioning on the PDF page. //TODO: add a test
    Create(PolylineShape(List(Vector2D(0,0),Vector2D(200,100),Vector2D(20,110),Vector2D(20,30))))
    //Create(TextShape("testing write text to PDF", Vector2D(50,50),10))

    //call relevant exported on the basis of the typed-in file extension:
    def getFileType(f : String) : String = {
      if(f.toLowerCase.contains("dxf")) "dxf"
      else if(f.contains("pdf"))"pdf"
      else ""
    }


    try {
      val frame = new Frame()
      val dialog = new FileDialog(frame, "Export to file", FileDialog.SAVE)
      dialog.setVisible(true)

      val directory = dialog.getDirectory
      val filename = dialog.getFile

      if (getFileType(filename) == "pdf" ) {
        // Fetch the output stream
        val output = new FileOutputStream(directory + filename)
        val PDFdoc = new PDFFile // instantiate the PDF class

        val contents = PDFdoc.output(Some("datauri"))
        output.write(Codec.toUTF8(contents))

        // Flush and close
        output.flush()
        output.close()
      } else if (getFileType(filename) == "dxf") {
        // Fetch the output stream
        val output = new FileOutputStream(directory + filename)
        try {
          DXFExporter.apply(output)  //export!

        } catch {
          case e => Siigna display "DXF export failed: "+ e
        }
      }

      dialog.dispose()
      frame.dispose()

      Siigna display "Export successful."

    } catch {
      case e => Siigna display "Export cancelled."
    }
  }
}