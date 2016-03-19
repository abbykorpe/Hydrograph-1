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

 
package com.bitwise.app.engine.ui.converter;

import org.slf4j.Logger;

import com.bitwise.app.engine.ui.converter.impl.AggregateUiConverter;
import com.bitwise.app.engine.ui.converter.impl.CloneUiConverter;
import com.bitwise.app.engine.ui.converter.impl.DiscardUiConverter;
import com.bitwise.app.engine.ui.converter.impl.DummyUiConverter;
import com.bitwise.app.engine.ui.converter.impl.FilterUiConverter;
import com.bitwise.app.engine.ui.converter.impl.GenerateRecordsUiConverter;
import com.bitwise.app.engine.ui.converter.impl.InputFileDelimitedUiConverter;
import com.bitwise.app.engine.ui.converter.impl.InputFixedWidthUiConverter;
import com.bitwise.app.engine.ui.converter.impl.JoinComponentUiConverter;
import com.bitwise.app.engine.ui.converter.impl.LimitUiConverter;
import com.bitwise.app.engine.ui.converter.impl.LookupUiConverter;
import com.bitwise.app.engine.ui.converter.impl.OutputFileDelimitedUiConverter;
import com.bitwise.app.engine.ui.converter.impl.OutputFixedWidthUiConverter;
import com.bitwise.app.engine.ui.converter.impl.RemoveDupsUiConverter;
import com.bitwise.app.engine.ui.converter.impl.SortUiConverter;
import com.bitwise.app.engine.ui.converter.impl.TransformComponentUiConverter;
import com.bitwise.app.engine.ui.converter.impl.UnionAllUiConverter;
import com.bitwise.app.engine.ui.converter.impl.UniqueSequenceUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.inputtypes.GenerateRecord;
import com.bitwiseglobal.graph.inputtypes.TextFileDelimited;
import com.bitwiseglobal.graph.inputtypes.TextFileFixedWidth;
import com.bitwiseglobal.graph.operationstypes.Aggregate;
import com.bitwiseglobal.graph.operationstypes.Filter;
import com.bitwiseglobal.graph.operationstypes.GenerateSequence;
import com.bitwiseglobal.graph.operationstypes.HashJoin;
import com.bitwiseglobal.graph.operationstypes.Join;
import com.bitwiseglobal.graph.operationstypes.Transform;
import com.bitwiseglobal.graph.outputtypes.Discard;
import com.bitwiseglobal.graph.straightpulltypes.Clone;
import com.bitwiseglobal.graph.straightpulltypes.Limit;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;
import com.bitwiseglobal.graph.straightpulltypes.Sort;
import com.bitwiseglobal.graph.straightpulltypes.UnionAll;

/**
 * The class UiConverterFactory Factory class for creating Converter instances for particular component
 * 
 * @author Bitwise
 * 
 */

public class UiConverterFactory {
	public static final UiConverterFactory INSTANCE = new UiConverterFactory();
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UiConverterFactory.class);

	private UiConverterFactory() {

	}

	/**
	 * Instantiate specific ui-converter.
	 * 
	 * @param typeBaseComponent
	 * 
	 * @param container
	 * 
	 * @return UiConverter, specific ui-converter.
	 */
	public UiConverter getUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		LOGGER.debug("Getting Ui-Converter for component:{}", typeBaseComponent.getClass());
		if ((com.bitwiseglobal.graph.outputtypes.TextFileDelimited.class)
				.isAssignableFrom(typeBaseComponent.getClass())) {
			return new OutputFileDelimitedUiConverter(typeBaseComponent, container);
		}
		if ((TextFileDelimited.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new InputFileDelimitedUiConverter(typeBaseComponent, container);
		}
		if ((com.bitwiseglobal.graph.outputtypes.TextFileFixedWidth.class).isAssignableFrom(typeBaseComponent
				.getClass())) {
			return new OutputFixedWidthUiConverter(typeBaseComponent, container);
		}
		if ((TextFileFixedWidth.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new InputFixedWidthUiConverter(typeBaseComponent, container);
		}
		if ((Clone.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new CloneUiConverter(typeBaseComponent, container);
		}
		if ((UnionAll.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new UnionAllUiConverter(typeBaseComponent, container);
		}
		if ((RemoveDups.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new RemoveDupsUiConverter(typeBaseComponent, container);
		}
		if ((Filter.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new FilterUiConverter(typeBaseComponent, container);
		}
		if ((Aggregate.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new AggregateUiConverter(typeBaseComponent, container);
		}
		if ((Transform.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new TransformComponentUiConverter(typeBaseComponent, container);
		}
		if ((Join.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new JoinComponentUiConverter(typeBaseComponent, container);
		}
		if ((HashJoin.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new LookupUiConverter(typeBaseComponent, container);
		}
		if ((GenerateRecord.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new GenerateRecordsUiConverter(typeBaseComponent, container);
		}
		if ((GenerateSequence.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new UniqueSequenceUiConverter(typeBaseComponent, container);
		}
		if((Limit.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new LimitUiConverter(typeBaseComponent,container);
		}
		if((Sort.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new SortUiConverter(typeBaseComponent,container);
 		}
		if((Discard.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new DiscardUiConverter(typeBaseComponent,container);
 		}
		return new DummyUiConverter(typeBaseComponent,container);
	}
}
