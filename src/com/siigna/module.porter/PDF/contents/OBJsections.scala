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

package com.siigna.module.porter.PDF.contents

import java.util.Date
import com.siigna.app.Siigna
import com.siigna.module.porter.PDF
import PDF.PDFFile
import com.siigna.app.view.View
import com.siigna.app.model.Drawing

/**
 * methods that generate various sections of the PDF file.
 */
object OBJsections {

  //document properties
  val author = None
  var buffer : List[String] = List()
  val creator = None
  var fontNumber = 1 // TODO: This is temp, replace with real font handling
  var pageScale = 1.0 // Scale factor
  val keywords = None
  var lineWidth = 0.2 // 0.2 mm
  var objectNumber : Int = 0 // "n" Current object number
  var offsets : List[Int] = List() // List of offsets
  var pdfVersion = "1. 4" // PDF Version
  var page = 1
  //var pages = Array()
  var state = 0 // Current document state
  val subject = None
  val title = None
  val version = Siigna.string("version").getOrElse("")

  //write to buffer
  def out(s : String) = {
    if(state == 2) {
      //pages[page] += s + "\n"
      //pages += s + "\n"
      //Array()
    } else {
      buffer = (s + "\n") :: buffer
    }
  }

  def catalog =  {
    out("<</Type /Catalog")
    out("/Outlines 2 0 R")
    out("/Pages 3 0 R")
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
  def PDFheader = {
    out("%PDF exported from www.siigna.com - online graphics app and library")
    out("%PDF-" + pdfVersion)
  }

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
    out("<</Type Outlines")
    out("/Count 0")
    out(">>")
    out("endobj")
  }

  def pageSize : (Int, Int, Boolean) = {
    if(Drawing.boundary.width > Drawing.boundary.height) {
      (841,597, true)
    } else {
      (597,841, false)
    }
  }

  // TODO: Fix, hardcoded to a4 portrait
  def pageDefinition = {
    var wPt = pageSize._1 * pageScale
    var hPt = pageSize._2 * pageScale

    for(n <- 1 to page) {
      out("<</Type /Pages")
      //var kids="/Kids ["
      //for (i <- 0 to page) kids += (3 + 2 * i) + " 0 R "
      //out(kids + "]")
      out("/Kids [ 4 0 R ]")
      out("/Count " + page)
      out(">>")

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
    out("/Contents " + (objectNumber + 1) + " 0 R")
    out("/Resources << /ProcSet 6 0 R>>")
    out(">>")
    out("endobj")
  }

  // TODO: allow placement of Xobjects
  def XobjectDict = {
    println("adding xObjects not implemented")
  }

  //TODO: implement info
  def info = {
    out("/Producer (SIIGNA " + version + ")")
    if(title.isDefined) {
      out("/Title (" + title.get + ")")
    }
    if(subject.isDefined) {
      out("/Subject (" + subject.get + ")")
    }
    if(author.isDefined) {
      out("/Author (" + author.get + ")")
    }
    if(keywords.isDefined) {
      out("/Keywords (" + keywords.get + ")")
    }
    if(creator.isDefined) {
      out("/Creator (" + creator.get + ")")
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
    //out("/Root " + objectNumber + " 0 R")
    out("/Root 1 0 R")
    //out("/Info " + (objectNumber - 1) + " 0 R")
  }
}
