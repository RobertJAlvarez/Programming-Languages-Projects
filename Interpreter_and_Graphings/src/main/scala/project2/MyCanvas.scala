package project2

/* A class to maintain a canvas object.
 * A canvas is a collection of circles and polygons.
 */

import java.awt.geom.{Ellipse2D, Rectangle2D}
import java.awt.{Graphics2D}
import scala.math.{min,max,sin,cos}

/* A figure is a sealed trait. It can be a Polygon or a "MyCircle" */
sealed trait Figure {
    def getBoundingBox: (Double, Double, Double, Double)
    def translate(shiftX: Double, shiftY: Double): Figure
    def render(g: Graphics2D, scaleX: Double, scaleY: Double, shiftX: Double, shiftY: Double): Unit
}

/*
 Class Polygon
   A polygon is defined by a list of its vertices
*/
case class Polygon(val cList: List[(Double, Double)]) extends Figure {
    // Define the bounding box of the polygon
    // A bounding box is a 4-tuple (xMin, xMax, yMin, yMax)
    override def getBoundingBox: (Double, Double, Double, Double) = {
        cList.foldLeft[(Double, Double, Double, Double)]
            ((Double.MaxValue),(Double.MinValue),(Double.MaxValue),(Double.MinValue))
            ({case ((xMin,xMax,yMin,yMax), (x, y)) => (min(xMin,x), max(xMax,x), min(yMin,y), max(yMax,y))})
    }

    // Create a new polygon by shifting each vertex in cList by (x,y)
    override def translate(shiftX: Double, shiftY: Double): Polygon = {
        new Polygon(cList.map[(Double, Double)]({(x,y) => (x+shiftX, y+shiftY)}))
    }

    // render -- draw the polygon. Do not edit this function.
    override def render(g: Graphics2D, scaleX: Double, scaleY: Double, shiftX: Double, shiftY: Double) = {
        val xPoints: Array[Int] = new Array[Int](cList.length)
        val yPoints: Array[Int] = new Array[Int](cList.length)
        for (i <- 0 until cList.length){
            xPoints(i) = ((cList(i)._1 + shiftX )* scaleX).toInt
            yPoints(i) = ((cList(i)._2 + shiftY) * scaleY).toInt
        }
        g.drawPolygon(xPoints, yPoints, cList.length)
    }
}

/*
  Class MyCircle
  Define a circle with a given center c and radius r
*/
case class MyCircle(val c: (Double, Double), val r: Double) extends Figure {
    // Define the bounding box for the circle
    // A bounding box is a 4-tuple (xMin, xMax, yMin, yMax)
    override def getBoundingBox: (Double, Double, Double, Double) = {
        (c._1-r, c._1+r, c._2-r, c._2+r)
    }

    // Create a new circle by shifting the center
    override def translate(shiftX: Double, shiftY: Double): MyCircle = {
        new MyCircle((c._1+shiftX, c._2+shiftY), r)
    }

    // Function: render -- draw the polygon. Do not edit this function.
    override def render(g: Graphics2D, scaleX: Double, scaleY: Double, shiftX: Double, shiftY: Double) = {
        val centerX = ((c._1 + shiftX) * scaleX) .toInt
        val centerY = ((c._2 + shiftY) * scaleY) .toInt
        val radX = (r * scaleX).toInt
        val radY = (r * math.abs(scaleY)).toInt
        //g.draw(new Ellipse2D.Double(centerX, centerY, radX, radY))
        g.drawOval(centerX-radX, centerY-radY, 2*radX, 2*radY)
    }
}

/*
  Class : MyCanvas
  Define a canvas through a list of figure objects. Figure objects can be circles or polygons.
*/
class MyCanvas(val listOfObjects: List[Figure]) {
    // Write a function to get the boundingbox for the entire canvas.
    //  Hint: use existing getBoundingBox functions defined in each figure.
    //  A bounding box is defined as (xMin, xMax, yMin, yMax)
    def getBoundingBox: (Double, Double, Double, Double) = {
        listOfObjects.foldLeft[(Double, Double, Double, Double)]
            ((Double.MaxValue),(Double.MinValue),(Double.MaxValue),(Double.MinValue))
            ({
                case ((xMin,xMax,yMin,yMax), obj) => {
                    val (x_min, x_max, y_min, y_max) = obj.getBoundingBox
                    (min(xMin,x_min), max(xMax,x_max), min(yMin,y_min), max(yMax,y_max))
                }
            })
    }

