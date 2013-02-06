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
    val contents = pdf.output(Some("datauri"))
    val charSeq = Codec.toUTF8(contents.toString)

    // Write
    out.write(charSeq)
  }

  def extension = "pdf"
}

