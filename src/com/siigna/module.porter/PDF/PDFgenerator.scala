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

import com.siigna.app.Siigna
import java.util.Date
import sun.misc.BASE64Encoder


/**
 * PDFexport is adapted from:
 * jsPDF
 * (c) 2009 James Hall
 *
 * Some parts based on FPDF.
 */
//TODO: implement lines, rectangles, arcs, and circles generation as done in https://github.com/MrRio/jsPDF/blob/master/jspdf.js
class PDFgenerator {
  println("in PDF generator")
  def scalaPDF (orientation : String, unit :String, PDFFormat : String) : String = {

    // Private properties
    val version = Siigna.version
    var buffer : Array[String] = Array()

    var pdfVersion = 1.3 // PDF Version
    var defaultPageFormat = "a4"
    def pageFormats = { // Size in mm of various paper formats
    val a3 = (841.89,1190.55)
      def a4 = (595.28, 841.89)
      def a5 = (420.94, 595.28)
      def letter = (612, 792)
      def legal = (612, 1008)
    }

    var textColor = "0 g"
    var page = 0
    var objectNumber = 2 // "n" Current object number
    var state = 0 // Current document state
    var pages = Array()
    var offsets = Array() // List of offsets
    var lineWidth = 0.200025 // 2mm
    var pageHeight = 595.28
    var pageWidth = 841.89
    var k = 1.0 // Scale factor
    var unit = "mm" // Default to mm for units
    var fontNumber = 1 // TODO: This is temp, replace with real font handling

    //document properties
    val title = None
    val subject = None
    val author = None
    val keywords = None
    val creator = None


    var fontSize = 12 // Default font size
    var pageFontSize = 12

    // Initilisation
    if (unit == "pt") {
      k = 1
    } else if(unit == "mm") {
      k = 72/25.4
    } else if(unit == "cm") {
      k = 72/2.54
    } else if(unit == "in") {
      k = 72
    }

    //write to buffer?
    def out(s : String) = {
      if(state == 2) {
        //pages[page] += s + "\n"
        //pages += s + "\n"
        Array()
      } else {
        buffer.toArray + (s + "\n")
      }
    }

    // Private functions
    def newObject = {
      //Begin a new object
      objectNumber += 1
      offsets(objectNumber) == buffer.length
      out(objectNumber.toString + " 0 obj")
    }

    //create a header
    val putHeader = out("%PDF-" + pdfVersion)

    // TODO: Fix, hardcoded to a4 portrait
    def putPages = {
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

    // TODO: Loop through images
    var putXobjectDict = {
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
    }

    def putFonts = {
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

    var putResources = {
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

    var putCatalog =  {
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

    def endDocument = {
      state = 1
      putHeader
      putPages

      putResources
      //Info
      newObject
      out("<<")
      putInfo
      out(">>")
      out("endobj")

      //Catalog
      newObject
      out("<<")
      putCatalog
      out(">>")
      out("endobj")

      //Cross-ref
      var o = buffer.length
      out("xref")
      out("0 " + (objectNumber + 1))
      out("0000000000 65535 f ")
      for (i <- 0 to objectNumber) {
        out(format("%010d 00000 n ", offsets(i)))
      }
      //Trailer
      out("trailer")
      out("<<")
      putTrailer
      out(">>")
      out("startxref")
      //out(o) //type should be string?
      out("o")
      out("%%EOF")
      state = 3
    }

    val beginPage = {
      page += 1
      // Do dimension stuff
      state = 2
      //pages(page) = ""
      //pages.update(page, _)

      // TODO: Hardcoded at A4 and portrait
      pageHeight = 841 / k
      pageWidth = 596 / k
    }

    var _addPage = {
      beginPage
      // Set line width
      out(format("%.2f w", (lineWidth * k)))

      //TODO:set font.
      // 12 is the font size
      pageFontSize = fontSize
      out("BT /F1 " + fontSize + ".00 Tf ET")
    }

    // Add the first page automatically
    _addPage

    return {
      def addPage = {
        _addPage
      }
      def text(x : Int, y : Int, text : String) = {
        // need page height
        if(pageFontSize != fontSize) {
          out("BT /F1 " + fontSize + ".00 Tf ET")
          pageFontSize = fontSize
        }
        var str = format("BT %.2f %.2f Td (%s) Tj ET", x * k, (pageHeight - y) * k, pdfEscape(text))
        out(str)
      }
      //ADD IMAGES HERE
      //addImage: function(imageData, format, x, y, w, h) {
      "test"
    }

    def output(inputType : Option[String]) = {
      endDocument
      if(inputType == None) {
        buffer
      }
      if(inputType == Some("datauri")) {
        var encodedBuffer =new BASE64Encoder().encode(buffer.toString.getBytes())
        var document = "c:/siigna/pdf;base64," + encodedBuffer
        document
      }
      //TODO: Add different output options
    }
    def setFontSize(size : Int) =  {
      fontSize = size
    }
    "test2"
  }
}

