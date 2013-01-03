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

package com.siigna.module

import com.siigna._
import java.awt.Color
import com.siigna.module.porter._

/**
 * a class for an Input-output dialog, if it is needed
 */

class ModuleInit extends Module {
  val t = TransformationMatrix
  val boundary = View.screen
  val DXFtopRight = Vector2D(boundary.bottomLeft.x + 38 , boundary.bottomLeft.y + 4)
  val DXFbottomLeft = Vector2D(DXFtopRight.x+20,DXFtopRight.y+20)

  val PDFtopRight = Vector2D(boundary.bottomLeft.x + 68 , boundary.bottomLeft.y + 4)
  val PDFbottomLeft = Vector2D(PDFtopRight.x+20,PDFtopRight.y+20)

  def isImport : Boolean = {
    if((DXFbottomLeft.x > mousePosition.x && DXFbottomLeft.y > mousePosition.y) && ((DXFtopRight.x < mousePosition.x && DXFtopRight.y < mousePosition.y))) true
 else false
  }

  def isExport : Boolean ={
    if((PDFbottomLeft.x > mousePosition.x && PDFbottomLeft.y > mousePosition.y) && ((PDFtopRight.x < mousePosition.x && PDFtopRight.y < mousePosition.y))) true
  else false
  }

  def stateMap = Map(
    'Start -> {
      case MouseDown(p, MouseButtonLeft, _) :: tail => {
        if (isImport == true) {
          //instantiate the Import class
          val importClass = new Import
          //run the importer method
          importClass.importer
          End
        } else if (isExport == true) {
          //instantiate the Export class
          val exportClass = new Export
          //run the exporter method with PDF extension
          exportClass.exporter("pdf")
          End
        }
      }
      case MouseMove(p, _ , _) :: tail =>
      case _ => None
    }
  )
  override def paint(g : Graphics, t : TransformationMatrix) {
    def drawIcon = {
     //draw a load icon:
      g draw PolylineShape(Rectangle2D(DXFtopRight, DXFbottomLeft)).setAttributes("Color" -> new Color(0.25f, 0.85f, 0.25f, 0.80f), "StrokeWidth" -> 2.0)
      g draw PolylineShape(Rectangle2D(PDFtopRight, PDFbottomLeft)).setAttributes("Color" -> new Color(0.85f, 0.25f, 0.25f, 0.80f), "StrokeWidth" -> 2.0)
    }
    drawIcon
  }
}