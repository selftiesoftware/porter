package com.siigna.module.porter.DXF

import java.io.{File, FilenameFilter}

/**
 * A file name filter for .dxf files.
 */
object DXFFilenameFilter extends FilenameFilter {

    def accept(dir: File, name: String): Boolean = dir.getName.endsWith(".dxf")

}
