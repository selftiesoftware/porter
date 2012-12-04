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

package com.siigna.module.porter.PDF.contents

import java.util.Date
import com.siigna.app.Siigna
import com.siigna.module.porter.PDF
import PDF.PDF

/**
 * methods that generate various sections of the PDF file.
 */
object OBJsections {

  //document properties
  val author = None
  var buffer : Array[String] = Array()
  val creator = None
  var fontNumber = 1 // TODO: This is temp, replace with real font handling
  var k = 1.0 // Scale factor
  val keywords = None
  var lineWidth = 0.200025 // 2mm
  var objectNumber = 1 // "n" Current object number
  var offsets = Array() // List of offsets
  var pageHeight = 595.28
  var pdfVersion = 1.3 // PDF Version
  var page = 0
  var pages = Array()
  var pageWidth = 841.89
  var state = 0 // Current document state
  val subject = None
  val title = None
  val version = Siigna.version

  //write to buffer
  def out(s : String) = {
    println("running def out with string: "+s)
    if(state == 2) {
      //pages[page] += s + "\n"
      //pages += s + "\n"
      Array()
    } else {
      buffer.toArray + (s + "\n")
    }
  }

  //create a header
  def putHeader = out("%PDF-" + pdfVersion)

  //Start a new object entry
  def newObject = {
    println("running newObject")
    //Begin a new object
    objectNumber += 1
    offsets(objectNumber) == buffer.length
    out(objectNumber.toString + " 0 obj")
  }

  // TODO: Fix, hardcoded to a4 portrait
  def putPages = {
    println("running putPages")
    var wPt = pageWidth * k
    var hPt = pageHeight * k

    for(n <- 0 to page) {
      newObject
      out("<</Type /Page")
      out("/Parent 1 0 R")
      out("/Resources 2 0 R")
      out("/Contents " + (objectNumber + 1) + " 0 R>>")
      out("endobj")

      //Page content
      var p = pages(n)
      newObject
      //out("<</Length " + p.length  + ">>");   //p.length does not work
      out("<</Length " + p.toString.length  + ">>")

      putStream(p)
      out("endobj")
    }
    offsets(1) == buffer.length
    out("1 0 obj")
    out("<</Type /Pages")
    var kids="/Kids ["
    for (i <- 0 to page) kids += (3 + 2 * i) + " 0 R "
    out(kids + "]")
    out("/Count " + page)
    out(format("/MediaBox [0 0 %.2f %.2f]", wPt, hPt))
    out(">>")
    out("endobj")
  }

  def putStream (s : String) = {
    out("stream")
    out(s)
    out("endstream")
  }

  // TODO: allow placement of Xobjects
  def putXobjectDict = {
    println("adding xObjects not implemented")
  }

  def putResourceDictionary = {
    out("/ProcSet [/PDF /Text /ImageB /ImageC /ImageI]")
    out("/Font <<")
    // Do this for each font, the "1" bit is the index of the font
    // fontNumber is currently the object number related to "putFonts"
    out("/F1 " + fontNumber + " 0 R")
    out(">>")
    out("/XObject <<")
    putXobjectDict
    out(">>")
  }

  // ADD IMAGES HERE...
  var putImages = {
    println("adding images to PDF is not possible yet")
  }

  def putFonts = {
    println("runnin gputFonts")
    // TODO: Only supports core font hardcoded to Helvetica
    newObject
    fontNumber = objectNumber
    val name = "Helvetica"
    out("<</Type /Font")
    out("/BaseFont /" + name)
    out("/Subtype /Type1")
    out("/Encoding /WinAnsiEncoding")
    out(">>")
    out("endobj")
  }

  def putResources = {
    putFonts
    putImages

    //Resource dictionary
    offsets(2) == buffer.length
    out("2 0 obj")
    out("<<")
    putResourceDictionary
    out(">>")
    out("endobj")
  }

  // Escape text TODO: ACTIVATE THIS??
  def pdfEscape (s : String) = {
    s
    //text.replace(/\\/g, "\\\\").replace(/\(/g, "\\(").replace(/\)/g, "\\)");
    //text.replace(/\\/g, "\\\\").replace(/\(/g, "\\(").replace(/\)/g, "\\)");
  }

  def putInfo = {
    out("/Producer (SIIGNA " + version + ")")
    if(title.isDefined) {
      out("/Title (" + pdfEscape(title.get) + ")")
    }
    if(subject.isDefined) {
      out("/Subject (" + pdfEscape(subject.get) + ")")
    }
    if(author.isDefined) {
      out("/Author (" + pdfEscape(author.get) + ")")
    }
    if(keywords.isDefined) {
      out("/Keywords (" + pdfEscape(keywords.get) + ")")
    }
    if(creator.isDefined) {
      out("/Creator (" + pdfEscape(creator.get) + ")")
    }
    //TODO: use non-deprecated scale date and time classes.
    val created = new Date()
    val year = created.getYear
    val month = (created.getMonth() + 1)
    val day = created.getDate()
    val hour = created.getHours()
    val minute = created.getMinutes()
    val second = created.getSeconds()
    out("/CreationDate (D:" + format("%02d%02d%02d%02d%02d%02d", year, month, day, hour, minute, second) + ")")
  }

  def putCatalog =  {
    out("/Type /Catalog")
    out("/Pages 1 0 R")
    // TODO: Add zoom and layout modes
    out("/OpenAction [3 0 R /FitH null]")
    out("/PageLayout /OneColumn")
  }

  def putTrailer = {
    out("/Size " + (objectNumber + 1))
    out("/Root " + objectNumber + " 0 R")
    out("/Info " + (objectNumber - 1) + " 0 R")
  }
}
