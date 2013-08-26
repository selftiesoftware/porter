package main.scala.com.siigna.module.porter.DXF

import com.siigna.app.model.shape.Shape

/**
 * Two functions used to:
 * a) parse colors to the DXF format
 * b) set siigna colors from DXF colors.
 */

object ColorParser {
  //parse Siigna colors to DXF color codes
  def parseColor(c : String) : Int = {
    c match {
      case "java.awt.Color[r=0,g=0,b=0]"     => 0   //black
      case "java.awt.Color[r=64,g=64,b=64]" => 8   //anthracite  darkest
      case "java.awt.Color[r=102,g=102,b=102]" => 251 //dimgrey    lighter
      case "java.awt.Color[r=153,g=153,b=153]" => 252 //grey       -->
      case "java.awt.Color[r=191,g=191,b=191]" => 253 //lightGrey
      case "java.awt.Color[r=222,g=222,b=222]" => 254 //brightGrey  //DOESNT WORK??
      case "java.awt.Color[r=255,g=255,b=102]" => 2   //yellow
      case "java.awt.Color[r=255,g=191,b=77]" => 30  //orange
      case "java.awt.Color[r=242,g=115,b=56]" => 20  //orangeRed
      case "java.awt.Color[r=242,g=31,b=77]" => 10  //red
      case "java.awt.Color[r=242,g=36,b=117]" => 240 //radicalRed
      case "java.awt.Color[r=242,g=38,b=148]" => 232 //violetRed
      case "java.awt.Color[r=242,g=38,b=204]" => 6   //magenta
      case "java.awt.Color[r=163,g=46,b=217]" => 222 //plum
      case "java.awt.Color[r=89,g=56,b=230]" => 220 //purple
      case "java.awt.Color[r=31,g=64,b=242]" => 180 //blue
      case "java.awt.Color[r=26,g=115,b=242]" => 160 //navyBlue
      case "java.awt.Color[r=26,g=166,b=242]" => 140 //pacificBlue
      case "java.awt.Color[r=26,g=242,b=242]" => 4   //cyan
      case "java.awt.Color[r=26,g=242,b=191]" => 130 //turquise
      case "java.awt.Color[r=26,g=242,b=128]" => 120 //caribbean
      case "java.awt.Color[r=26,g=242,b=26]" => 3   //green
      case "java.awt.Color[r=128,g=242,b=38]" => 70  //lime
      case "java.awt.Color[r=166,g=242,b=38]" => 60  //yellowGreen
    }
  }

  //a function to make a Siigna color FROM a DXF color
  def setColor(c: Double) : String = {
    println("C; "+c)
    c match {
      case 0.0 => "java.awt.Color[r=0,g=0,b=0]" //black
      case 8.0 => "java.awt.Color[r=64,g=64,b=64]" //anthracite
      case 251.0 =>  "java.awt.Color[r=102,g=102,b=102]" //dimgrey
      case 252.0 =>  "java.awt.Color[r=153,g=153,b=153]" //grey
      case 253.0 =>  "java.awt.Color[r=191,g=191,b=191]" //lightGrey
      case 254.0 =>  "java.awt.Color[r=222,g=222,b=222]" //brightGrey
      case 2.0 =>  "java.awt.Color[r=255,g=255,b=102]" //yellow
      case 30.0 =>  "java.awt.Color[r=255,g=191,b=77]" //orange
      case 20.0 =>  "java.awt.Color[r=242,g=115,b=56]" //orangeRed
      case 10.0 =>  "java.awt.Color[r=242,g=31,b=77]" //red
      case 240.0 => "java.awt.Color[r=242,g=36,b=117]" //radicalRed
      case 232.0 => "java.awt.Color[r=242,g=38,b=148]"//violetRed
      case 6.0 => "java.awt.Color[r=242,g=38,b=204]" //magenta
      case 222.0 => "java.awt.Color[r=163,g=46,b=217]" //plum
      case 220.0 => "java.awt.Color[r=89,g=56,b=230]" //purple
      case 180.0 => "java.awt.Color[r=31,g=64,b=242]" //blue
      case 160.0 => "java.awt.Color[r=26,g=115,b=242]"  //navyBlue
      case 140.0 => "java.awt.Color[r=26,g=166,b=242]" //pacificBlue
      case 4.0 => "java.awt.Color[r=26,g=242,b=242]" //cyan
      case 130.0 => "java.awt.Color[r=26,g=242,b=191]" //turquise
      case 120.0 => "java.awt.Color[r=26,g=242,b=128]" //caribbean
      case 3.0 => "java.awt.Color[r=26,g=242,b=26]" //green
      case 70.0 => "java.awt.Color[r=128,g=242,b=38]" //lime
      case 60.0 => "java.awt.Color[r=166,g=242,b=38]" //yellowGreen
    }
  }
}
