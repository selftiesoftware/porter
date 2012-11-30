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

package com.siigna.module.porter.DXF

import com.siigna.module.Module
import org.kabeja._
/**
 * An import module using Kabeja 0.4 DXF import JAR library.
 * Kabeja version 0.4 is released under the apache 2.0 license.
 * See the Kabeja 0.4 licence for more information.
 */
class DXFImport extends Module{

  def stateMap = Map(
    'Start -> {
      case _ => {
        println("RUNNING DXF import")

        //End
      }
    }
  )
}
