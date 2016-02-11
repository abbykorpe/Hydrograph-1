package com.bitwise.app.graph.job;

import org.eclipse.core.commands.AbstractHandler;
/**
 * 
 * The class is responsible for providing references to run and stop job button
 * 
 * @author Bitwise
 *
 */

public enum RunStopButtonCommunicator{
	RunJob {
		private AbstractHandler abstractHandler;
		
		AbstractHandler getAbstractHandler() {
			return abstractHandler;
		}
		void setAbstractHandler(AbstractHandler abstractHandler){
			this.abstractHandler = abstractHandler;
		}		
	},
	
	StopJob {
		private AbstractHandler abstractHandler;
		
		AbstractHandler getAbstractHandler() {
			return abstractHandler;
		}
		void setAbstractHandler(AbstractHandler abstractHandler){
			this.abstractHandler = abstractHandler;
		}		
	};
	
	abstract AbstractHandler getAbstractHandler();
	abstract void setAbstractHandler(AbstractHandler abstractHandler);
	
	/**
	 * returns button handler
	 * 
	 * @return
	 */
	public AbstractHandler getHandler() {
		return getAbstractHandler();
	}
	
	/**
	 * set button handler
	 * 
	 * @param abstractHandler
	 */
	public void setHandler(AbstractHandler abstractHandler){
		setAbstractHandler(abstractHandler);
	};
	
}

