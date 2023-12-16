package o1.adventure

import scala.collection.mutable.Map

class Area(var name: String, var description: String, var locked: Boolean, var hidden: Boolean):

  private val neighbors = Map[String, Area]()
  private val items = Map[String, Container]()

  /** Returns the area that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)
  
  def allNeighbors = this.neighbors.values.toVector // Assuming every area has at least one neighbor.
  
  
  /** Checks whether the given area is a neighbor of this area or not. */
  def hasNeighbor(area: Area) = this.allNeighbors.contains(area)
  
  /** Checks whether this area is a neighbor to the given area. */
  def isNeighbor(area: Area) = area.allNeighbors.contains(this)

  /** Adds an exit from this area to the given area. The neighboring area is reached by moving in
    * the specified direction from this area. */
  def setNeighbor(direction: String, neighbor: Area) =
    this.neighbors += direction -> neighbor

  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
    * the `setNeighbor` method on each of the given direction–area pairs.
    * @param exits  contains pairs consisting of a direction and the neighboring area in that direction
    * @see [[setNeighbor]] */
  def setNeighbors(exits: Vector[(String, Area)]) =
    this.neighbors ++= exits


  /** Returns a multi-line description of the area as a player sees it. This includes a basic
    * description of the area as well as information about exits and items. If there are no
    * items present, the return value has the form "DESCRIPTION\n\nExits available:
    * DIRECTIONS SEPARATED BY SPACES". If there are one or more items present, the return
    * value has the form "DESCRIPTION\nYou see here: ITEMS SEPARATED BY SPACES\n\nExits available:
    * DIRECTIONS SEPARATED BY SPACES". The items and directions are listed in an arbitrary order. */
  def fullDescription =
    val revealedItems = this.items.filter((itemName, item) => !item.hidden)

    val exitList = "\n\nExits available: " + this.neighbors.filter((areaName, area) => !area.hidden).keys.mkString(" ")
    val itemList = "\nYou see here:\n" + revealedItems.keys.mkString(", ")

    val resultString =
      if revealedItems.isEmpty then this.description + exitList
      else this.description + itemList + exitList

    resultString


  def addItem(item: Container): Unit = this.items(item.name) = item
  def addItem(itemVector: Vector[Container]): Unit = itemVector.foreach(item => this.items(item.name) = item)
  
  def contains(itemName: String): Boolean = this.items.contains(itemName)
  def getItem(itemName: String): Option[Container] = this.items.get(itemName)
  def removeItem(itemName: String): Option[Container] = this.items.remove(itemName)
  def getAllItems = this.items.values.toVector
  
  
  def setLockedStatus(status: Boolean) = 
    this.locked = status
    
  def setHiddenStatus(status: Boolean) =
    this.hidden = status


  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)

end Area

