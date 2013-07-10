package com.siigna.module.porter.PDF


import java.io.{FileOutputStream, OutputStream}
import com.siigna.app.model.Drawing
import com.siigna.util.geom.TransformationMatrix
import com.siigna.app.model.shape._
import com.itextpdf.text.{PageSize, Document}
import com.itextpdf.text.pdf.PdfWriter
import com.siigna.app.Siigna
import com.siigna.util.geom.Vector2D

/**
 * Exports the current drawing in PDF format.
 * For help and other examples; see http://itextpdf.com/book/examples.php
 */
object PDFExporter extends (OutputStream => Unit) {
  var mm = 72/25.4
  def apply(out : OutputStream) {
    val orientation = if (pageSize._3) PageSize.A4.rotate() else PageSize.A4
    if (pageSize._3) println("landscape") else println("portrait")
    var document = new Document(orientation)

    //TODO make the definition of landscape or portrait (PageSize.A4 or PageSize.A4.rotate)
    var writer =PdfWriter.getInstance(document, out)
    document.open()
    var canvas = writer.getDirectContent

    shapesEvaluation

    document.close()
    def shapesEvaluation = {
      if(Drawing.shapes.size != 0) {
        val shapes = Drawing.shapes.map (t => (t._2))

        shapes.foreach(s =>
          s match {
            case c : CircleShape => {
              canvas.saveState()
              val center =rePos(c.center)
              canvas.circle(center.x.toFloat,center.y.toFloat,c.radius.toFloat)
              canvas.stroke();
              canvas.restoreState()
            }
            case l : LineShape => {
              canvas.saveState()
              canvas.moveTo(l.p1.x.toFloat,l.p1.y.toFloat)
              canvas.lineTo(l.p2.x.toFloat,l.p2.y.toFloat);
              canvas.stroke();
              canvas.restoreState()
              // line(rePos(l.p1),rePos(l.p2),l.attributes)
            }
            case o : PolylineShape.PolylineShapeOpen => {
              val points = o.innerShapes
              canvas.saveState()

              canvas.restoreState()
                      //TODO: Implement in iText
              //openPolyline(rePos(o.startPoint), points, o.attributes)
            }

            case p : PolylineShape.PolylineShapeClosed => {
              val points = p.innerShapes
              //TODO: Implement in iText
              //closedPolyline(rePos(p.startPoint), points, p.attributes)
            }
            case t : TextShape => {

              val content = t.text
              val pos = t.position
              val size = t.fontSize.toInt
              canvas.saveState()
              canvas.beginText()
              canvas.moveText(pos.x.toFloat,pos.y.toFloat)
              //canvas.setFontAndSize(//)
              //TODO: Implement in iText
              //canvas.
              //text(rePos(pos), content, size)
            }
            case a : ArcShape => {
              //TODO: Implement in iText
            }
            case e => println("no match on shapes: "+shapes)
          }
        )
      } else {
        println("no lines in the drawing")
        None
      }

    }

  }

  def extension = "pdf"
  //TODO: test if scaling is right across different zoom scales
  def rePos(v : Vector2D) = {
    //find the bounding box of the drawing
    val box = Drawing.boundary.center
    val landscape = pageSize._3
    //transform all objects so that the center point of the bounding box matches that of the PDF.
    val paperCenter = if (landscape) Vector2D(420.5, 298.5) else Vector2D(298.5,420.5)
    //move the center of the shapes to 0,0, then scale them to millimeters. (PDF is in inches per default)
    val t = TransformationMatrix(-box,1) //move to 0,0
    val t2 = TransformationMatrix(Vector2D(0,0),mm / Siigna.paperScale) //scale to mm and divide by the paper scale.
    val t3 = TransformationMatrix(paperCenter,1) // move the shapes to the center of the paper
    val transformed = v.transform(t)  //perform the first transformation
    val transformed2 = transformed.transform(t2) //perform the second.
    val transformed3 = transformed2.transform(t3) //perform the third.
    transformed3
  }
  def pageSize : (Int, Int, Boolean) = {
    if(Drawing.boundary.width > Drawing.boundary.height) {
      (841,597, true)
    } else {
      (597,841, false)
    }
  }
}

