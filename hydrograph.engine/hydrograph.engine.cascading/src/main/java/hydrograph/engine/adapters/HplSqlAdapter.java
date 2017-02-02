package hydrograph.engine.adapters;

import hydrograph.engine.adapters.base.CommandAdapterBase;
import hydrograph.engine.commandtype.component.BaseCommandComponent;
import hydrograph.engine.commandtype.component.HplSqlComponent;
import hydrograph.engine.core.component.generator.HplSqlEntityGenerator;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

public class HplSqlAdapter extends CommandAdapterBase{
	
	
	private static final long serialVersionUID = -7068571351102960755L;
	HplSqlComponent hplSqlComponent;
	HplSqlEntityGenerator entityGenerator;
	
	public HplSqlAdapter(TypeBaseComponent typeCommandComponent) {
	
		entityGenerator=new HplSqlEntityGenerator(typeCommandComponent);
	}
	

	@Override
	public BaseCommandComponent getComponent() {
		try {
			hplSqlComponent=new HplSqlComponent(entityGenerator.getEntity());
		} catch (Throwable e) {
			throw new RuntimeException("Assembly Creation Error for HplSql Component : " + e.getMessage());
			
		}
		
		return null;
	}

	

}
