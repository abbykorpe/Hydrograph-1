package com.bitwise.app.engine.ui.converter.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.StraightfullUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.RemovedupsComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.removedups.TypePrimaryKeyFields;
import com.bitwiseglobal.graph.removedups.TypeSecondaryKeyFields;
import com.bitwiseglobal.graph.removedups.TypeSecondayKeyFieldsAttributes;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;

public class RemoveDupsUiConverter extends StraightfullUiConverter {
		private RemoveDups removeDups;
		private static final String COMPONENT_NAME_SUFFIX = "RemoveDups_";
		private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(RemoveDupsUiConverter.class);
		
		public RemoveDupsUiConverter(TypeBaseComponent typeBaseComponent,
				Container container) {
			this.container = container;
			this.typeBaseComponent = typeBaseComponent;
			this.uiComponent = new RemovedupsComponent();
			this.propertyMap = new LinkedHashMap<>();
		}

		@Override
		public void prepareUIXML() {

			super.prepareUIXML();
			LOGGER.debug("Fetching RemoveDups-Properties for -{}",COMPONENT_NAME);
			removeDups = (RemoveDups) typeBaseComponent;
			
			propertyMap.put(PropertyNameConstants.RETENTION_LOGIC_KEEP.value(),removeDups.getKeep().getValue().value());
			propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
			propertyMap.put(PropertyNameConstants.DEDUP_FILEDS.value(),getPrimaryKeys());
			propertyMap.put(PropertyNameConstants.SECONDARY_COLUMN_KEYS.value(), getSecondaryKeys());
			
			uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY	.value());
			container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
			container.getComponentNames().add(COMPONENT_NAME);
			uiComponent.setProperties(propertyMap);
			uiComponent.setType(UIComponentsConstants.REMOVE_DUPS.value());
			uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY.value());
		
		}

		private Map<String,String> getSecondaryKeys() {
			LOGGER.debug("Fetching RemoveDups-Secondary-Keys-Properties for -{}",COMPONENT_NAME);
			Map<String,String> secondaryKeyMap=null;
			removeDups = (RemoveDups) typeBaseComponent;
			TypeSecondaryKeyFields  typeSecondaryKeyFields=removeDups.getSecondaryKeys() ;
			
			if(typeSecondaryKeyFields!=null){
				secondaryKeyMap=new TreeMap<String, String>();
				for(TypeSecondayKeyFieldsAttributes secondayKeyFieldsAttributes:typeSecondaryKeyFields.getField()){
					secondaryKeyMap.put(secondayKeyFieldsAttributes.getName(), secondayKeyFieldsAttributes.getOrder().value());
				
				}
			}
			
			return secondaryKeyMap;
		}

		private HashSet<String> getPrimaryKeys() {
			LOGGER.debug("Fetching RemoveDups-Primary-Keys-Properties for -{}",COMPONENT_NAME);
			HashSet<String> primaryKeySet =null;
			removeDups = (RemoveDups) typeBaseComponent;
			TypePrimaryKeyFields typePrimaryKeyFields = removeDups.getPrimaryKeys();
			if(typePrimaryKeyFields!=null){	
				
				primaryKeySet=new  HashSet<String>();
					for(TypeFieldName fieldName:typePrimaryKeyFields.getField()){
						primaryKeySet.add(fieldName.getName());
				}
			}
			return primaryKeySet;
		}

		

		
	}
