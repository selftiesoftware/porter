package com.siigna.module.porter.PDF

import com.siigna.module.porter._
import java.io.OutputStream
import com.siigna.app.model.Drawing
import io.Codec

/**
 * Exports the current drawing in PDF format.
 */
object PDFExporter {

  def apply(out : OutputStream) {
    val pdf = new PDFFile
    var contents = pdf.output(Some("datauri"))
    var charSeq = Codec.toUTF8(contents)

    // Write
    out.write(charSeq)
    out.close()
    println("CONTENTS LENGTH "+contents.length)
    println("PDF "+pdf)
    contents = ""
    println("CONTENTS LENGTH2 "+contents.length)

    charSeq = Array()
  }

  def extension = "pdf"
}

