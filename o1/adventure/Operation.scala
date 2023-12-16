package o1.adventure

class Operation(val typeOfOperation: String, val item: Container, val area: Area):
  private var operationFunction = (itemUsed: Container, areaWhereUsed: Area) => ""
  
  
  def setOperationFunction(operation: (Container, Area) => String): Unit =
    this.operationFunction = operation
  
  
  def usageResult() =
    this.operationFunction(this.item, this.area)
  
  override def toString = this.typeOfOperation
  
end Operation
