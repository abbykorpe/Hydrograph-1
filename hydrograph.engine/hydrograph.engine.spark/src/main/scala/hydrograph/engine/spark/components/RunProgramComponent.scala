/** *****************************************************************************
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
  * ******************************************************************************/
package hydrograph.engine.spark.components

import java.io.{BufferedReader, IOException, InputStreamReader}

import hydrograph.engine.core.component.entity.RunProgramEntity
import hydrograph.engine.core.component.entity.base.AssemblyEntityBase
import hydrograph.engine.spark.components.base.CommandComponentSparkFlow
import org.slf4j.{Logger, LoggerFactory}

/**
  * The Class RunProgramComponent.
  *
  * @author Bitwise
  *
  */
class RunProgramComponent(assemblyEntityBase: AssemblyEntityBase) extends CommandComponentSparkFlow with Serializable {

  val LOG: Logger = LoggerFactory.getLogger(classOf[RunProgramComponent])
  var runProgramEntity: RunProgramEntity = assemblyEntityBase.asInstanceOf[RunProgramEntity]

  exitStatus = -2

  override def execute() = {
    var command: String = this.runProgramEntity.getCommand
    try {
      if (System.getProperty("os.name").toLowerCase.contains("windows")) {
        LOG.debug("Command: " + command)
        command = "cmd /c" + command
      }
      LOG.debug("Executing Command.")
      val p: Process = Runtime.getRuntime.exec(command)

      val stdError = new BufferedReader(new InputStreamReader(p.getErrorStream))

      var errorMessage: String = stdError.readLine()
      try {
        if (errorMessage != null) {
          for (s <- stdError.readLine) {
            errorMessage += s
          }
        }
      }
      catch {
        case e =>

          LOG.error("\nException in Run Program Component - "
            + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
            + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
            + "\"]\nError being: " + e.getMessage, e)
          throw new RuntimeException("\nException in Run Program Component - "
          + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
          + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
          + "\"]\nError being: " + e.getMessage, e)
      }
      finally {
        if (errorMessage != null) {

          LOG.error("\nException in Run Program Component - "
            + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
            + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
            + "\"]\nError being: " + errorMessage)
          throw new IOException("\nException in Run Program Component - "
            + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
            + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
            + "\"]\nError being: " + errorMessage)
        }
        exitStatus = p.waitFor
      }
    }
    catch {
      case e: IOException =>

        LOG.error("\nException in Run Program Component - "
          + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
          + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
          + "\"]\nError being: " + e.getMessage, e)
        throw new IOException("\nException in Run Program Component - "
        + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
        + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
        + "\"]\nError being: " + e.getMessage, e)
      case e: InterruptedException =>
        LOG.error("\nException in Run Program Component - "
          + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
          + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
          + "\"]\nError being: " + e.getMessage)
        throw new InterruptedException("\nException in Run Program Component - "
        + "\nComponent Id:[\"" + runProgramEntity.getComponentId + "\"]" + "\nComponent Name:[\""
        + runProgramEntity.getComponentName + "\"]\nBatch:[\"" + runProgramEntity.getBatch
        + "\"]\nError being: " + e.getMessage)
    }
  }

  def getStatus(): Int = {
    exitStatus
  }
}
