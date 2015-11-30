package com.bitwise.app.engine.ui.converter;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.converter.impl.AggregateUiConverter;
import com.bitwise.app.engine.ui.converter.impl.CloneUiConverter;
import com.bitwise.app.engine.ui.converter.impl.FilterUiConverter;
import com.bitwise.app.engine.ui.converter.impl.InputFileDelimitedUiConverter;
import com.bitwise.app.engine.ui.converter.impl.InputFixedWidthUiConverter;
import com.bitwise.app.engine.ui.converter.impl.OutputFileDelimitedUiConverter;
import com.bitwise.app.engine.ui.converter.impl.OutputFixedWidthUiConverter;
import com.bitwise.app.engine.ui.converter.impl.RemoveDupsUiConverter;
import com.bitwise.app.engine.ui.converter.impl.TransformComponentUiConverter;
import com.bitwise.app.engine.ui.converter.impl.UnionAllUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.inputtypes.TextFileDelimited;
import com.bitwiseglobal.graph.inputtypes.TextFileFixedWidth;
import com.bitwiseglobal.graph.operationstypes.Aggregate;
import com.bitwiseglobal.graph.operationstypes.Filter;
import com.bitwiseglobal.graph.operationstypes.Transform;
import com.bitwiseglobal.graph.straightpulltypes.Clone;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;
import com.bitwiseglobal.graph.straightpulltypes.UnionAll;

/**
 * Factory class for creating Converter instances for particular component
 * 
 */
public class UIConverterFactory {
	public static final UIConverterFactory INSTANCE = new UIConverterFactory();
	private static final Logger logger = LogFactory.INSTANCE
			.getLogger(UIConverterFactory.class);

	private UIConverterFactory() {

	}

	public UIConverter getUiConverter(TypeBaseComponent typeBaseComponent,Container container) {
		
		if (typeBaseComponent instanceof com.bitwiseglobal.graph.outputtypes.TextFileDelimited) {
			return new OutputFileDelimitedUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof TextFileDelimited) {
			return new InputFileDelimitedUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof com.bitwiseglobal.graph.outputtypes.TextFileFixedWidth) {
			return new OutputFixedWidthUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof TextFileFixedWidth) {
			return new InputFixedWidthUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof Clone) {
			return new CloneUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof UnionAll) {
			return new UnionAllUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof RemoveDups) {
			return new RemoveDupsUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof Filter) {
			return new FilterUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof Aggregate) {
			return new AggregateUiConverter(typeBaseComponent,container);
		}
		if (typeBaseComponent instanceof Transform) {
			return new TransformComponentUiConverter(typeBaseComponent,container);
		}
		return null;
	}
}
