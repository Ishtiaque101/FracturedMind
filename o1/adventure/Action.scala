package o1.adventure

/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as “go east” or “rest” */
class Action(input: String):

  private val commandText = input.trim
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim
  private val modifierOne = modifiers.filter(_ != ' ').takeWhile(_ != '>').trim
  private val modifierTwo = modifiers.filter(_ != ' ').drop(modifierOne.length + 1).trim
   
  def getVerb = this.verb
  def getModifierOne = this.modifierOne
  def getModifierTwo = this.modifierTwo

  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as “You go west.”). The description is returned in an `Option`
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player, operationOption: Option[Operation]) = this.verb match
    case "help"      => Some(
                              "Commands: \n" +
                                "1.  go {direction}\n" +
                                "2.  rest\n" +
                                "3.  quit\n" +
                                "4.  inventory\n" +
                                "5.  health\n" +
                                "6.  get {itemName}\n" +
                                "7.  drop {itemName}\n" +
                                "8.  examine {itemName}\n" +
                                "9.  use {itemName}\n" +
                                "10. move {itemName}\n" +
                                "11. add {itemToAdd} > {itemName}\n\n\n" +
                              "Things to note:\n" +
                                "1. You can't perform add operation on two " +
                                "items that are both in your inventory. You must " +
                                "drop the bigger item (container) and then perform " +
                                "the add operation.\n" +
                                "2. Items that increase health are not crucial in winning " +
                                "the game or completing it. You can, however, use them to " +
                                "increase your health.\n" +
                                "3. Item names are case-sensitive. So \"itemName\" != \"itemname\". " +
                                "notice that while performing the operations."
                            )
    case "go"        => Some(actor.go(this.modifiers))
    case "rest"      => Some(actor.rest())
    case "quit"      => Some(actor.quit())
    case "inventory" => Some(actor.inventory)
    case "health"    => Some(actor.getHealth)
    case "get"       => Some(actor.get(this.modifiers))
    case "drop"      => Some(actor.drop(this.modifiers))
    case "examine"   => Some(actor.examine(this.modifiers))
    case "use"       => Some(actor.use(this.modifiers, operationOption))
    case "move"      => Some(actor.move(this.modifiers, operationOption))
    case "add"       => Some(actor.add(this.modifierOne, this.modifierTwo))
    case other       => None

  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = s"$verb (modifiers: $modifiers)" + s"${if this.verb == "add" then s" with first item: ${this.modifierOne} and second item: ${this.modifierTwo}." else ""}"

end Action

