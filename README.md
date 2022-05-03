# codeforce

codeforce是一个基于SpringBoot的通用快速开发脚手架工程，使用它意味着某些技术选型已经固化了。codeforce除了对一些框架的扩展改造外，还提供了一个基于DDD的自动代码生成框架，该自动代码生成框架基于DDD的领域驱动建模思想，能够按照模块化的（最小模块支持到聚合根级别）便于团队协作的方式自动生成基本CRUD后端代码（API接口、应用服务、领域服务、领域对象(实体对象、值对象、聚合根)、基础设施(DAL-基于Mybatis的数据访问层)）,其基于自定义的DDD领域驱动设计[配置Schema](https://github.com/penggle/xmodule3/blob/main/xmodule3-sample2/xmodule3-sample2-codegen/src/main/resources/config/application-codegen-product-new.yml)实现了领域聚合根内的实体对象级联保存、级联获取等特性，说白了就是这个自动生成代码的框架具有处理多表关联CRUD的能力，如果仅仅只能处理单表CRUD那它是没有存在意义的。
