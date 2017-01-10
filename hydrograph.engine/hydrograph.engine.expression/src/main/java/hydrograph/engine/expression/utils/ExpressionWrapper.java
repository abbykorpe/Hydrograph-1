package hydrograph.engine.expression.utils;

import hydrograph.engine.expression.api.ValidationAPI;

/**
 * Created by gurdits on 1/6/2017.
 */
public class ExpressionWrapper {

    private ValidationAPI validationAPI;
    private String intialValueExpression;

    public ExpressionWrapper(ValidationAPI validationAPI,String intialValue){
        this.validationAPI=validationAPI;
        this.intialValueExpression =intialValue;
    }

    public ValidationAPI getValidationAPI() {
        return validationAPI;
    }

    public String getIntialValueExpression() {
        return intialValueExpression;
    }
}
