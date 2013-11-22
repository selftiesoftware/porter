package com.siigna.module

import javax.swing.filechooser.FileNameExtensionFilter

/**
 * A package for porting files in and out of Siigna.
 */
package object porter {

  /**
   * A file name filter for .pdf files.
   */
  lazy val PDFFileFilter = new FileNameExtensionFilter("PDF files", "pdf")

  /**
   * A file name filter for .pdf files.
   */
  lazy val DXFFileFilter = new FileNameExtensionFilter("DXF files", "dxf")

  /**
   * A file name filter for .jpg files.
   */
  lazy val JPGFileFilter = new FileNameExtensionFilter("JPG files", "jpg")

}
