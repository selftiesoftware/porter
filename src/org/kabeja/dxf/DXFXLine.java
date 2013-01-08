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

/*
 * Created on 17.10.2005
 *
 */
package org.kabeja.dxf;


/**
 * @author <a href="mailto:simon.mieth@gmx.de>Simon Mieth</a>
 *
 */
public class DXFXLine extends DXFRay {
    public Bounds getBounds() {
        //the xline is a infinite straight line
        //so we omit the bounds
        Bounds bounds = new Bounds();
        bounds.setValid(false);

        return bounds;
    }

    public String getType() {
        return DXFConstants.ENTITY_TYPE_XLINE;
    }
}
