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

package com.siigna.module.porter.PDF

import com.siigna._

class PDFExport extends Module {

  def stateMap = Map(
    'Start -> {
      case _ => {

        //instantiate the PDF class:
        val PDFdoc = new PDF

        //add text to the buffer:
        PDFdoc.text(20, 20, "TESTING:")
        PDFdoc.text(20, 30, "first export of SIIGNA PDF exporter!")
        //doc.addPage
        //doc.text(20, 20, "testing page 2")

        // Initiate the output creation:
        PDFdoc.output(Some("datauri"))
        End
      }
    }
  )
}