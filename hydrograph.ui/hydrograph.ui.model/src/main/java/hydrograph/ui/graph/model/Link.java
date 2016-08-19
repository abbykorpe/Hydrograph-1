/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package hydrograph.ui.graph.model;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;

import java.util.Map;

import org.eclipse.draw2d.Graphics;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * The Class Link.
 * 
 * @author Bitwise
 */
public class Link extends Model {
	private static final long serialVersionUID = -4969635974273718739L;

	/** Line drawing style for this connection. */
	private int lineStyle = Graphics.LINE_SOLID;
	private int linkNumber;
	/**
	 * Used for indicating that a Connection with solid line style should be created.
	 */
	public static final Integer SOLID_CONNECTION = new Integer(Graphics.LINE_SOLID);
	/** Property ID to use when the line style of this connection is modified. */
	public static final String LINESTYLE_PROP = "LineStyle";

	/** True, if the connection is attached to its endpoints. */
	private boolean isConnected;

	/** Connection's source endpoint. */
	private Component source;

	/** Connection's target endpoint. */
	private Component target;

	private String sourceTerminal, targetTerminal;
	
	@XStreamOmitField
	private String recordCount;
	/**
	 * Instantiates a new link.
	 */
	public Link() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Attach source.
	 */
	public void attachSource() {
		if (getSource() == null || getSource().getSourceConnections().contains(this))
			return;

		getSource().connectOutput(this);
		updateSubjobVersionForAnyUpdation(this);
		if(this.getTarget()!=null )
			propagateSchema(this.getSource());
	}

	/**
	 * Attach target.
	 */
	public void attachTarget() {
		if (getTarget() == null || getTarget().getTargetConnections().contains(this))
			return;
		getTarget().connectInput(this);
		updateSubjobVersionForAnyUpdation(this);
			propagateSchema(this.getSource());
	}

	private void propagateSchema(Component sourceComponent) {
		if (sourceComponent.getProperties().get(Constants.SCHEMA_TO_PROPAGATE) != null)
			SchemaPropagation.INSTANCE.continuousSchemaPropagation(sourceComponent,
					(Map<String,ComponentsOutputSchema>) sourceComponent.getProperties().get(Constants.SCHEMA_TO_PROPAGATE));

	}

	/**
	 * Detach source.
	 */
	public void detachSource() {
		if (getSource() == null)
			return;
		getSource().disconnectOutput(this);
		updateSubjobVersionForAnyUpdation(this);
	}

	/**
	 * Detach target.
	 */
	public void detachTarget() {
		if (getTarget() == null)
			return;
		getTarget().disconnectInput(this);
		updateSubjobVersionForAnyUpdation(this);
	}

	public Component getSource() {
		return source;
	}

	public String getSourceTerminal() {
		return sourceTerminal;
	}

	public Component getTarget() {
		return target;
	}

	public String getTargetTerminal() {
		return targetTerminal;
	}

	public void setSource(Component e) {
		Object old = source;
		source = e;
		firePropertyChange("source", old, source);
	}

	public void setSourceTerminal(String s) {
		Object old = sourceTerminal;
		sourceTerminal = s;
		firePropertyChange("sourceTerminal", old, sourceTerminal);
	}

	public void setTarget(Component e) {
		Object old = target;
		target = e;
		firePropertyChange("target", old, target);
	}

	public void setTargetTerminal(String s) {
		Object old = targetTerminal;
		targetTerminal = s;
		firePropertyChange("targetTerminal", old, targetTerminal);
	}

	/**
	 * Returns the line drawing style of this connection.
	 * 
	 * @return an int value (Graphics.LINE_DASH or Graphics.LINE_SOLID)
	 */
	public int getLineStyle() {
		return lineStyle;
	}

	/**
	 * Set the line drawing style of this connection.
	 * 
	 * @param lineStyle
	 *            one of following values: Graphics.LINE_SOLID
	 * @see Graphics#LINE_SOLID
	 * @throws IllegalArgumentException
	 *             if lineStyle does not have one of the above values
	 */
	public void setLineStyle(int lineStyle) {
		if (lineStyle != Graphics.LINE_SOLID) {
			throw new IllegalArgumentException();
		}
		this.lineStyle = lineStyle;
		firePropertyChange(LINESTYLE_PROP, null, new Integer(this.lineStyle));
	}

	public int getLinkNumber() {
		return linkNumber;
	}

	public void setLinkNumber(int linkNumber) {
		this.linkNumber = linkNumber;
	}

	/**
	 * Reconnect.
	 */
	public void reconnect() {
		if (!isConnected) {
			source.connectOutput(this);
			source.engageOutputPort(sourceTerminal);

			isConnected = true;
		}
	}

	/**
	 * Reconnect.
	 * 
	 * @param newSource
	 *            the new source
	 * @param sourceTerminal
	 *            the source terminal
	 */
	// Reconnect to different Source or Target
	public void reconnect(Component newSource, String sourceTerminal) {
		if (newSource == null && sourceTerminal == null) {
			throw new IllegalArgumentException();
		}
		detachSource();
		this.source = newSource;
		this.sourceTerminal = sourceTerminal;
		reconnect();
	}

	private void applySchemaToTarget(Component sourceComponent, Component targetComponent) {

		if (targetComponent.getComponentName().equalsIgnoreCase("CloneComponent")
				|| targetComponent.getComponentName().equalsIgnoreCase("UnionallComponent")) {
			targetComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE,
					sourceComponent.getProperties().get(Constants.SCHEMA_TO_PROPAGATE));
		}
	}
	
	private void updateSubjobVersionForAnyUpdation(Link link) {
		link.getSource().getParent().updateSubjobVersion();
	}
	
	public String getRecordCount() {
		return recordCount;
	}
	
	public void updateRecordCount(String count) {
		this.recordCount = count;
		firePropertyChange("record_count", null, count);
	}

}
