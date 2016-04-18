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

 
package hydrograph.ui.engine.ui.converter;

import hydrograph.ui.engine.ui.converter.impl.AggregateUiConverter;
import hydrograph.ui.engine.ui.converter.impl.CloneUiConverter;
import hydrograph.ui.engine.ui.converter.impl.CumulateUiConverter;
import hydrograph.ui.engine.ui.converter.impl.DiscardUiConverter;
import hydrograph.ui.engine.ui.converter.impl.DummyUiConverter;
import hydrograph.ui.engine.ui.converter.impl.FilterUiConverter;
import hydrograph.ui.engine.ui.converter.impl.GenerateRecordsUiConverter;
import hydrograph.ui.engine.ui.converter.impl.InputFileDelimitedUiConverter;
import hydrograph.ui.engine.ui.converter.impl.InputFixedWidthUiConverter;
import hydrograph.ui.engine.ui.converter.impl.InputHiveParquetUiConverter;
import hydrograph.ui.engine.ui.converter.impl.InputHiveTextFileUiConverter;
import hydrograph.ui.engine.ui.converter.impl.InputParquetUiConverter;
import hydrograph.ui.engine.ui.converter.impl.JoinComponentUiConverter;
import hydrograph.ui.engine.ui.converter.impl.LimitUiConverter;
import hydrograph.ui.engine.ui.converter.impl.LookupUiConverter;
import hydrograph.ui.engine.ui.converter.impl.NormalizeUiConverter;
import hydrograph.ui.engine.ui.converter.impl.OutputFileDelimitedUiConverter;
import hydrograph.ui.engine.ui.converter.impl.OutputFixedWidthUiConverter;
import hydrograph.ui.engine.ui.converter.impl.OutputHiveParquetUiConverter;
import hydrograph.ui.engine.ui.converter.impl.OutputHiveTextFileUiConverter;
import hydrograph.ui.engine.ui.converter.impl.OutputParquetUiConverter;
import hydrograph.ui.engine.ui.converter.impl.RemoveDupsUiConverter;
import hydrograph.ui.engine.ui.converter.impl.SortUiConverter;
import hydrograph.ui.engine.ui.converter.impl.TransformComponentUiConverter;
import hydrograph.ui.engine.ui.converter.impl.UnionAllUiConverter;
import hydrograph.ui.engine.ui.converter.impl.UniqueSequenceUiConverter;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.logging.factory.LogFactory;

import org.slf4j.Logger;

import hydrograph.engine.jaxb.inputtypes.ParquetHiveFile;
import hydrograph.engine.jaxb.inputtypes.HiveTextFile;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.GenerateRecord;
import hydrograph.engine.jaxb.inputtypes.TextFileDelimited;
import hydrograph.engine.jaxb.inputtypes.TextFileFixedWidth;
import hydrograph.engine.jaxb.operationstypes.Aggregate;
import hydrograph.engine.jaxb.operationstypes.Cumulate;
import hydrograph.engine.jaxb.operationstypes.Filter;
import hydrograph.engine.jaxb.operationstypes.GenerateSequence;
import hydrograph.engine.jaxb.operationstypes.HashJoin;
import hydrograph.engine.jaxb.operationstypes.Join;
import hydrograph.engine.jaxb.operationstypes.Normalize;
import hydrograph.engine.jaxb.operationstypes.Transform;
import hydrograph.engine.jaxb.outputtypes.Discard;
import hydrograph.engine.jaxb.straightpulltypes.Clone;
import hydrograph.engine.jaxb.straightpulltypes.Limit;
import hydrograph.engine.jaxb.straightpulltypes.RemoveDups;
import hydrograph.engine.jaxb.straightpulltypes.Sort;
import hydrograph.engine.jaxb.straightpulltypes.UnionAll;

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
		if ((hydrograph.engine.jaxb.outputtypes.TextFileDelimited.class)
				.isAssignableFrom(typeBaseComponent.getClass())) {
			return new OutputFileDelimitedUiConverter(typeBaseComponent, container);
		}
		if ((TextFileDelimited.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new InputFileDelimitedUiConverter(typeBaseComponent, container);
		}
		if ((hydrograph.engine.jaxb.outputtypes.TextFileFixedWidth.class).isAssignableFrom(typeBaseComponent
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
		if ((Cumulate.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new CumulateUiConverter(typeBaseComponent, container);
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
		if((hydrograph.engine.jaxb.outputtypes.ParquetHiveFile.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new OutputHiveParquetUiConverter(typeBaseComponent,container);
		}
		if((ParquetHiveFile.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new InputHiveParquetUiConverter(typeBaseComponent, container);
		}
		if((hydrograph.engine.jaxb.outputtypes.HiveTextFile.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new OutputHiveTextFileUiConverter(typeBaseComponent,container);
		}
		if((HiveTextFile.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new InputHiveTextFileUiConverter(typeBaseComponent, container);
		}
		if((Normalize.class).isAssignableFrom(typeBaseComponent.getClass())){
			return new NormalizeUiConverter(typeBaseComponent, container);
		}
		if((hydrograph.engine.jaxb.inputtypes.ParquetFile.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new InputParquetUiConverter(typeBaseComponent, container);
		}	
		if((hydrograph.engine.jaxb.outputtypes.ParquetFile.class).isAssignableFrom(typeBaseComponent.getClass())) {
			return new OutputParquetUiConverter(typeBaseComponent, container);
		}
		return new DummyUiConverter(typeBaseComponent,container);
	}
}