package hydrograph.engine.phasebreak.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobConf;

import cascading.tap.hadoop.Hfs;
import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.core.entity.LinkInfo;
import hydrograph.engine.core.utilities.SocketUtilities;
import hydrograph.engine.flow.utils.FlowManipulationContext;
import hydrograph.engine.flow.utils.ManipulatorListener;
import hydrograph.engine.jaxb.commontypes.FieldDataTypes;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseRecord;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.inputtypes.SequenceInputFile;
import hydrograph.engine.jaxb.inputtypes.SequenceInputFile.Path;
import hydrograph.engine.jaxb.outputtypes.SequenceOutputFile;

public class PhaseBreakPlugin implements ManipulatorListener {

	private Map<String, Set<SchemaField>> schemaFieldsMap;
	private List<TypeBaseComponent> jaxbGraph;
	private List<LinkInfo> phaseChangeOriginalLinks;
	private List<LinkInfo> phaseChangeLinks;
	private static final String DEFAULT_OUT_SOCKET = "out0";
	private static final String DEFAULT_IN_SOCKET = "in0";
	private Configuration conf;
	private List<String> tempPathList;

	@Override
	public List<TypeBaseComponent> execute(FlowManipulationContext manipulationContext) {
		tempPathList=new ArrayList<String>();
		conf = manipulationContext.getConf();
		jaxbGraph = manipulationContext.getJaxbMainGraph();
		schemaFieldsMap = manipulationContext.getSchemaFieldMap();
		this.phaseChangeOriginalLinks = new ArrayList<LinkInfo>();
		this.phaseChangeLinks = new ArrayList<LinkInfo>();
		updatePhase();
		populatePhaseChangeComponents();
		updateLinksAndComponents(jaxbGraph);
		manipulationContext.setTmpPath(tempPathList);
		return jaxbGraph;
	}

	/**
	 * Update subphase of all the components in the job. Phase attribute should
	 * be present on all the components. It throws RuntimeException if phase of
	 * source component is greater than phase of target component.
	 * 
	 */
	private void updatePhase() {
		HashSet<String> componentsTraversed = new HashSet<String>();
		for (TypeBaseComponent jaxbComponent : jaxbGraph) {
			updatePhaseOfInputFlow(jaxbComponent, componentsTraversed);
		}
	}

	private void updatePhaseOfInputFlow(TypeBaseComponent jaxbComponent, HashSet<String> componentsTraversed) {

		if (!componentsTraversed.contains(jaxbComponent.getId())) {
			List<? extends TypeBaseInSocket> inSocketList = SocketUtilities.getInSocketList(jaxbComponent);

			for (TypeBaseInSocket typeBaseInSocket : inSocketList) {
				String phase = jaxbComponent.getPhase();
				int phaseLevel = phase.split("\\.").length;

				TypeBaseComponent sourceComponent = getComponent(typeBaseInSocket.getFromComponentId());

				updatePhaseOfInputFlow(sourceComponent, componentsTraversed);
				for (int j = 0; j < phaseLevel; j++) {
					if (Integer.parseInt(phase.split("\\.")[j]) < Integer
							.parseInt(sourceComponent.getPhase().split("\\.")[j])) {
						// if main phase is less than source component main
						// phase then throw error
						// else update the subphase of this component with the
						// subphase of source component
						if (j > 0) {
							jaxbComponent.setPhase(sourceComponent.getPhase());
						} else {
							throw new RuntimeException(
									"Phase of source component cannot be greater than target component. Source component '"
											+ sourceComponent.getId() + "' has phase " + sourceComponent.getPhase()
											+ " and target component '" + jaxbComponent.getId() + "' has phase "
											+ jaxbComponent.getPhase());
						}
					}
				}

			}
			componentsTraversed.add(jaxbComponent.getId());
		}
	}

	public TypeBaseComponent getComponentFromComponentId(String componentId) {
		for (TypeBaseComponent component : jaxbGraph) {
			if (component.getId().equals(componentId)) {
				return component;
			}
		}
		throw new GraphTraversalException("Component not present for the component id: " + componentId);
	}

