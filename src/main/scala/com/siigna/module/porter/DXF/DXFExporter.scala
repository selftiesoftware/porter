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

package com.siigna.module.porter.DXF

import java.io.OutputStream
import com.siigna.app.model.Drawing
import io.Codec
import com.siigna.app.model.shape.Shape
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.InputStream
import java.io.ByteArrayInputStream

/**
 * Exports the current drawing in DXF format.
 */
object DXFExporter extends (OutputStream => Unit) {

  //save a given selection to the computer's  clipboard (used by cad-suite/Copy and Paste to copy artwork between drawings)

  def toDXFtoClipboard(shape : Map[Int,Shape]) {
    val dxf = new DXFFile

    dxf ++ Drawing.map(t => DXFSection.toDXF(t._2)).toSeq

    val clip = Toolkit.getDefaultToolkit.getSystemClipboard
    val s : StringSelection = new StringSelection(dxf.toString)

    clip.setContents(s,s)
  }

  def apply(out : OutputStream) {
    var dxf = new DXFFile

    dxf ++ Drawing.map(t => DXFSection.toDXF(t._2)).toSeq
    var charSeq = Codec.toUTF8(dxf.toString)

    // Write
    out.write(charSeq)
    out.close()
  }

  def extension = "dxf"
}
