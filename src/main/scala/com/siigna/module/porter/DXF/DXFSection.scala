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

package com.siigna.module.porter.DXF

import scala.actors.Debug

import scala.collection.generic.Subtractable
import com.siigna._
import com.siigna.util.geom.Vector2D
import main.scala.com.siigna.module.porter.DXF.ColorParser
import com.siigna.app.model.shape.RectangleShape

/**
 * A DXF section, represented by list of DXFValues.
 */
case class DXFSection(values: Seq[DXFValue]) extends Subtractable[DXFValue, DXFSection] {
  def +(value: DXFValue) = DXFSection(values.:+(value))

  def ++(xs: Traversable[DXFValue]) = DXFSection(values ++ xs)

  def +(section: DXFSection) = DXFSection(values ++ section.values)

  def -(value: DXFValue) = DXFSection(values.filterNot(_ == value))

  def repr = this

  override def toString = values.mkString

}

/**
 * The companion object to DXFValue providing utility functions to create DXFValues from
 * various objects.
 */
object DXFSection {

  def apply(value: DXFValue): DXFSection = new DXFSection(List(value))

  def apply(value1: DXFValue, value2: DXFValue, values: DXFValue*): DXFSection = DXFSection(value1) + value2 ++ values

  /**
   * Create a DXFSection from a Vector2D.
   */
  def fromVector(vector: Vector2D) = {
    DXFSection(DXFValue(100, "AcDbPoint"), DXFValue(10, vector.x), DXFValue(20, vector.y))
  }

  //****EXPORT FUNCTIONS ****
  //write selected shape to DXF:

