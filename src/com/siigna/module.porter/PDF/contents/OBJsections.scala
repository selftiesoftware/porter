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
  var buffer : List[String] = List()
  val creator = None
  var fontNumber = 1 // TODO: This is temp, replace with real font handling
  var k = 1.0 // Scale factor
  val keywords = None
  var lineWidth = 0.200025 // 2mm
  var objectNumber : Int = 0 // "n" Current object number
  var offsets : List[Int] = List() // List of offsets
  var pageHeight = 595.28
  var pageWidth = 841.89
  var pdfVersion = 1.4 // PDF Version
  var page = 1
  var pages = Array()
  var state = 0 // Current document state
  val subject = None
  val title = None
  val version = Siigna.version

  //write to buffer
  def out(s : String) = {
    if(state == 2) {
      //pages[page] += s + "\n"
      //pages += s + "\n"
      Array()
    } else {
      buffer = (s + "\n") :: buffer
    }
  }

  def catalog =  {
    out("<</Type /Catalog")
    out("Outlines 2 0 R")
    out("Pages 3 0 R")
    out(">>")
    out("endobj")

  }

  def fonts = {
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

  // ADD IMAGES HERE...
  def images = {
    println("adding images to PDF not implemented")
  }

  //create a header
  def header = out("%PDF-" + pdfVersion)

  //Start a new object entry
  def newObject = {
    //Begin a new object
    objectNumber += 1
    offsets = offsets :+ buffer.length
    //offsets(objectNumber) = buffer.size //TODO: this currently crashes the exporter -- should be fixed
    out(objectNumber.toString + " 0 obj")
  }


  //write offsets: (a series of numbers pr. object describing the size of the buffer)
  def offsetDefinition = {
    for(i <- 0 to objectNumber -1) out(String.format("%010d 00000 n", new java.lang.Integer(offsets(i))))
  }


  def outlines =  {
    out("<</Type /Outlines")
    out("/Count 0")
    out(">>")
    out("endobj")
  }

  // TODO: Fix, hardcoded to a4 portrait
  def pageDefinition = {
    var wPt = pageWidth * k
    var hPt = pageHeight * k

    for(n <- 0 to page) {
      out("<</Type /Pages")
      var kids="/Kids ["
      for (i <- 0 to page) kids += (3 + 2 * i) + " 0 R "
      out(kids + "]")
      out("/Count " + page)

      //Page content
      //var p = pages(n) //TODO: setting var causes crash
      //newObject //TODO: running newObject stops the process. fix it!
      //out("<</Length " + p.length  + ">>");   //p.length does not work
      //out("<</Length " + p.toString.length  + ">>")

      //putStream(p)
      out("endobj")
    }

    //offsets(1) = buffer.length //TODO: nt possible. fix it!
    newObject
    out("<</Type /Page")
    out("/Parent 3 0 R")
    out("/MediaBox [0 0 "+ wPt +" " + hPt + "]")
    out("/Contents " + (objectNumber + 1) + " 0 R>>")
    out("/Resources << /ProcSet 6 0 R>>")
    out(">>")
    out("endobj")
  }

  // Escape text TODO: reimplement the replace method
  def pdfEscape (s : String) = {
    s
    //text.replace(/\\/g, "\\\\").replace(/\(/g, "\\(").replace(/\)/g, "\\)");
    //text.replace(/\\/g, "\\\\").replace(/\(/g, "\\(").replace(/\)/g, "\\)");
  }

  def resourceDictionary = {
    out("/ProcSet [/PDF /Text /ImageB /ImageC /ImageI]")
    out("/Font <<")
    // Do this for each font, the "1" bit is the index of the font
    // fontNumber is currently the object number related to "putFonts"
    out("/F1 " + fontNumber + " 0 R")
    out(">>")
    out("/XObject <<")
    XobjectDict
    out(">>")
  }

  def resources = {
    fonts
    //putImages

    //Resource dictionary
    //offsets(2) == buffer.length //TODO: activate this
    out("<<")
    resourceDictionary //TODO: activate this
    out(">>")
    out("endobj")
  }

  //TODO: not implemented
  def stream = {
    out("<</Length 35>>")
    out("stream")
    out("… Page-marking operators …")
    out("endstream")
  }

  // TODO: allow placement of Xobjects
  def XobjectDict = {
    println("adding xObjects not implemented")
  }

  def info = {
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

  def trailer = {
    out("/Size " + (objectNumber + 1))
    out("/Root " + objectNumber + " 0 R")
    //out("/Info " + (objectNumber - 1) + " 0 R")
  }
}
