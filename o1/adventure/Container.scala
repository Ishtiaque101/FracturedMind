package o1.adventure

import scala.collection.mutable.Buffer

/** An extension to Item class that introduces items within items. */
class Container(name: String, description: String, weight: Int, isBag: Boolean, hidden: Boolean, usable: Boolean, moveable: Boolean) extends Item(name, description, weight, isBag, hidden, usable, moveable):

  private var contentBuffer = Buffer[Item]()

  def setContentVector(newVector: Vector[Item]): Unit =
    this.contentBuffer = newVector.toBuffer

  def addContent(newContent: Item): Unit =
    this.contentBuffer += newContent
    
  def addContent(newContentVector: Vector[Item]): Unit =
    this.contentBuffer ++= newContentVector
    
  def removeContent(content: Item): Unit =
    this.contentBuffer -= content

  def removeContent(contentVector: Vector[Item]): Unit =
    this.contentBuffer --= contentVector

  def contents = this.contentBuffer.toVector
  
  def holderOf(content: Item) = this.contentBuffer.contains(content)

  override def toString =
    if this.contentBuffer.isEmpty then
      super.toString
    else
      s"${super.toString} (that contains\n${this.contentBuffer.mkString("\n")})\n"
