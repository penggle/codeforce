package com.penglecode.codeforce.common.codegen.support;

import com.penglecode.codeforce.common.codegen.config.DomainObjectConfig;
import com.penglecode.codeforce.common.codegen.config.GenerableTargetConfig;
import com.penglecode.codeforce.common.codegen.config.ModuleCodegenConfigProperties;

/**
 * 代码生成上下文
 *
 * @author pengpeng
 * @version 1.0
 */
public class CodegenContext<C extends ModuleCodegenConfigProperties, T extends GenerableTargetConfig, D extends DomainObjectConfig> {

    /** 代码生成配置 */
    private final C codegenConfig;

    /** 当前生成目标配置 */
    private final T targetConfig;

    /** 当前生成目标绑定的领域对象 */
    private final D domainObjectConfig;

    public CodegenContext(C codegenConfig, T targetConfig, D domainObjectConfig) {
        this.codegenConfig = codegenConfig;
        this.targetConfig = targetConfig;
        this.domainObjectConfig = domainObjectConfig;
    }

    public C getCodegenConfig() {
        return codegenConfig;
    }

    public T getTargetConfig() {
        return targetConfig;
    }

    public D getDomainObjectConfig() {
        return domainObjectConfig;
    }

}
