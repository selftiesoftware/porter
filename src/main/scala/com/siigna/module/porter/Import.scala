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

import com.siigna._
import DXF._
import JPG._

/**
 * An import module for Siigna.
 * Currently supports the following file types: DXF
 */

class Import extends Module {

  var placeJPG = false
  var goOut = false

  val stateMap: StateMap = Map(
    'Start -> {
      case e => {
        if(goOut == false) { //TODO: very ugly hack preventing the dialog from opening again after image import.
          Siigna display "Opening import dialog..."

          //Dialogue.readInputStream(Map(DXFFileFilter -> DXFImport))
          val a = Dialogue.readInputStream(DXFFileFilter,JPGFileFilter)
          //try the available parsers

          //TODO: run both parsers, and be sure the JPG image in JGPImport is not set if it is not used
          //a.map(DXFImport.apply)
          a.map(JPGImport.apply) //currently only JPGs are evaluated. // CHANGE THIS!!!

          //exit the dialogue and goto the module in which the background can be placed:
          if(Siigna.imageBackground._1.isDefined) {
            goOut = true
            Start('cad,"file.ImageBackground")
          }
          else {
          //zoom extends
          View.zoomExtends
          End
          }
        } else End
      }
    }
  )
}
