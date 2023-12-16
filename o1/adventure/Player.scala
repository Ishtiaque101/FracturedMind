package o1.adventure

import scala.collection.mutable.Map

class Player(startingArea: Area, startingInventorySize: Int, startingHealth: Int):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var itemsInPossession = Map[String, Container]()
  private var inventorySize = startingInventorySize // size in grams (g)
  private var health = this.startingHealth

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven
  
  def getItemsInPossession = this.itemsInPossession

  /** Returns the player’s current location. */
  def location = this.currentLocation
  
  /** New methond */
  def updateInventorySize(amount: Int) = this.inventorySize += amount

  /** New method */
  def updateHealthSize(amount: Int) = this.health += amount

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */

  /** Modified movement. Now checks for locked areas. */
  def go(direction: String) =
    val destination = this.location.neighbor(direction)

    destination match
      case None =>
        s"You can't go ${direction}."
      case Some(area) =>
        if area.hidden then s"You can't go ${direction}."
        else if area.locked then s"The ${area.name} in the ${direction} is locked. Do something that will open the door."
        else
          this.currentLocation = area

          s"You go ${direction}. You are now in the ${area.name}."


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() =
    "You rest for a while. Better get a move on, though."


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    "You are trapped in an endless cycle of the distinction between true and false. Perhaps you want to settle in this void and neglect your responsibilities. " +
      "At the end of the day, we learn from what we cannot endure...\n\n"

  /** Modified drop method to include inventory size. */
  def drop(itemName: String): String =
    val item = this.itemsInPossession.remove(itemName)
    item match
      case None => "You don't have that!"
      case Some(value) =>
        this.inventorySize =
          
          if value.isBag then
            this.inventorySize - value.weight
          else
            this.inventorySize + value.weight
            
        this.currentLocation.addItem(value)
        if value.contents.nonEmpty then
          val contentVector = value.contents
          val childItems = this.itemsInPossession.filter((childItemName, childItem) => contentVector.contains(childItem))
          childItems.values.foreach(this.currentLocation.addItem)
          childItems.keys.toVector.foreach(this.itemsInPossession.remove)


        this.itemsInPossession.filter((itemName_, item_) => item_.holderOf(value)).foreach((itemName__, item__) => item__.removeContent(value))
        s"You drop the ${itemName}."

  /** Modified get method to handle different cases of the specified item. */
  def get(itemName: String): String =
    val item = this.currentLocation.removeItem(itemName)
    
    item match
      case None => s"There is no ${itemName} here to pick up."
      case Some(value) =>
        if value.weight > this.inventorySize then
          this.currentLocation.addItem(value)
          s"You cannot pick up the ${itemName}."
        else
          this.inventorySize -= value.weight
          
          if value.isBag then
            this.inventorySize += 2 * value.weight
            
          val allItems = this.currentLocation.getAllItems // assuming every room contains at least one item
          allItems.filter(_.holderOf(value)).foreach(_.removeContent(value))
          this.itemsInPossession(value.name) = value
          value.contents.map(item => this.currentLocation.removeItem(item.name).get).foreach(item => this.itemsInPossession(item.name) = item)

          s"You pick up the ${itemName}.\n${if value.isBag then s"Your inventory size has increased! You can now store more items." else ""}"



  def has(itemName: String): Boolean = this.itemsInPossession.contains(itemName)

  def inventory: String =
    if this.itemsInPossession.isEmpty then
      s"You are empty-handed.\nStorage remaining: ${this.inventorySize}"
    else
      s"You are carrying:\n${this.itemsInPossession.keys.mkString("\n")}\nStorage remaining: ${this.inventorySize}"

  def getHealth: String =
    if this.health < 10 then
      s"You're health is ${this.health}.\nYou are running low on health! Pick up something that you can use to improve it."
    else
      s"You're health is ${this.health}."


  /** Modified examine method to handle items within items. */
  def examine(itemName: String): String =
    val getItem = this.currentLocation.getItem(itemName)

    var result = ""
    getItem match

      case None =>
        val isInInventory = this.itemsInPossession.get(itemName)

        isInInventory match
          case None => result += s"There is no such ${itemName} for you to examine."
          case Some(item) =>
            if item.contents.nonEmpty then
              result += s"You look closely at the ${itemName}:\n\n\n${item.description}\n\nYou see here: \n${item.contents.mkString(", ")}"
            else
              result += s"You look closely at the ${itemName}:\n\n\n${item.description}\n\n"


      case Some(item) =>
        if item.weight <= this.inventorySize then
          result += s"Small items need to be picked up in order for you to examine them."

        else
          val itemsInside = item.contents

          if itemsInside.nonEmpty then
            itemsInside.foreach(_.setHiddenStatus(false))
            result += s"You look closely at the ${itemName}:\n\n\n${item.description}\n\nYou see here: \n${item.contents.mkString(", ")}"
          else
            result += s"You look closely at the ${itemName}:\n\n\n${item.description}"

    result
    
  /** New method */
  def use(itemName: String, usageOperationOption: Option[Operation]): String =
    if usageOperationOption.isEmpty then
      s"The ${itemName} is either not usable or it does not exist. Check your command and try again."

    else
      val usageOperation = usageOperationOption.get
      val getItem = this.itemsInPossession.get(itemName)

      getItem match
        case None => s"This ${itemName} seems usable but you need to pick it up first."
        case Some(item) =>
          val usable = item.usable

          if usable then
            val usedResult = usageOperation.usageResult()
            s"You use the ${item}.\n\n${usedResult}"
          else
            s"The ${item} has already been used or you cannot just use this item yet."

  
  /** New method */
  def move(itemName: String, moveOperationOption: Option[Operation]): String =
    if moveOperationOption.isEmpty then
      s"The ${itemName} is either not moveable or it does not exist. Check your command and try again."
    else
      val moveOperation = moveOperationOption.get
      val getItem = this.currentLocation.getItem(itemName)
    
      getItem match
        case None => s"There is no such ${itemName} for you to move."
        case Some(item) =>
          val moveable = item.moveable
        
          if moveable then
            val movedResult = moveOperation.usageResult()
            s"You move the ${item}.\n\n${movedResult}"
          else
            s"The ${item} has already been moved. There is no point in moving it anymore."



  def add(itemName: String, toItemName: String): String =
    val getItem = this.itemsInPossession.get(itemName)

    getItem match
      case None => s"There is no such ${itemName} for you to add. Either it is not in your inventory or it does not exist."
      case Some(item) =>
        val getToItem = this.currentLocation.getItem(toItemName)

        getToItem match
          case None => s"There is no such ${toItemName} in this area to add ${itemName}."
          case Some(toItem) =>
            if toItem.holderOf(item) then
              s"You have already added ${item} to ${toItem.name}."
            else if item.weight >= 2 * toItem.weight then
              s"You cannot fit this ${item} inside the ${toItem.name}."
            else
              this.itemsInPossession.remove(itemName)
              this.inventorySize += item.weight
              toItem.addContent(item)
              this.currentLocation.addItem(item)
              s"You have added ${item} to ${toItem.name}."



  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


end Player

