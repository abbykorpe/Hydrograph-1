/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.spark.components.utils

import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}

import scala.collection.mutable.ListBuffer

/**
  * Created by bitwise on 1/13/2017.
  */
case class FieldContext(name: String, xPath:String, datatype: DataType, isNullable: Boolean,format: String) {
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

      case x if (x.fieldContext.name.equals(parent)&&checkXpaths(x.fieldContext,child)) => x.addChild(child)
      case x => x.children.toList.map(a => findAndAdd(a))
    }

    def checkXpaths(parent: FieldContext, child: FieldContext):Boolean={

      parent.xPath.equals(child.xPath.substring(0,child.xPath.lastIndexOf("/")))
    }

    findAndAdd(rootNode)
  }



  def isPresent(prospectNodeName: String, prospectNodeXPath:String): Boolean = {
    var present: Boolean = false

    def find(treeNode: TreeNode): Unit = treeNode match {
      case x if (x.fieldContext.name.equals(prospectNodeName)&&x.fieldContext.xPath.equals(prospectNodeXPath)) => present = true
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

