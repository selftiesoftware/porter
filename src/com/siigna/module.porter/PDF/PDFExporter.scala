package com.siigna.module.porter.PDF

import com.siigna.module.porter._
import java.io.OutputStream
import com.siigna.app.model.Drawing
import io.Codec

/**
 * Exports the current drawing in PDF format.
 */
object PDFExporter extends (OutputStream => Unit) {

  var pdf: Option[PDFFile] = None

  def apply(out : OutputStream) {
    //clear old vars in OBJSections:
    com.siigna.module.porter.PDF.contents.OBJsections.buffer = List()
    com.siigna.module.porter.PDF.contents.OBJsections.state = 0
    com.siigna.module.porter.PDF.contents.OBJsections.objectNumber = 0

    pdf = Some(new PDFFile)
    var contents = pdf.get.output(Some("datauri"))
    var charSeq = Codec.toUTF8(contents)

    // Write
    out.write(charSeq)
    out.close()
    println("CONTENTS LENGTH "+contents.length)
    println("PDF "+pdf)
    contents = ""
    println("CONTENTS LENGTH2 "+contents.length)
    pdf = None
    charSeq = Array()
  }

  def extension = "pdf"
}