    // Write a function to translate each figure in the canvas by shiftX, shiftY
    def translate(shiftX: Double, shiftY: Double): MyCanvas = {
        new MyCanvas(
            listOfObjects.foldLeft[List[Figure]]
                (List[Figure]())
                ({ (ls,obj) => ls :+ obj.translate(shiftX,shiftY) })
        )
    }

    // Write a function that will return a new MyCanvas object that places
    //  all the objects in myc2 to the right of the objects in this MyCanvas.
    //  refer to the notebook documentation on how to perform this.
    def placeRight(myc2: MyCanvas): MyCanvas = {
        val (xMin1, xMax1, yMin1, yMax1) = this.getBoundingBox
        val (xMin2, xMax2, yMin2, yMax2) = myc2.getBoundingBox
        val xShift = (xMax1 - xMin1)
        val yShift = (yMax1 - yMin1)/2 - (yMax2 - yMin2)/2
        val c2 = myc2.translate(xShift, yShift)

        this.overlap(c2)
    }

    // Write a function that will return a new MyCanvas object that places
    //  all the figures in myc2 on top of the figures in this MyCanvas.
    //  refer to the notebook documentation on how to perform this.
    def placeTop(myc2: MyCanvas): MyCanvas = {
        val (xMin1, xMax1, yMin1, yMax1) = this.getBoundingBox
        val (xMin2, xMax2, yMin2, yMax2) = myc2.getBoundingBox
        val xShift = (xMax1 - xMin1)/2 - (xMax2 - xMin2)/2
        val yShift = (yMax1 - yMin1)
        val c2 = myc2.translate(xShift, yShift)

        this.overlap(c2)
    }

    //TODO: Write a function that will rotate each figure in the canvas using
    // the angle `ang` defined in radians.
    // Suggestion: first write rotation functions for polygon and circle.
    // rotating a polygon is simply rotating each vertex.
    // rotating a circle is simply rotating the center with radius unchanged.
    def rotate(angRad: Double): MyCanvas = {
        def rotate_point(p: (Double, Double)): (Double, Double) = {
            (p._1*cos(angRad) - p._2*sin(angRad), p._1*sin(angRad) + p._2*cos(angRad))
        }

        new MyCanvas(listOfObjects.map[Figure]({
            fig => fig match {
                case Polygon(cList) => {
                    new Polygon(
                        cList.map[(Double,Double)]({
                            point => rotate_point(point)
                        }))
                }
                case MyCircle(c, r) => {
                    new MyCircle(rotate_point(c), r)
                }
            }
        }))
    }

    // This function takes a list of objects from this canvas
    //  and contatenates with a list of objects from canvas c2.
    // The result is a new MyCanvas object with the concatenated
    //  list of objects.
    def overlap(c2: MyCanvas): MyCanvas = {
        new MyCanvas(this.listOfObjects ++ c2.listOfObjects)
    }

    // DO NOT EDIT THE CODE BELOW

    // Function to draw the canvas.
    def render(g: Graphics2D, xMax: Double, yMax: Double) = {
        val (lx1, ux1, ly1, uy1) = this.getBoundingBox
        val shiftx = -lx1
        val shifty = -uy1
        val scaleX = xMax/(ux1 - lx1  + 1.0)
        val scaleY = yMax/(uy1 - ly1 + 1.0)
        listOfObjects.foreach(f => f.render(g,scaleX, -scaleY, shiftx, shifty))
    }

    override def toString: String = {
        listOfObjects.foldLeft[String] ("") { case (acc, fig) => acc ++ fig.toString }
    }
    def getListOfObjects: List[Figure] = listOfObjects
    def numPolygons: Int = listOfObjects.count {
        case Polygon(_) => true
        case _ => false
    }
    def numCircles: Int = listOfObjects.count {
        case MyCircle(_,_) => true
        case _ => false
    }
    def numVerticesTotal: Int = {
        listOfObjects.foldLeft[Int](0) ((acc, f) =>
            f match {
                case Polygon(lst1) => acc + lst1.length
                case _ => acc
            }
        )
    }
}
