package ${targetPackage};

<#if (targetProjectImports?size > 0 || targetThirdImports?size > 0)>
<#list targetProjectImports as targetImport>
import ${targetImport};
</#list>
<#list targetThirdImports as targetImport>
import ${targetImport};
</#list>

</#if>
<#if (targetJdkImports?size > 0)>
<#list targetJdkImports as targetImport>
import ${targetImport};
</#list>

</#if>
/**
 * ${targetComment}
 *
 * @author ${targetAuthor}
 * @version ${targetVersion}
 * @created ${targetCreated}
 */
<#list targetAnnotations as targetAnnotation>
${targetAnnotation}
</#list>
public class ${targetClass}<#if targetExtends??> extends ${targetExtends}</#if><#if (targetImplements?size > 0)> implements <#list targetImplements as targetImplement>${targetImplement}<#if (item_has_next)>, </#if></#list></#if> {

    private static final long serialVersionUID = 1L;

<#list inherentFields as field>
    /** ${field.fieldComment} */
    <#list field.fieldAnnotations as fieldAnnotation>
    ${fieldAnnotation}
    </#list>
    private ${field.fieldType} ${field.fieldName};

</#list>
<#list inherentFields as field>
    public ${field.fieldType} ${field.fieldGetterName}() {
        return ${field.fieldName};
    }

    public void ${field.fieldSetterName}(${field.fieldType} ${field.fieldName}) {
        this.${field.fieldName} = ${field.fieldName};
    }

</#list>
}
