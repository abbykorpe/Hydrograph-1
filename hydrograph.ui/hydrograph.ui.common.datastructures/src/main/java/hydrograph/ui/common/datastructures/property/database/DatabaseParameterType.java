package hydrograph.ui.common.datastructures.property.database;

public class DatabaseParameterType {

	private String hostName = "";
	private String portNo = "";
	private String userName = "";
	private String password = "";
	private String databaseName = "";
	private String jdbcName = "";
	private String schemaName = "";
	private String dataBaseType;
	
	
	private DatabaseParameterType(DatabaseBuilder builder){
		this.dataBaseType = builder.dataBaseType;
		this.hostName = builder.hostName;
		this.portNo = builder.portNo;
		this.userName = builder.userName;
		this.password = builder.password;
		this.databaseName = builder.databaseName;
		this.jdbcName = builder.jdbcName;
		this.schemaName = builder.schemaName;
	}
	public static class DatabaseBuilder{
		//required fields
		private String dataBaseType;
		private String hostName = "";
		private String portNo = "";
		private String userName = "";
		private String password = "";
		
		//optional fields
		private String jdbcName = "";
		private String schemaName = "";
		private String databaseName = "";
		
		public DatabaseBuilder(String dataBaseType,String hostName, String portNo, String userName, String password){
			this.dataBaseType = dataBaseType;
			this.hostName = hostName;
			this.portNo = portNo;
			this.userName = userName;
			this.password = password;
		}
		
		public DatabaseParameterType build(){
			return new DatabaseParameterType(this);
		}
		
		public DatabaseBuilder jdbcName(String jdbcName){
			this.jdbcName = jdbcName;
			return this;
		}
		
		public DatabaseBuilder schemaName(String schemaName){
			this.schemaName = schemaName;
			return this;
		}
		
		public DatabaseBuilder databaseName(String databaseName){
			this.databaseName = databaseName;
			return this;
		}
		
		public DatabaseBuilder copy(DatabaseParameterType parameterType){
			this.hostName = parameterType.hostName;
			return this;
		}
	}
		
	public String getJdbcName(){
		return jdbcName;
	}
	
	public String getSchemaName(){
		return schemaName;
	}
	
	public String getDataBaseType(){
		return dataBaseType;
	}
	
	public String getHostName(){
		return hostName;
	}
	
	public String getPortNo(){
		return portNo;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getDatabaseName(){
		return databaseName;
	}
	
	public String getPassword(){
		return password;
	}
	
}
