package com.siigna.module.porter.PDF

import java.io.{File, FilenameFilter}

/**
 * A file name filter for .pdf files.
 */
object PDFFilenameFilter extends FilenameFilter {

  def accept(dir: File, name: String): Boolean = dir.getName.endsWith(".pdf")

}
