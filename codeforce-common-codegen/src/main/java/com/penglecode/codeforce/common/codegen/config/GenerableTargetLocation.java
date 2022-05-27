package com.penglecode.codeforce.common.codegen.config;

/**
 * 能自动生成的目标位置
 *
 * @author pengpeng
 * @version 1.0
 */
public class GenerableTargetLocation {

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

}
