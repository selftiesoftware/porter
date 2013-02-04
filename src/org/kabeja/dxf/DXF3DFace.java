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
 * Created on 29.09.2005
 *
 */
package org.kabeja.dxf;

import org.kabeja.math.MathUtils;


/**
 * @author simon
 *
 */
public class DXF3DFace extends DXFSolid {
    public String getType() {
        return DXFConstants.ENTITY_TYPE_3DFACE;
    }

    public double getLength() {
        double length = 0.0;
        int flag = this.getFlags();

        if ((flag & 1) == 0) {
            length += MathUtils.distance(this.getPoint1(), this.getPoint2());
        }

        if ((flag & 2) == 0) {
            length += MathUtils.distance(this.getPoint2(), this.getPoint3());
        }

        if ((flag & 4) == 0) {
            length += MathUtils.distance(this.getPoint3(), this.getPoint4());
        }

        if ((flag & 8) == 0) {
            length += MathUtils.distance(this.getPoint4(), this.getPoint1());
        }

        return length;
    }
}
