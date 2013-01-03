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
 * Created on 13.04.2005
 *
 */
package org.kabeja.dxf;


/**
 * @author <a href="mailto:simon.mieth@gmx.de>Simon Mieth</a>
 *
 */
public class DXFLWPolyline extends DXFPolyline {
    private double constantwidth = 0.0;
    private double elevation = 0.0;

    public DXFLWPolyline() {
    }

    public void setConstantWidth(double width) {
        this.constantwidth = width;
    }

    public double getContstantWidth() {
        return this.constantwidth;
    }

    /**
     * @return Returns the elevation.
     */
    public double getElevation() {
        return elevation;
    }

    /**
     * @param elevation
     *            The elevation to set.
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    /* (non-Javadoc)
     * @see de.miethxml.kabeja.dxf.DXFEntity#getType()
     */
    public String getType() {
        return DXFConstants.ENTITY_TYPE_LWPOLYLINE;
    }
}