	private String getTempPath(String prefix, Configuration jobConf) {
		String name = prefix + "_" + UUID.randomUUID().toString();
		name = name.replaceAll("\\s+|\\*|\\+|/+", "_");
		String tempPath = (new org.apache.hadoop.fs.Path(Hfs.getTempPath(jobConf), name)).toString();
		tempPathList.add(tempPath);
		return tempPath;

	}

	private void updateLinksAndComponents(List<TypeBaseComponent> jaxbGraph2) {
		int counter = 0;

		for (LinkInfo link2 : phaseChangeLinks) {
			TypeBaseComponent targetComponent = this.getComponentFromComponentId(link2.getComponentId());
			String tempPath = getTempPath(targetComponent.getId(), conf);
			TypeBaseInSocket inSocket = link2.getInSocket();
			Set<SchemaField> schemaFields = schemaFieldsMap
					.get(link2.getInSocket().getFromComponentId() + "_" + link2.getInSocket().getFromSocketId());
			String sequenceInputComponentId = targetComponent.getId() + "_" + counter + "_phase_"
					+ targetComponent.getPhase();

			SequenceInputFile jaxbSequenceInputFile = new SequenceInputFile();
			jaxbSequenceInputFile.setId(sequenceInputComponentId);
			jaxbSequenceInputFile.setPhase(targetComponent.getPhase());
			Path inPath = new Path();
			inPath.setUri(tempPath);
			jaxbSequenceInputFile.setPath(inPath);
			TypeInputOutSocket outputSocket = new TypeInputOutSocket();
			outputSocket.setId(DEFAULT_OUT_SOCKET);
			TypeBaseRecord record = new TypeBaseRecord();
			for (SchemaField field : schemaFields) {

				TypeBaseField typeBaseField = new TypeBaseField();
				typeBaseField.setName(field.getFieldName());
				typeBaseField.setType(FieldDataTypes.fromValue(field.getFieldDataType()));
				setFieldScale(field, typeBaseField);
				setFieldPrecision(field, typeBaseField);
				// setFieldScaleType(schemaField, typeBaseField);
				setFieldFormat(field, typeBaseField);
				record.getFieldOrRecordOrIncludeExternalSchema().add(typeBaseField);
			}
			outputSocket.setSchema(record);
			jaxbSequenceInputFile.getOutSocket().add(outputSocket);

			jaxbGraph2.add(jaxbSequenceInputFile);

			inSocket.setFromComponentId(sequenceInputComponentId);
			inSocket.setFromSocketId(DEFAULT_OUT_SOCKET);

			// SocketUtilities.replaceInSocket(targetComponent,
			// inSocket.getId(), newInSocket);
			counter++;

			TypeBaseComponent sourceComponent = this.getComponentFromComponentId(link2.getSourceComponentId());

			TypeBaseOutSocket outSocket = link2.getOutSocket();

			SequenceOutputFile jaxbSequenceOutputFile = new SequenceOutputFile();

			String sequenceOutputComponentId = sourceComponent.getId() + "_" + counter + "_phase_"
					+ sourceComponent.getPhase();
			jaxbSequenceOutputFile.setId(sequenceOutputComponentId);

			jaxbSequenceOutputFile.setPhase(sourceComponent.getPhase());
			hydrograph.engine.jaxb.outputtypes.SequenceOutputFile.Path outPath = new hydrograph.engine.jaxb.outputtypes.SequenceOutputFile.Path();
			outPath.setUri(tempPath);
			jaxbSequenceOutputFile.setPath(outPath);
			TypeOutputInSocket outputInSocket = new TypeOutputInSocket();

			outputInSocket.setFromComponentId(sourceComponent.getId());
			outputInSocket.setFromSocketId(outSocket.getId());
			outputInSocket.setId(DEFAULT_IN_SOCKET);

			TypeBaseRecord record2 = new TypeBaseRecord();
			for (SchemaField field : schemaFields) {

				TypeBaseField typeBaseField = new TypeBaseField();
				typeBaseField.setName(field.getFieldName());
				typeBaseField.setType(FieldDataTypes.fromValue(field.getFieldDataType()));
				setFieldScale(field, typeBaseField);
				setFieldPrecision(field, typeBaseField);
				// setFieldScaleType(schemaField, typeBaseField);
				setFieldFormat(field, typeBaseField);
				record2.getFieldOrRecordOrIncludeExternalSchema().add(typeBaseField);
			}
			outputInSocket.setSchema(record2);
			jaxbSequenceOutputFile.getInSocket().add(outputInSocket);

			jaxbGraph2.add(jaxbSequenceOutputFile);

			counter++;

			addInSocketToOriginalLinks(sourceComponent.getId(), outSocket.getId(), inSocket);
			addOutSocketToOriginalLinks(targetComponent.getId(), inSocket.getId(), outputSocket);

		}
	}

