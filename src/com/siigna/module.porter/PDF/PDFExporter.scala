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

import com.siigna.module.porter._
import java.io.OutputStream
import io.Codec

/**
 * Exports the current drawing in PDF format.
 */

object PDFExporter extends Exporter {
  def apply(out : OutputStream) {

    // TODO: Optimize this!
    //val charSeq = Codec.toUTF8(dxf.toString)
    // Write
    //out.write(Codec.toUTF8(contents))
  }

  def extension = "pdf"
}
