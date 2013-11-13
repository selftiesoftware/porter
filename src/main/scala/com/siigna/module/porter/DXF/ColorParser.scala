package main.scala.com.siigna.module.porter.DXF

import java.awt.Color


/**
 * Two functions used to:
 * a) parse colors to the DXF format
 * b) set siigna colors from DXF colors.
 */

object ColorParser {
  //parse Siigna colors to DXF color codes
  def parseColor(c : String) : Int = {
    print("parsing color: "+c)
    c match {
      case "java.awt.Color[r=0,g=0,b=0]"     => 0   //black
      case "java.awt.Color[r=38,g=38,b=38]"    => 8   //black
      case "java.awt.Color[r=64,g=64,b=64]"    => 250   //anthracite  darkest
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
      //case _ => 0
    }
  }

  //a function to make a Siigna color FROM a DXF color
  def setColor(c: Double) : Option[Color] = {
    val transp = 1.00f

    c match {
      case 0.0 =>   None //leave blank
      case 255.0 => Some(new Color(0.00f, 0.00f, 0.00f, transp)) //black
      case 8.0 =>   Some(new Color(0.25f, 0.25f, 0.25f, transp)) //anthracite
      case 251.0 => Some(new Color(0.40f, 0.40f, 0.40f, transp)) //dimgrey
      case 252.0 => Some(new Color(0.60f, 0.60f, 0.60f, transp)) //grey
      case 253.0 => Some(new Color(0.75f, 0.75f, 0.75f, transp)) //lightGrey
      case 254.0 => Some(new Color(0.90f, 0.90f, 0.90f, transp)) //brightGrey
      case 2.0 =>   Some(new Color(1.00f, 1.00f, 0.40f, transp)) //yellow
      case 30.0 =>  Some(new Color(1.00f, 0.75f, 0.30f, transp)) //orange
      case 20.0 =>  Some(new Color(0.95f, 0.45f, 0.22f, transp)) //orangeRed
      case 10.0 =>  Some(new Color(0.95f, 0.12f, 0.30f, transp)) //red
      case 240.0 => Some(new Color(0.95f, 0.14f, 0.46f, transp)) //radicalRed
      case 232.0 => Some(new Color(0.95f, 0.15f, 0.58f, transp)) //violetRed
      case 6.0 =>   Some(new Color(0.95f, 0.15f, 0.80f, transp)) //magenta
      case 222.0 => Some(new Color(0.64f, 0.18f, 0.85f, transp)) //plum
      case 220.0 => Some(new Color(0.35f, 0.22f, 0.90f, transp)) //purple
      case 180.0 => Some(new Color(0.12f, 0.25f, 0.95f, transp)) //blue
      case 160.0 => Some(new Color(0.10f, 0.45f, 0.95f, transp)) //navyBlue
      case 140.0 => Some(new Color(0.10f, 0.65f, 0.95f, transp)) //pacificBlue
      case 4.0 =>   Some(new Color(0.10f, 0.95f, 0.95f, transp)) //cyan
      case 130.0 => Some(new Color(0.10f, 0.95f, 0.75f, transp)) //turquise
      case 120.0 => Some(new Color(0.10f, 0.95f, 0.50f, transp)) //caribbean
      case 3.0 =>   Some(new Color(0.10f, 0.95f, 0.10f, transp)) //green
      case 70.0 =>  Some(new Color(0.50f, 0.95f, 0.15f, transp)) //lime
      case 60.0 =>  Some(new Color(0.65f, 0.95f, 0.15f, transp)) //yellowGreen
      case _ =>     Some(new Color(0.00f, 0.00f, 0.00f, transp)) //black

    }
  }
}
