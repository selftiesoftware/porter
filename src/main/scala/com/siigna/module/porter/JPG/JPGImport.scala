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

package com.siigna.module.porter.JPG

import java.io.InputStream
import java.awt.{Toolkit, Canvas, MediaTracker}
import com.siigna._
import javax.imageio.ImageIO

object JPGImport {
  //a tracker to check when the image is loaded (the image height and width can not be read before then)
  var tracker : MediaTracker = new MediaTracker(new Canvas())

  //a function to read a DXF file and create the shapes in it.
  def apply(input : InputStream) = {

    //get a background image and save (the info is stored in siigna/app(Siigna)
    val image = Some(ImageIO.read(input).asInstanceOf[java.awt.Image])
    if(image.isDefined){
      Siigna.imageBackground = (Some(image.get),None,1.0)
      //track when the image is loaded. Necessary since width and height parameters are unavailable before then.
      tracker.addImage(image.get, 0)
      tracker.waitForID(0)
    }
  }

}