  def toDXF(shape: Shape): DXFSection = {

    def vectorToDXF(v: Vector2D) = {
      Seq(DXFValue(10, v.x),
        DXFValue(20, v.y))
    }

    try {
      shape match {
        //export ArcShapes
        case a: ArcShape => {
          DXFSection(DXFValue(0, "ARC"),
            //random identifier number (HEX)
            DXFValue(5, (scala.util.Random.nextInt.toHexString)),
            DXFValue(100, "AcDbEntity"),
            //layer
            DXFValue(8, 0),
            DXFValue(100, "AcDbCircle"),
            //color
            DXFValue(62, if (!shape.attributes.get("Color").isEmpty) ColorParser.parseColor(shape.attributes.get("Color").get.toString) else 0),
            //width
            DXFValue(370, if (!shape.attributes.get("StrokeWidth").isEmpty) (shape.attributes.get("StrokeWidth").get.toString.toDouble * 100).toInt else 0),
            //number of points
            DXFValue(90, 2),
            DXFValue(70, 0),
            //Center point
            DXFValue(10, a.center.x),
            DXFValue(20, a.center.y),
            //radius
            DXFValue(40, a.radius),
            DXFValue(100, "AcDbArc"),
            //start angle
            DXFValue(50, a.startAngle),
            DXFValue(51, a.geometry.endAngle)
          )
        }

        //export lineShapes
        case l: LineShape => {
          DXFSection(DXFValue(0, "LWPOLYLINE"),
            //random identifier number (HEX)
            DXFValue(5, (scala.util.Random.nextInt.toHexString)),
            DXFValue(100, "AcDbEntity"),
            //layer
            DXFValue(8, 0),
            DXFValue(100, "AcDbPolyline"),
            //color
            DXFValue(62, if (!shape.attributes.get("Color").isEmpty) ColorParser.parseColor(shape.attributes.get("Color").get.toString) else 0),
            //LineWidth
            DXFValue(370, if (!shape.attributes.get("StrokeWidth").isEmpty) (shape.attributes.get("StrokeWidth").get.toString.toDouble * 100).toInt else 0),
            //number of points
            DXFValue(90, 2),
            DXFValue(70, 0),
            //Points
            DXFValue(10, l.p1.x), DXFValue(20, l.p1.y), DXFValue(10, l.p2.x), DXFValue(20, l.p2.y)
        )}
        case p: PolylineShape => {
          val vertices = p.geometry.vertices
          val numberOfVertices = vertices.size

          DXFSection(
            DXFValue(0, "LWPOLYLINE"),
            //random identifier number (HEX)
            DXFValue(5, (scala.util.Random.nextInt.toHexString)),
            DXFValue(100, "AcDbEntity"),
            //layer
            DXFValue(8, 0),
            DXFValue(100, "AcDbPolyline"),
            //color
            DXFValue(62, if (!shape.attributes.get("Color").isEmpty) ColorParser.parseColor(shape.attributes.get("Color").get.toString) else 0),
            //Line width
            DXFValue(370, if (!shape.attributes.get("StrokeWidth").isEmpty) (shape.attributes.get("StrokeWidth").get.toString.toDouble * 100).toInt else 0),
            //number of points
            DXFValue(90, numberOfVertices),
            DXFValue(70, 0)
            //Points
          ) ++ vertices.map(vectorToDXF).flatten
        }
        case c: CircleShape => {
          DXFSection(DXFValue(0, "CIRCLE"),
            //random identifier number (HEX)
            DXFValue(5, (scala.util.Random.nextInt.toHexString)),
            DXFValue(100, "AcDbEntity"),
            //layer
            DXFValue(8, 0),
            DXFValue(100, "AcDbCircle"),
            //color
            DXFValue(62, if (!shape.attributes.get("Color").isEmpty) ColorParser.parseColor(shape.attributes.get("Color").get.toString) else 0),
            //width
            DXFValue(370, if (!shape.attributes.get("StrokeWidth").isEmpty) (shape.attributes.get("StrokeWidth").get.toString.toDouble * 100).toInt else 0),
            //number of points
            DXFValue(90, 2),
            DXFValue(70, 0),
            //Center point
            DXFValue(20, c.center.x), DXFValue(30, c.center.y),
            //radius
            DXFValue(40, c.radius)
          )
        }
        case t: TextShape => {
          println(t.position.x)
          println(t.position.y)
          DXFSection(DXFValue(0, "MTEXT"),
            //random identifier number (HEX)
            DXFValue(5, (scala.util.Random.nextInt.toHexString)),
            DXFValue(100, "AcDbEntity"),
            //layer
            DXFValue(8, 0),
            DXFValue(100, "AcDbMText"),
            //placement
            DXFValue(10, t.position.x),
            DXFValue(20, t.position.y),
            DXFValue(30, 0.0),
            //text height
            DXFValue(40, t.scale / 1.2),
            //text rectangle width
            DXFValue(41, t.geometry.width),
            //attachment point
            DXFValue(71, 1),
            //drawing direction
            DXFValue(72, 1),
            //text string
            DXFValue(1, t.text)
          )
        }

        //rectangles are exported to closed polylines
        case r : RectangleShape => {
          val vertices = r.geometry.vertices
          val closedVertices = vertices :+ vertices.head
          val numberOfVertices = closedVertices

          DXFSection(
            DXFValue(0, "LWPOLYLINE"),
            //random identifier number (HEX)
            DXFValue(5, (scala.util.Random.nextInt.toHexString)),
            DXFValue(100, "AcDbEntity"),
            //layer
            DXFValue(8, 0),
            DXFValue(100, "AcDbPolyline"),
            //color
            DXFValue(62, if (!shape.attributes.get("Color").isEmpty) ColorParser.parseColor(shape.attributes.get("Color").get.toString) else 0),
            //Line width
            DXFValue(370, if (!shape.attributes.get("StrokeWidth").isEmpty) (shape.attributes.get("StrokeWidth").get.toString.toDouble * 100).toInt else 0),
            //number of points
            DXFValue(90, numberOfVertices),
            DXFValue(70, 0)
            //Points
          ) ++ closedVertices.map(vectorToDXF).flatten
        }

        case e => {
          Siigna display ("DXF export of " + e + " not supported")
          //TODO: a hack which constructs an empty line if an unrecognized shape is found.
          DXFSection(DXFValue(0,"LINE"))
        }
      }

    }
  }
}