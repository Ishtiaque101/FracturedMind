package o1.adventure

import scala.collection.mutable.Map

class Adventure:

  /** the name of the game */
  val title = "Fractured Mind"

  def welcomeMessage = "What is this place? Ugh... My head hurts. Feels like I was sleeping for eternity... Time to get up."

  /** Areas in the game that are visible to the player */
  private val bedroom =    Area("Bedroom", "This is where I was sleeping... Fading memories... Where was I before? Is this a dream? Everything looks familiar but strange at the same time...", false, false)
  private val corridor =   Area("Corridor", "I was definitely here before... The cold ambience and dim lights... The dancing shadows at the corner... The difference is: I was not here alone.", false, false)
  private val babyRoom =   Area("Baby's room", "Looks like a toddler's playground. But where is the child?", false, false)
  private val stairs =     Area("Stairs that go down.", "Walking through the stairs reminds me of something important that I left behind... Something that needs my attention...", false, false)
  private val livingRoom = Area("Living Room", "I suppose this is the living room? items scattered on the table... TV turned on... Looks like it was occupied just moments ago...", false, false)
  private val kitchen =    Area("Kitchen", "I have this feeling she used to cook at this hour of time... Where is she now? It's worrying me...", false, false)
  private val yard =       Area("Yard", "So cold outside... surely not colder than this house... I don't have a clue what's happening right now... Those pile of trash surely don't look good...", false, false)
  private val garage =     Area("Garage", "This place seems like it was closed for a long time... So much dirt and trash... I don't know why but I think I am at the right place.", true, false)


  /** Areas that are hidden to the player and the player finds them by finding hidden keys. */
  private val hiddenRoomOne =   Area("Hidden room", "Perhaps I think I am getting near to where I should be...", true, true)
  private val hiddenRoomTwo =   Area("Hidden room", "Never seen this room before... Where does it lead me?", true, true)
  private val hiddenRoomThree = Area("Hidden room", "I knew I was in the right place... But I think this is not the end.", true, true)


  /** The puzzle rooms and the final room where the bug is located. */
  private val puzzleRoomOne =   Area("Puzzle room", "Maybe they are on the other side of this door. Maybe they are in trouble and I need to save them...", true, false)
  private val puzzleRoomTwo =   Area("Puzzle room", "Another hidden room? Does this end?", false, false)
  private val puzzleRoomThree = Area("Puzzle room", "Another room that I don't know when it existed... Perhaps it leads somewhere to the truth I am seeking. Perhaps it leads to them...", false, false)
  private val finalRoom =       Area("Final room", "That uneasy feeling... Blurry memories... The start of a never-ending tale... I know this place...", true, false)

  /** Exit room */
  private val exitRoom =        Area("Exit room", "What lies on the other side... I don't know... I'm scared...", false, true)


  /** Connections between the rooms (consider the rooms as nodes in a graph and the connections between nodes as edges). */
  val (n, e, w, s) = Tuple4("north", "east", "west", "south")

  bedroom.        setNeighbors(Vector(n -> corridor,        s -> hiddenRoomOne                                              ))
  corridor.       setNeighbors(Vector(n -> stairs,          s -> bedroom,           e -> hiddenRoomTwo, w -> babyRoom       ))
  babyRoom.       setNeighbors(Vector(                                              e -> corridor                           ))
  stairs.         setNeighbors(Vector(n -> livingRoom,      s -> corridor                                                   ))
  livingRoom.     setNeighbors(Vector(                      s -> stairs,            e -> kitchen,       w -> yard           ))
  kitchen.        setNeighbors(Vector(                                                                  w -> livingRoom     ))
  yard.           setNeighbors(Vector(                      s -> garage,            e -> livingRoom                         ))
  garage.         setNeighbors(Vector(n -> yard,            s -> hiddenRoomThree                                            ))
  hiddenRoomOne.  setNeighbors(Vector(n -> bedroom,         s -> puzzleRoomOne                                              ))
  hiddenRoomTwo.  setNeighbors(Vector(                      s -> puzzleRoomTwo,                         w -> corridor       ))
  hiddenRoomThree.setNeighbors(Vector(n -> garage,          s -> puzzleRoomThree                                            ))
  puzzleRoomOne.  setNeighbors(Vector(n -> hiddenRoomOne,   s -> finalRoom,         e -> puzzleRoomTwo, w -> puzzleRoomThree))
  puzzleRoomTwo.  setNeighbors(Vector(n -> hiddenRoomTwo,                                               w -> puzzleRoomOne  ))
  puzzleRoomThree.setNeighbors(Vector(n -> hiddenRoomThree,                         e -> puzzleRoomOne                      ))
  finalRoom.      setNeighbors(Vector(n -> puzzleRoomOne,   s -> exitRoom                                                   ))


  /** Items around the game world. */
  // bedroom:
  private val bed = Container("bed", "an unusually large bed for two people... As if it's hiding something strange... Why was I sleeping here in the first place?", 50000, false, false, false, true)
  private val table = Container("table", "A study table... Looks quite new...", 10000, false, false, false, false)
  private val clockOne = Container("clock", "Seems like it doesn't work... Stopped at 3:45:30... Quite perfect for a clock that does not work...", 1500, false, false, false, false)
  private val drawer = Container("drawer", "An empty drawer with just a backpack... Odd for a bedroom drawer...", 15000, false, false, false, false)

  private val backPack = Container("backpack", "Looks like a travel bag... Never knew I was a traveler... Perhaps I can take it with me to put some items inside.", 237, true, true, false, false)
  private val keyOne = Container("old-key", "This does not look like a house key... Why is it marked \"S\"?", 17, false, true, true, false)
  private val paper = Container("small-paper", "\"Der Anfang ist das Ende und das Ende ist der Anfang <--|-->\"\n\nWhat does it say?", 5, false, true, false, false)
  private val photo = Container("photo", "A family photo... Perhaps that is the wife, the daughter in the middle, and this is the husband... Looks strikingly similar to me... Wait...\n\nHow do I look like?", 7, false, true, false, false)

  table.addContent(Vector(paper, keyOne, photo))
  drawer.addContent(backPack)

  bedroom.addItem(Vector(bed, table, clockOne, drawer, backPack, keyOne, paper, photo))

  // corridor:
  private val bookShelf = Container("bookshelf", "An old, dirty bookshelf... What secrets does it hide?", 20000, false, false, false, true)
  private val bookOne = Container("big-book", "\"The theory of consciousness and evolution\"... interesting...", 200, false, true, false, false)

  private val bookTwo = Container("small-book", "" +
    "\"The Tower of Hanoi (also called The problem of Benares Temple or Tower of Brahma or Lucas' Tower and sometimes pluralized as Towers, or simply pyramid puzzle)" +
    " is a mathematical game or puzzle consisting of three rods and a number of disks of various diameters, which can slide onto any rod. The puzzle begins with the disks " +
    "stacked on one rod in order of decreasing size, the smallest at the top, thus approximating a conical shape. The objective of the puzzle is to move the entire stack " +
    "to one of the other rods, obeying the following rules:\n\n1.Only one disk may be moved at a time.\n2.Each move consists of taking the upper disk from one of the stacks " +
    "and placing it on top of another stack or on an empty rod.\n3.No disk may be placed on top of a disk that is smaller than it.\n4.With three disks, the puzzle can be solved in" +
    " seven moves. The minimal number of moves required to solve a Tower of Hanoi puzzle is 2^n − 1, where n is the number of disks.\"\n\n" +
    "Interesting game...", 150, false, true, false, false)

  private val bookThree = Container("page", "\"A mirror world is a representation of the real world in digital form. It attempts to map real-world structures in a geographically " +
    "accurate way. Mirror worlds offer a software model of real human environments and their workings. It is very similar to the concept of a digital twin.\"", 70, false, true, false, false)

  private val bookFour = Container("diary", "\"06/07/09\n\n6:35:45\n\nHey there! If you are reading this, then I have to tell you that don't worry! Everything seems unclear at first, but trust" +
    " me, it would make sense once you get to know yourself! I was here, and I passed through just fine. You are special! You can do this. See you on the other side!\n\n Best wishes, you know who\"", 50,
    false, true, false, false)

  private val bookFive = Container("newspaper", "\"Headlines - The era of artificial consciousness: blessing or a curse?\"", 25, false, true, false, false)
  private val bookSix = Container("note", "\"I left all these so that you can learn faster. Things are easy nowadays isn't it? :)\"", 10, false, true, false, false)
  private val bookSeven = Container("magazine", "\"Are you ready for some plot twists? You will be after you are ready.\"", 33, false, true, false, false)

  bookShelf.addContent(Vector(bookOne, bookTwo, bookThree, bookFour, bookFive, bookSix, bookSeven))

  corridor.addItem(Vector(bookShelf, bookOne, bookTwo, bookThree, bookFour, bookFive, bookSix, bookSeven))

  //  baby room
  private val cradle = Container("cradle", "Where is the baby?", 7000, false, false, false, false)
  private val toyOne = Container("doll", "A regular barbie doll... Not that old... Wait there is something written on it...\n\n\"|Srood neddih Dinf <--|-->|\"", 20, false, true, false, false)
  private val toyTwo = Container("dollhouse", "A dollhouse... Quite big actually... The hidden doors seem interesting... Perfect for a kid.", 23, false, true, false, false)
  private val rodOne = Container("stick-one", "a stick that has some disks on top of one another. I have read about this somewhere.", 20, false, true, false, false)
  private val rodTwo = Container("stick-two", "Another stick.", 20, false, true, false, false)
  private val rodThree = Container("stick-three", "A third stick", 20, false, true, false, false)
  private val diskOne = Container("disk-one", "This is the smallest disk", 10, false, true, false, false)
  private val diskTwo = Container("disk-two", "This is the medium disk", 13, false, true, false, false)
  private val diskThree = Container("disk-three", "This is the large disk", 15, false, true, false, false)

  rodOne.addContent(Vector(diskOne, diskTwo, diskThree))

  cradle.addContent(Vector(toyOne, toyTwo, rodOne, rodTwo, rodThree, diskOne, diskTwo, diskThree))

  babyRoom.addItem(Vector(cradle, toyOne, toyTwo, rodOne, rodTwo, rodThree, diskOne, diskTwo, diskThree))

  // stairs
  private val painting = Container("painting", "Looks like a Picasso painting... Mind spirit and emotion I suppose... Marks around the edges of the painting... It's not aligned with the marks.", 1000, false, false, false, true)
  private val shovel = Container("shovel", "A dirty shovel... Someone just recently used it to dig... Maybe they are outside?", 120, false, false, true, false)

  stairs.addItem(Vector(painting, shovel))

  // living room
  private val sofa = Container("sofa", "A nice cozy sofa... Ahh! Feels at home...", 15000, false, false, false, false)

  private val tv = Container("TV", "Looks like someone forgot to switch it off... That is some strange show they were watching... " +
    "(TV Playing)\n\n" +
    "\"The beginning is the end, and the end is the beginning... \"" +
    "(Someone wakes up...opens his eyes)" +
    "(Speaking)\n\n" +
    "\"Where am I?\"", 5000, false, false, false, false)

  private val clockTwo = Container("another-clock", "The same clock I saw in the bedroom. This thing stopped at 4:40:30... I find it really strange...", 1500, false, false, false, false)
  private val phone = Container("phone", "Perhaps this is working???... Nah it's dead... The battery is missing", 230, false, true, true, false)
  private val carKey = Container("car-key", "An old car key... Where is the car?", 13, false, true, true, false)
  private val battery = Container("battery", "A phone battery... Quite new... Maybe the phone will work now.", 50, false, true, true, false)
  private val sofaTable = Container("sofa-table", "Scattered items everywhere... I'm sure now that someone was here just before me...", 10000, false, false, false, false)

  sofaTable.addContent(Vector(phone, carKey, battery))

  livingRoom.addItem(Vector(sofa, tv, clockTwo, phone, carKey, battery, sofaTable))


  // kitchen
  private val anotherPaper = Container("prescription", "\"Take these... You'll need them... <3\n\n-you know who\"", 10, false, true, false, false)
  private val fridge = Container("fridge", "I don't know why but I'm feeling stupidly hungry now...", 10000, false, false, false, false)
  private val medicine = Container("medicine", "What are these here for? Whatever, I think I should take them... My head hurts...", 30, false, true, true, false)
  private val bottleOfWater = Container("bottle-of-water", "Obviously I'm a human, am I not?", 100, false, true, true, false)

  fridge.addContent(Vector(medicine, bottleOfWater, anotherPaper))

  kitchen.addItem(Vector(anotherPaper, fridge, medicine, bottleOfWater))

  // yard
  private val garageKey = Container("garage-key", "Garage keys found by digging for reasons unknown just like everything around me...", 12, false, true, true, false)

  yard.addItem(garageKey)

  // garage
  private val car = Container("car", "A 1970 Chevrolet... Abandoned here for ages I suppose...", 350000, false, false, false, false)
  private val keyTwo = Container("key-two", "What is this key for? This thing's marked \"E\"... Seriously, how many of these puzzles they're gonna throw at me... Wait\n\n\n... Who are they??", 50, false, true, true, false)
  private val paintingTwo = Container("painting-two", "The same picasso painting I saw earlier at the corridor... The only difference is that this painting's marks are aligned with the painting " +
    "in the corridor... It seems like they are related... How about I align them with their marks?", 1000, false, false, false, true)

  car.addContent(keyTwo)

  garage.addItem(Vector(car, keyTwo, paintingTwo))


  // Items in the hidden rooms

  // hidden room one (right behind the bed in the bedroom)
  private val writingsOnWall = Container("writings-on-wall", "\"Welcome to the Mirror of reality!\"", 500000, false, false, false, false)
  hiddenRoomOne.addItem(writingsOnWall)

  // hidden room two (after moving the bookshelf in corridor)
  private val writingsOnWallTwo = Container("another-writing", "Der Anfang ist das Ende und das Ende ist der Anfang", 500000, false, false, false, false)
  hiddenRoomTwo.addItem(writingsOnWallTwo)

  // hidden room three (after moving the car and fixing the orientation of the painting)
  private val writingOnWallThree = Container("yet-another-writing", "Der Anfang ist das Ende und das Ende ist der Anfang", 500000, false, false, false, false)
  hiddenRoomThree.addItem(writingOnWallThree)


  // Puzzle room

  // puzzle room one
  private val locker = Container("locker", "Three cells... Three disks on the left cell... Looks very familiar...", 50, false, false, true, false)
  private val cellOne = Container("cell-one", "The first cell... Gotta play the game I think", 500000, false, false, false, false)
  private val cellTwo = Container("cell-two", "The second cell.", 500000, false, false, false, false)
  private val cellThree = Container("cell-three", "The third cell.", 500000, false, false, false, false)
  private val objectOne = Container("object-one", "A small disk", 50, false, false, false, false)
  private val objectTwo = Container("object-two", "A medium disk", 50, false, false, false, false)
  private val objectThree = Container("object-three", "A large disk", 50, false, false, false, false)
  private val writingsOnWallThree = Container("final-writing", "Welcome to the point of no return! You are going to take a pivotal decision about your life... BE READY!", 500000, false, false, false, false)

  cellOne.addContent(Vector(objectThree, objectTwo, objectOne))
  puzzleRoomOne.addItem(Vector(locker, cellOne, cellTwo, cellThree, objectOne, objectTwo, objectThree, writingsOnWallThree))

  // puzzle room two
  private val lockerTwo = Container("locker-two", "Three letters... NWS... What do they mean?", 50, false, false, true, false)
  private val writingsOnWallFour = Container("final-writing-second", "NWS", 500000, false, false, false, false)

  puzzleRoomTwo.addItem(Vector(lockerTwo, writingsOnWallFour))


  // puzzle room three
  private val lockerThree = Container("locker-three", "One letter... E... What does this mean?", 50, false, false, true, false)
  private val writingsOnWallFive = Container("final-writing-third", "NEW", 500000, false, false, false, false)
  puzzleRoomThree.addItem(Vector(lockerThree, writingsOnWallFive))

  // Final room
  private val anotherBed = Container("another-bed", "The same bed... The same place... The same location...", 50000, false, false, false, false)
  private val anotherTable = Container("another-table", "The same table... Looks like a replica of the one I saw earlier in the bedroom... Items are different...", 10000, false, false, false, false)
  private val anotherClock = Container("another-clock", "The same clock... But stopped at 6:30:30... Is it trying to guide me?", 1500, false, false, false, false)
  private val anotherDrawer = Container("another-drawer", "What is this drawer doing here?", 15000, false, false, false, false)
  private val finalPaper = Container("another-paper", "\"The beginning is the end and the end is the beginning <--|-->\"", 5, false, true, false, false)
  private val finalPhoto = Container("another-photo", "The same photo... But wait... Where am I?", 7, false, true, false, false)
  private val anotherPhone = Container("another-phone", "Another phone... This one slightly older than the previous one... Maybe the battery will work on this one...", 250, false, true, false, false)

  anotherTable.addContent(Vector(finalPaper, finalPhoto, anotherPhone))

  finalRoom.addItem(Vector(anotherBed, anotherTable, anotherClock, anotherDrawer, finalPaper, finalPhoto, anotherPhone))


  val player = Player(bedroom, 500, 2000)

  // Some checker functions
  def towerOfHanoiPuzzleChecker(numberOfMoves: Int): Boolean = numberOfMoves == 7
  def rotatedPaintingsChecker(paintingOne: Item, paintingTwo: Item): Boolean = !paintingOne.moveable && !paintingTwo.moveable

  // Objects and their operations
  private val usableItemsToOperation = Map[String, Operation]()


  // operation methods
  def doorOpeningOperation(item: Item, area: Area) =
    val currentRoom = this.player.location
    if !currentRoom.hasNeighbor(area) || area.hidden then
      s"It seems like you cannot use this here."
    else
      item.setUsableStatus(false)
      area.setLockedStatus(false)
      s"You have opened the ${area.name}!"

  def carOpeningOperation(item: Item, area: Area) =
    val currentRoom = this.player.location
    if currentRoom != garage then
      s"You cannot use the car key here."
    else
      item.setUsableStatus(false)
      car.setMoveableStatus(true)
      s"You get into the car. You can now move the car by driving it."


  def medicineUseOperation(medicine: Item, area: Area): String =
    val newHealthSize = medicine.weight + 1
    this.player.updateHealthSize(newHealthSize)
    s"You are now taking the ${medicine}. Your health has improved."


  def moveObjectToOpenAreaOperation(item: Item, area: Area): String =
    item.setMoveableStatus(false)
    area.setHiddenStatus(false)
    s"You have found a hidden door! This might lead to some new area."

  def rotatePaintings(painting: Item, area: Area): String =
    painting.setMoveableStatus(false)

    if (!painting.moveable && !this.paintingTwo.moveable) && (!this.painting.moveable && !painting.moveable) then
      this.hiddenRoomThree.setLockedStatus(false)
      s"You rotated all the paintings! Surprisingly you have unlocked the hidden room at the garage."
    else
      s"You rotated the painting. The painting is now perfectly aligned with the boundary marks."


  def useObjectToRevealHiddenObject(item: Item, area: Area): String =
    if area != this.yard then
      s"You cannot use the shovel here."
    else
      item.setUsableStatus(false)

      this.garageKey.setHiddenStatus(false)
      s"You have found the key to the garage! Examine the item to know more."


  def puzzleRoomOneOpeningOperation(item: Container, area: Area): String =
    if item.contents.isEmpty then
      s"You cannot use the locker yet."
    else
      if item == this.lockerTwo && item.contents.contains(keyTwo) then
        item.setUsableStatus(false)
      if item == this.lockerThree && item.contents.contains(keyOne) then
        item.setUsableStatus(false)

      if (!item.usable && !this.lockerTwo.usable) && (!item.usable && !this.lockerThree.usable) then
        this.puzzleRoomOne.setLockedStatus(false)
        s"You have opened puzzle room one."
      else
        s"Perhaps there are more doors that lead to the other room."


  private var hanoiMovesCount = 0
  def finalRoomOpeningOperation(item: Container, area: Area): String =
    val resultantVector = Vector[Item](objectThree, objectTwo, objectOne)
    if this.hanoiMovesCount == 7 && this.cellThree.contents == resultantVector then
      item.setUsableStatus(false)
      area.setLockedStatus(false)
      s"You have unlocked the door!"
    else
      this.cellThree.setContentVector(Vector[Item]())
      this.cellTwo.setContentVector(Vector[Item]())
      this.cellOne.setContentVector(Vector[Item]())

      for objects <- resultantVector do
        this.player.drop(objects.name)

      item.setUsableStatus(true)
      this.cellOne.addContent(resultantVector)
      this.hanoiMovesCount = 0
      s"Perhaps you have exceeded the move limit. Try again."


  def useBatteryOperation(battery: Container, area: Area): String =
    if !this.player.has(anotherPhone.name) && !this.player.has(phone.name) then
      s"Pick up the phone first."
    else if !this.player.has(anotherPhone.name) && this.player.has(phone.name) then
      s"Seems like the battery doesn't work on this phone. Perhaps there is another phone somewhere around?"
    else
      battery.setUsableStatus(false)
      anotherPhone.setUsableStatus(true)
      s"You have a working phone."


  def usePhoneOperation(phone: Container, area: Area): String =
    phone.setUsableStatus(false)
    this.exitRoom.setHiddenStatus(false)
    s"\"Hello subject 201! If you are reading this message then congratulations! You have passed the first round of training. " +
      s"You are the most intelligent form of artifical consciousness that we have created yet." +
      s" We are on a global crisis: our world and all of its natural resources are at the brink of death. Together with your aid at being an intelligent and self-conscious being, we hope to accomplish " +
      s"the impossible feat that no nation has yet been able to do: A new digital twin of the world.\n\nYour wife and daughter are with us... They are safe! However, it is upto you to decide whether or not " +
      s"you want to go to the next phase or stay here within your own consciousness. There is an exit room right in front of you that leads you out of this house. " +
      s"You are the most intelligent model we have created that has free will so choose wisely... But at the end of the day... The choice " +
      s"is upto you\n\nGood luck subject 201\"\n\n\n(What do you want to do: \n1) Stay here and never go to the next phase (quit the game)\n2) Exit the room\n)"




  // operations

  // operations in bedroom
  private val hiddenRoomOneOperation = Operation("hidden room one opening operation", keyOne, hiddenRoomOne)
  private val moveBedOperation = Operation("move object operation", bed, hiddenRoomOne)
  private val useBagOperation = Operation("bag use operation", backPack, bedroom)
  // operations mapped to functions
  hiddenRoomOneOperation.setOperationFunction(doorOpeningOperation)
  moveBedOperation.setOperationFunction(moveObjectToOpenAreaOperation)

  this.usableItemsToOperation(keyOne.name) = hiddenRoomOneOperation
  this.usableItemsToOperation(bed.name) = moveBedOperation
  this.usableItemsToOperation(backPack.name) = useBagOperation

  // operations in corridor
  private val moveShelfOperation = Operation("hidden room two reveal", bookShelf, hiddenRoomTwo)
  private val hiddenRoomTwoOperation = Operation("hidden room two opening operation", keyTwo, hiddenRoomTwo)

  moveShelfOperation.setOperationFunction(moveObjectToOpenAreaOperation)
  hiddenRoomTwoOperation.setOperationFunction(doorOpeningOperation)

  this.usableItemsToOperation(bookShelf.name) = moveShelfOperation
  this.usableItemsToOperation(keyTwo.name) = hiddenRoomTwoOperation



  // operations in baby room

  // operations in stairs
  private val paintingRotateOne = Operation("painting one rotate operation", painting, stairs)

  paintingRotateOne.setOperationFunction(rotatePaintings)

  this.usableItemsToOperation(painting.name) = paintingRotateOne

  // operations in living room


  // operations in kitchen
  private val medicineOperation = Operation("medicine operation on health", medicine, kitchen)
  private val bottleOfWaterOperation = Operation("bottle of water on heatlh", bottleOfWater, kitchen)

  medicineOperation.setOperationFunction(medicineUseOperation)
  bottleOfWaterOperation.setOperationFunction(medicineUseOperation)

  this.usableItemsToOperation(medicine.name) = medicineOperation
  this.usableItemsToOperation(bottleOfWater.name) = medicineOperation

  // operations on yard
  private val revealGarageKey = Operation("reveal garage key", shovel, yard)
  private val openGarageOperation = Operation("garage open operation", garageKey, garage)

  revealGarageKey.setOperationFunction(useObjectToRevealHiddenObject)
  openGarageOperation.setOperationFunction(doorOpeningOperation)

  this.usableItemsToOperation(shovel.name) = revealGarageKey
  this.usableItemsToOperation(garageKey.name) = openGarageOperation
  // REVEAL GARAGE KEY OPERATION YET TO BE IMPLEMENTED. (DONE)

  // operations on garage
  private val unlockCarOperation = Operation("car unlock operation", carKey, garage)
  private val moveCarOperation = Operation("car move operation", car, hiddenRoomThree)
  private val paintingRotateTwo = Operation("painting two rotate operation", paintingTwo, garage)

  unlockCarOperation.setOperationFunction(carOpeningOperation)
  moveCarOperation.setOperationFunction(moveObjectToOpenAreaOperation)
  paintingRotateTwo.setOperationFunction(rotatePaintings)

  this.usableItemsToOperation(carKey.name) = unlockCarOperation
  this.usableItemsToOperation(car.name) = moveCarOperation
  this.usableItemsToOperation(paintingTwo.name) = paintingRotateTwo

  // operations on hidden room one

  // operations on hidden room two

  // operations on hidden room three

  // operations on puzzle rooms
  private val lockerOperation = Operation("locker one operation", locker, finalRoom)
  private val lockerOperationTwo = Operation("locker two operation", lockerTwo, puzzleRoomOne)
  private val lockerOperationThree = Operation("locker three operation", lockerThree, puzzleRoomOne)

  lockerOperation.setOperationFunction(finalRoomOpeningOperation)
  lockerOperationTwo.setOperationFunction(puzzleRoomOneOpeningOperation)
  lockerOperationThree.setOperationFunction(puzzleRoomOneOpeningOperation)

  this.usableItemsToOperation(locker.name) = lockerOperation
  this.usableItemsToOperation(lockerTwo.name) = lockerOperationTwo
  this.usableItemsToOperation(lockerThree.name) = lockerOperationThree


  // operations in final room
  private val batteryUseOperationTwo = Operation("battery use operation two", battery, finalRoom)
  private val anotherPhoneUseOperation = Operation("last phone use operation", anotherPhone, finalRoom)

  batteryUseOperationTwo.setOperationFunction(useBatteryOperation)
  anotherPhoneUseOperation.setOperationFunction(usePhoneOperation)

  this.usableItemsToOperation(battery.name) = batteryUseOperationTwo
  this.usableItemsToOperation(anotherPhone.name) = anotherPhoneUseOperation


//  this.player.get(keyOne.name)
  var isOver = false
  var isQuit = false


  private var turnCount = 0

  def playTurn(command: String) =
     val action = Action(command)
     val verb = action.getVerb
     val modifierOne = action.getModifierOne
     val wasFinalRoom = this.player.location == finalRoom

     var result = ""
     val outcomeReport = action.execute(this.player, this.usableItemsToOperation.get(modifierOne))
     if outcomeReport.isDefined then
       this.turnCount += 1
       if this.player.hasQuit then
         isQuit = true

       if player.location == this.puzzleRoomOne && verb == "add" && !outcomeReport.get.contains("There") then
         this.hanoiMovesCount += 1
         result += s"\nNumber of moves played: ${this.hanoiMovesCount}"

       if this.player.location == this.exitRoom then
         result += s"\n You are beginning a journey that you don't know when it will end. But the thing is: you just want to meet your family.\n\n\nIsn't that motivating enough?"
         isOver = true

     outcomeReport.getOrElse(s"""Unknown command: "$command".""") + result


end Adventure

