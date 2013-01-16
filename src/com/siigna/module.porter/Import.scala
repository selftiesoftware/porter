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
import javax.swing.{JFileChooser, UIManager}
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * An import module for Siigna.
 * Currently supports the following file types: DXF
 */

class Import extends Module {

  val frame = new Frame

  def importer = {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

      //initiate the export dialog
      val showSaveFileDialog = {

        val fileChooser = new JFileChooser()

        //set file filters
        val filterDXF = new FileNameExtensionFilter("DXF Documents", "dxf", "DXF")

        fileChooser.setDialogTitle("Export Siigna paper contents")
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
        fileChooser.setFileFilter(filterDXF)
        fileChooser.setAcceptAllFileFilterUsed(true)

        //check if the resulting filetype is accepted
        val result : Int = fileChooser.showSaveDialog(fileChooser)
        val ext = fileChooser.getFileFilter.getDescription

        if (result == JFileChooser.APPROVE_OPTION) {

          var f = fileChooser.getSelectedFile()

          //DXF IMPORT
          if(ext == "DXF Documents") {
            com.siigna.app.view.View.isImporting = true //disable drawing for performance purposes
            // Import! TODO: when a paper stack is implemented in Siigna, then import each layer to its own paper.
            val readDXF = new DXFExtractor

            readDXF.read(f)

            Siigna display "Loading completed."
          } else Siigna display "please select a .dxf file"
        }
      }
    showSaveFileDialog //import!
    } catch {
      case e => Siigna display "Import cancelled."
    }
  com.siigna.app.view.View.isImporting = false //enable drawing

  End
  }
  val stateMap: StateMap = Map(
    'Start -> {
      case _ => {
        //instantiate the Import class
        val importClass = new Import
        //run the importer method
        importClass.importer
        End
      }

    }
  )
}
