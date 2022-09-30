package com.penglecode.codeforce.common.codegen.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 领域服务参数
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainServiceParameters extends ArrayList<DomainServiceParameter> {

    private static final long serialVersionUID = 1L;

    private final DomainServiceParameter masterDomainServiceParameter;

    private final List<DomainServiceParameter> slaveDomainServiceParameters;

    public DomainServiceParameters(DomainServiceParameter masterDomainServiceParameter, List<DomainServiceParameter> slaveDomainServiceParameters) {
        this.masterDomainServiceParameter = masterDomainServiceParameter;
        this.slaveDomainServiceParameters = slaveDomainServiceParameters;
        List<DomainServiceParameter> allDomainServiceParameters = new ArrayList<>();
        allDomainServiceParameters.add(masterDomainServiceParameter);
        allDomainServiceParameters.addAll(slaveDomainServiceParameters);
        addAll(allDomainServiceParameters);
    }

    public DomainServiceParameter getMasterDomainServiceParameter() {
        return masterDomainServiceParameter;
    }

    public List<DomainServiceParameter> getSlaveDomainServiceParameters() {
        return slaveDomainServiceParameters;
    }

}
