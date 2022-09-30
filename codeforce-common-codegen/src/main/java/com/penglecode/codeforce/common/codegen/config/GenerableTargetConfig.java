package com.penglecode.codeforce.common.codegen.config;

/**
 * 能自动生成的目标配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
public abstract class GenerableTargetConfig {

    /** 默认代码src目录：当前项目下的src/main/java */
    public static final String DEFAULT_TARGET_PROJECT = "src/main/java";

    /** 输出代码的作者 */
    private String targetAuthor;

    /** 输出代码的时间 */
    private String targetVersion;

    /** 代码输出的项目位置 */
    private String targetProject;

    /** 代码输出的包路径 */
    private String targetPackage;

    public String getTargetAuthor() {
        return targetAuthor;
    }

    public void setTargetAuthor(String targetAuthor) {
        this.targetAuthor = targetAuthor;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    /**
     * 根据指定领域对象名称，获取生成的目标名称
     *
     * 根据调用者不同生成的结果也是不同的，示例：
     *      1、(ProductInfo, true, true) => com.xxx.product.domain.model.ProductInfo.java
     *      2、(ProductInfo, true, false) => com.xxx.product.domain.model.ProductInfo
     *      3、(ProductInfo, true, true) => com.xxx.product.dal.mapper.ProductInfoMapper.java
     *      4、(ProductInfo, true, false) => com.xxx.product.domain.service.ProductInfoService
     *      5、(Product, false, false) => ProductAppService
     *
     * @param domainObjectName  - 领域对象名称
     * @param includePackage    - 是否包含包名
     * @param includeSuffix     - 是否包含后缀名
     * @return 返回指定领域对象的目标名称
     */
    public abstract String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix);

}
