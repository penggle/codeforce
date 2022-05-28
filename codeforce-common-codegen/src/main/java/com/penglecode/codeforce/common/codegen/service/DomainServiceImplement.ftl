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
public class ${targetClass} implements <#list targetImplements as targetImplement>${targetImplement}<#if (item_has_next)>, </#if></#list> {

    @Resource(name="${mapperBeanName}")
    private ${mapperInterfaceName} ${mapperInstanceName};
<#if createDomainObject??>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${domainObjectParameter.domainObjectAlias}(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName}) {
    <#list createDomainObject.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        ${mapperInstanceName}.insert(${domainObjectParameter.lowerDomainObjectName});
    }
</#if>
<#if createDomainObjects??>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${domainObjectParameter.domainObjectsAlias}(List<${domainObjectParameter.domainObjectName}> ${domainObjectParameter.lowerDomainObjectsName}) {
    <#list createDomainObjects.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        EntityMapperHelper.batchUpdateEntityObjects(${domainObjectParameter.lowerDomainObjectsName}, this::create${domainObjectParameter.domainObjectAlias}, ${mapperInstanceName});
    }
</#if>
<#if modifyDomainObjectById??>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName}) {
    <#list modifyDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        ${mapperInstanceName}.updateById(${domainObjectParameter.lowerDomainObjectName}.identity(), updateColumns);
    }
</#if>
<#if modifyDomainObjectsById??>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${domainObjectParameter.domainObjectsAlias}ById(List<${domainObjectParameter.domainObjectName}> ${domainObjectParameter.lowerDomainObjectsName}) {
    <#list modifyDomainObjectsById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        EntityMapperHelper.batchUpdateEntityObjects(${domainObjectParameter.lowerDomainObjectsName}, this::modify${domainObjectParameter.domainObjectAlias}ById, ${mapperInstanceName});
    }
</#if>
<#if removeDomainObjectById??>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public int remove${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName}) {
    <#list removeDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        return ${mapperInstanceName}.deleteById(${domainObjectParameter.domainObjectIdName});
    }
</#if>
<#if removeDomainObjectsByIds??>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public int remove${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName}) {
    <#list removeDomainObjectsByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        return EntityMapperHelper.batchDeleteEntityObjects(${domainObjectParameter.domainObjectIdsName}, ${mapperInstanceName});
    }
</#if>
<#list removeDomainObjectsByXxxMasterId?values as removeDomainObjectsByMasterId>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public int remove${domainObjectParameter.domainObjectsAlias}By${removeDomainObjectsByMasterId.upperMasterIdNameOfSlave}(${removeDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectIdType} ${removeDomainObjectsByMasterId.masterIdNameOfSlave}) {
    <#list removeDomainObjectsByMasterId.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        ${mapperInstanceName}.deleteByCriteria(criteria);
    }
</#list>
<#if getDomainObjectById??>

    @Override
    public ${domainObjectParameter.domainObjectName} get${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName}) {
        return ObjectUtils.isEmpty(${domainObjectParameter.domainObjectIdName}) ? null : ${mapperInstanceName}.selectModelById(${domainObjectParameter.domainObjectIdName});
    }
</#if>
<#if getDomainObjectsByIds??>

    @Override
    List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName}) {
        return CollectionUtils.isEmpty(${domainObjectParameter.domainObjectIdsName}) ? Collections.emptyList() : ${mapperInstanceName}.selectModelListByIds(${domainObjectParameter.domainObjectIdsName});
    }
</#if>
<#list getDomainObjectsByXxxMasterId?values as getDomainObjectsByMasterId>

    @Override
    public ${getDomainObjectsByMasterId.methodReturnType} get${domainObjectParameter.domainObjectsAlias}By${getDomainObjectsByMasterId.upperMasterIdNameOfSlave}(${getDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectIdType} ${getDomainObjectsByMasterId.masterIdNameOfSlave}) {
    <#list getDomainObjectsByMasterId.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#list>
<#list getDomainObjectsByXxxMasterIds?values as getDomainObjectsByMasterIds>

    @Override
    public ${getDomainObjectsByMasterIds.methodReturnType} get${domainObjectParameter.domainObjectsAlias}By${getDomainObjectsByMasterIds.upperMasterIdsNameOfSlave}(List<${getDomainObjectsByMasterIds.masterDomainObjectParameter.domainObjectIdType}> ${getDomainObjectsByMasterIds.masterIdsNameOfSlave}) {
    <#list getDomainObjectsByMasterIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#list>
<#if getDomainObjectsByPage??>

    @Override
    public List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByPage(${domainObjectParameter.domainObjectName} condition, Page page) {
    <#list getDomainObjectsByPage.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        return EntityMapperHelper.selectEntityObjectListByPage(${mapperInstanceName}, criteria, page);
    }
</#if>
<#if getDomainObjectTotalCount??>

    @Override
    public int get${domainObjectParameter.domainObjectAlias}TotalCount() {
        return ${mapperInstanceName}.selectAllModelCount();
    }
</#if>

<#if forEachDomainObject1??>

    @Override
    public void forEach${domainObjectParameter.domainObjectAlias}(Consumer<${domainObjectParameter.domainObjectName}> consumer) {
        ${mapperInstanceName}.selectAllModelList().forEach(consumer);
    }
</#if>
<#if forEachDomainObject2??>

    @Override
    public void forEach${domainObjectParameter.domainObjectAlias}(ObjIntConsumer<${domainObjectParameter.domainObjectName}> consumer) {
        Cursor<${domainObjectParameter.domainObjectName}> cursor = ${mapperInstanceName}.selectAllModelList();
        int index = 0;
        for (${domainObjectParameter.domainObjectName} item : cursor) {
            consumer.accept(item, index++);
        }
    }
</#if>

}