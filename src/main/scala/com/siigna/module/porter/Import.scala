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
        if (goOut == false) {
          //TODO: very ugly hack preventing the dialog from opening again after image import.
          Siigna display "Opening import dialog..."

          //Dialogue.readInputStream(Map(DXFFileFilter -> DXFImport))
          val a = Dialogue.readInputStream(DXFFileFilter, JPGFileFilter)
           //read the file extension and use it to call the correct parser
           if((a.get._1 takeRight 3) == "dxf" || (a.get._1 takeRight 3) == "DXF") a.map(t => DXFImport(t._2))
           if((a.get._1 takeRight 3) == "jpg" || (a.get._1 takeRight 3) == "JPG") a.map(t => JPGImport(t._2))

          //exit the dialogue and goto the module in which the background can be placed:
          if (Siigna.imageBackground._1.isDefined) {
            goOut = true
            Start('cad, "file.ImageBackground")
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
