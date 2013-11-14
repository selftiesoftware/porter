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
import com.siigna.module.porter.PDF.PDFExporter
import com.siigna.module.porter.DXF.DXFExporter

/**
 * An export module for Siigna.
 * Currently supports PDF and DXF file types
 */
class Export extends Module {

  val stateMap: StateMap = Map(
    'Start -> {
      case _ => {
        Siigna display "OPENING EXPORT DIALOG..."

        // Write the DXF or PDF exporter to a file
        val a = Dialogue.writeOutputStream(Map(PDFFileFilter -> new PDFExporter, DXFFileFilter -> DXFExporter))

        if (a) {
          val s = Drawing.size
          Siigna display "EXPORTED "+s+" SHAPE(S)"

        } else Siigna display "EXPORTED FAILED"
        Thread.sleep(2000)
        End
      }

    }
  )
}