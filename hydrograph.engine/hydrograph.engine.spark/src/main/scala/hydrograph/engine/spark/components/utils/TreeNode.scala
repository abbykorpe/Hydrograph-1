package hydrograph.engine.spark.components.utils

import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}

import scala.collection.mutable.ListBuffer

/**
  * Created by bitwise on 1/13/2017.
  */
case class FieldContext(name: String, datatype: DataType, isNullable: Boolean) {
}

case class TreeNode(fieldContext: FieldContext) {
  var children: ListBuffer[TreeNode] = ListBuffer()

  def addChild(data: FieldContext): Unit = children match {
    case x if (x == ListBuffer) => x.append(TreeNode(data))
    case y => y.append(TreeNode(data))
  }
}

case class XMLTree(fc: FieldContext) {
  val rootNode = TreeNode(fc)

  def addChild(parent: String, child: FieldContext): Unit = {
    def findAndAdd(node: TreeNode): Unit = node match {
      case x if (x.fieldContext.name.equals(parent)) => x.addChild(child)
      case x => x.children.toList.map(a => findAndAdd(a))
    }

    findAndAdd(rootNode)
  }

  def isPresent(prospectNodeName: String): Boolean = {
    var present: Boolean = false

    def find(treeNode: TreeNode): Unit = treeNode match {
      case x if (x.fieldContext.name.equals(prospectNodeName)) => present = true
      case x => x.children.toList.map(a => find(a))
    }

    find(rootNode)
    present
  }

  def isPresentWithParent(prospectNodeName: String, parentNode: String): Boolean = {
    var present: Boolean = false

    def find(treeNode: TreeNode, parent:String): Unit = (treeNode,parent) match {
      case (x,y) if (x.fieldContext.name.equals(prospectNodeName)) => present = true
      case (x,y) => {
        var parentTag = x
        x.children.toList.map(a => find(a,x.fieldContext.name))
      }
    }

    find(rootNode,rootNode.fieldContext.name)
    present
  }

  def printTree(node: TreeNode): Unit = node.children.toList match {
    case x => {
      print(node.fieldContext.name + " ")
      x.map(a => printTree(a))
      println("")
    }
  }

  def printTree(): Unit = printTree(rootNode)
}

