package com.penglecode.codeforce.common.codegen.api;

import com.penglecode.codeforce.common.codegen.support.CodegenMethodParameter;

/**
 * API接口方法生成参数
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class ApiMethodParameter extends CodegenMethodParameter {

    /** 输入DTO参数名 */
    private String inputApiModelName;

    /** 输出DTO参数名 */
    private String outputApiModelName;

    public String getInputApiModelName() {
        return inputApiModelName;
    }

    public void setInputApiModelName(String inputApiModelName) {
        this.inputApiModelName = inputApiModelName;
    }

    public String getOutputApiModelName() {
        return outputApiModelName;
    }

    public void setOutputApiModelName(String outputApiModelName) {
        this.outputApiModelName = outputApiModelName;
    }

}
