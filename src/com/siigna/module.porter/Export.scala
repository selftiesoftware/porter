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
import java.awt.event.{ActionEvent, ActionListener}

//import java.awt.{FileDialog, Frame}
import com.siigna._
import io.Codec
import PDF.PDFFile
import scala.Some
import java.io.FileOutputStream
import javax.swing._
import filechooser._
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

class Export {

  // The exporters that can export a given extension (string)
  private var frameIsLoaded: Boolean = false

  def exporter(extension : String) = {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    //create a testshape used to evaluate scaling and positioning on the PDF page. //TODO: add a test
    //Create(PolylineShape(List(Vector2D(0,0),Vector2D(200,100),Vector2D(20,110),Vector2D(20,30))))
    //Create(TextShape("testing write text to PDF", Vector2D(50,50),10))

    //initiate the export dialog
    val showSaveFileDialog = {

      val fileChooser = new JFileChooser()

      //set file filters
      val filterDXF = new FileNameExtensionFilter("DXF Documents", "dxf", "DXF")
      val filterPDF = new FileNameExtensionFilter("PDF Documents", "pdf", "PDF")

      fileChooser.setDialogTitle("Export Siigna paper contents")
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
      fileChooser.setFileFilter(filterPDF)
      fileChooser.setFileFilter(filterDXF)
      fileChooser.setAcceptAllFileFilterUsed(true)

      //check if the resulting filetype is accepted
      val result : Int = fileChooser.showSaveDialog(fileChooser)
      val ext = fileChooser.getFileFilter.getDescription

      if (result == JFileChooser.APPROVE_OPTION) {
        var f = fileChooser.getSelectedFile()
        val filepath = f.getPath

        if(ext == "PDF Documents") {
          if(!filepath.toLowerCase.endsWith("pdf")) f = new File(filepath + ".pdf") //make sure the file has the right extension
          val output = new FileOutputStream(f)
          val PDFdoc = new PDFFile // instantiate the PDF clas
          val contents = PDFdoc.output(Some("datauri"))

          output.write(Codec.toUTF8(contents))
          output.flush()// Flush and close
          output.close()

      } else if (ext == "DXF Documents") {
          if(!filepath.toLowerCase.endsWith("dxf")) f = new File(filepath + ".dxf") //make sure the file has the right extension
          // Fetch the output stream
        val output = new FileOutputStream(f)

          try {
          DXFExporter.apply(output)  //export!
          output.flush()// Flush and close
          output.close()
        } catch {
          case e => Siigna display "DXF export failed: "+ e
        }
      }
      Siigna display "Export successful."
      }
    }
    showSaveFileDialog  //run the export dialog
  }
}