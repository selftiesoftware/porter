package com.siigna.module.porter.PDF


import java.io.OutputStream
import java.awt.Color
import com.siigna.app.model.Drawing
import com.siigna.util.geom.TransformationMatrix
import com.itextpdf.text.{PageSize, Document}
import com.itextpdf.text.pdf.{BaseFont, PdfContentByte, PdfWriter}
import com.siigna.app.Siigna
import com.siigna.util.geom.Vector2D
import com.siigna._
import com.siigna.app.model.shape.PolylineShape
import com.siigna.app.model.shape.CircleShape
import com.siigna.app.model.shape.TextShape
import com.siigna.app.model.shape.ArcShape
import com.siigna.app.model.shape.LineShape

/**
 * Exports the current drawing in PDF format.
 * For help and other examples; see http://itextpdf.com/book/examples.php
 */
object PDFExporter extends (OutputStream => Unit) {
  var mm = 72/25.4
  val bf = BaseFont.createFont(Siigna.fontMiso, BaseFont.WINANSI, BaseFont.EMBEDDED);
  def apply(out : OutputStream) {

    val orientation = if (pageSize._3) PageSize.A4.rotate() else PageSize.A4
    val document = new Document(orientation)


    val writer =PdfWriter.getInstance(document, out)
    document.open()
    val canvas = writer.getDirectContent
    writeHeader(canvas)

    shapesEvaluation

    document.close()
    def shapesEvaluation = {
      if(Drawing.shapes.size != 0) {
        val shapes = Drawing.shapes.map (t => (t._2))

        shapes.foreach(s =>
          s match {
            case c : CircleShape => {
              println(c.attributes.toString())
              canvas.saveState()
              val center =rePos(c.center)
              val reposRadius=c.radius * (72/25.4) / Siigna.paperScale
              canvas.circle(center.x.toFloat,center.y.toFloat,reposRadius.toFloat)
              canvas.setLineWidth(width(c.attributes).toFloat)
              val col = color(c.attributes)
              canvas.setRGBColorStroke(col.getRed,col.getGreen,col.getBlue)
              canvas.stroke();
              canvas.restoreState()
            }
            case l : LineShape => {
              canvas.saveState()
              canvas.moveTo(l.p1.x.toFloat,l.p1.y.toFloat)
              canvas.lineTo(l.p2.x.toFloat,l.p2.y.toFloat)
              canvas.setLineWidth(width(l.attributes).toFloat)
              val col = color(l.attributes)
              canvas.setRGBColorStroke(col.getRed,col.getGreen,col.getBlue)
              canvas.stroke()
              canvas.restoreState()
              // line(rePos(l.p1),rePos(l.p2),l.attributes)
            }
              //takes care of both the open and the closed polylineshapes
            case o : PolylineShape => {
              val s=rePos(o.startPoint)
              canvas.saveState()
              canvas.moveTo(s.x.toFloat,s.y.toFloat)
              for(i <- o.innerShapes){
                val p = rePos(i.point)
                canvas.lineTo(p.x.toFloat,p.y.toFloat)
              }
              if(o.isInstanceOf[PolylineShape.PolylineShapeClosed]){canvas.lineTo(s.x.toFloat,s.y.toFloat)}
              canvas.setLineWidth(width(o.attributes).toFloat)
              val col = color(o.attributes)
              canvas.setRGBColorStroke(col.getRed,col.getGreen,col.getBlue)
              canvas.stroke()
              canvas.restoreState()
            }
            case t : TextShape => {

              val content = t.text
              val pos = t.position
              val size = t.fontSize.toInt
              writeText(canvas,content,size,pos.x.toFloat,pos.y.toFloat)
            }
            case a : ArcShape => {
              val fst = rePos(a.geometry.startPoint)
              val scd = rePos(a.geometry.endPoint)
              val angle = a.geometry.startAngle.toFloat
              val endAngle=a.geometry.angle.toFloat
              canvas.saveState()
              canvas.arc(fst.x.toFloat,fst.y.toFloat,scd.x.toFloat,scd.y.toFloat,angle,endAngle)
              canvas.setLineWidth(width(a.attributes).toFloat)
              val col = color(a.attributes)
              canvas.setRGBColorStroke(col.getRed,col.getGreen,col.getBlue)
              canvas.stroke()
              canvas.restoreState()
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
  def writeText(canvas:PdfContentByte,text:String,size:Int,x:Float,y:Float){
    canvas.saveState()
    canvas.beginText()
    canvas.moveText(x, y)
    canvas.setFontAndSize(bf, size)
    canvas.showText(text)
    canvas.endText()
    canvas.restoreState()
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

  def width(a : Attributes):Double= a.double("StrokeWidth").getOrElse(0.2d)/2

  //add a drawing header
  def writeHeader (canvas : PdfContentByte) = {
    val scaleText = "1: "+ Siigna.paperScale
    val infoText ="created @ www.siigna.com - free online drawing app and library"
    val posScale =1.1
    val posTradeMark= 3
    val yPos=18
    val xPosScale=(pageSize._1 /posScale).toFloat
    val xPosInfo =pageSize._1 /posTradeMark
    val textSize = 8

    writeText(canvas,scaleText,textSize,xPosScale,yPos)
    writeText(canvas,infoText,textSize,xPosInfo,yPos)
  }
  //add colors to exported shapes
  def color(a : Attributes):Color ={
    a.color("Color").getOrElse(Color.BLACK)
  }

}

