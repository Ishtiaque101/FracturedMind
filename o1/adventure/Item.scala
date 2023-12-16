package o1.adventure


class Item(val name: String, val description: String, val weight: Int, val isBag: Boolean, var hidden: Boolean, var usable: Boolean, var moveable: Boolean):

  /** Returns a short textual representation of the item (its name, that is). */
  
  def setHiddenStatus(status: Boolean) =
    this.hidden = status
    
  def setUsableStatus(status: Boolean) =
    this.usable = status  
  
  def setMoveableStatus(status: Boolean) =
    this.moveable = status
  
  override def toString = this.name

end Item