	public static void setFieldScale(SchemaField schemaField, TypeBaseField typeBaseField) {
		typeBaseField.setScale(schemaField.getFieldScale());
	}

	public static void setFieldPrecision(SchemaField schemaField, TypeBaseField typeBaseField) {
		typeBaseField.setPrecision(schemaField.getFieldPrecision());
	}

	public static void setFieldFormat(SchemaField schemaField, TypeBaseField typeBaseField) {
		if (schemaField.getFieldFormat() != null) {
			if (schemaField.getFieldDataType().toLowerCase().contains("date")) {
				typeBaseField.setFormat("yyyy-MM-dd HH:mm:ss");
			} else {
				typeBaseField.setFormat(schemaField.getFieldFormat());
			}
		}
	}

	private void addInSocketToOriginalLinks(String sourceComponentId, String outSocketId, TypeBaseInSocket inSocket) {
		for (LinkInfo link2 : phaseChangeOriginalLinks) {
			if (link2.getOutSocketId().equals(outSocketId) && link2.getSourceComponentId().equals(sourceComponentId)) {
				link2.setInSocket(inSocket);
			}
		}

	}

	private void addOutSocketToOriginalLinks(String targetComponentId, String inSocketPortId,
			TypeInputOutSocket outputSocket) {
		for (LinkInfo link2 : phaseChangeOriginalLinks) {
			if (link2.getInSocketId().equals(inSocketPortId) && link2.getComponentId().equals(targetComponentId)) {
				link2.setOutSocket(outputSocket);
			}
		}

	}

	private void populatePhaseChangeComponents() {

		for (TypeBaseComponent component : jaxbGraph) {
			List<? extends TypeBaseInSocket> inSocketList = SocketUtilities.getInSocketList(component);

			String phase = component.getPhase();

			for (TypeBaseInSocket inSocket : inSocketList) {
				// get the dependent component
				TypeBaseComponent sourceComponent = getComponent(inSocket.getFromComponentId());
				if (sourceComponent.getPhase().compareTo(phase) > 0) {
					throw new GraphTraversalException(
							"Phase of source component cannot be greator then target component. Source component "
									+ sourceComponent.getId() + " has phase " + sourceComponent.getPhase()
									+ " and target component " + component.getId() + " has phase " + phase);
				}

				TypeBaseOutSocket outSocket = SocketUtilities.getOutSocket(sourceComponent, inSocket.getFromSocketId());

				LinkInfo link = new LinkInfo(component.getId(), inSocket.getId(), inSocket, sourceComponent.getId(),
						outSocket.getId(), outSocket);

				if (sourceComponent.getPhase().compareTo(phase) < 0) {
					phaseChangeLinks.add(link);
				}
				phaseChangeOriginalLinks.add(link);
			}
		}
	}

	private TypeBaseComponent getComponent(String componentID) {
		for (TypeBaseComponent component : jaxbGraph) {
			if (component.getId().equals(componentID))
				return component;
		}
		throw new GraphTraversalException("Component not found with id '" + componentID + "'");
	}

	private class GraphTraversalException extends RuntimeException {

		private static final long serialVersionUID = -2396594973435552339L;

		public GraphTraversalException(String msg) {
			super(msg);
		}

		public GraphTraversalException(Throwable e) {
			super(e);
		}

		public GraphTraversalException(String msg, Throwable e) {
			super(msg, e);
		}
	}

}
