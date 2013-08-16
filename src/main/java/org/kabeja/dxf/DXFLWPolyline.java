/*
   Copyright 2005 Simon Mieth

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
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
