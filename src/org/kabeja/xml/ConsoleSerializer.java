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
 * Created on 28.11.2005
 *
 */
package org.kabeja.xml;

import java.io.OutputStream;


/**
 * @author simon
 *
 */
public class ConsoleSerializer extends SAXPrettyOutputter {
    /* (non-Javadoc)
     * @see org.kabeja.xml.SAXSerializer#setOutput(java.io.OutputStream)
     */
    public void setOutput(OutputStream out) {
        //switch output to console
        super.setOutput(System.out);
    }
}
