package com.penglecode.codeforce.examples.mybatistiny.boot;

import com.penglecode.codeforce.examples.mybatistiny.BasePackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * codeforce-common-mybatis-tiny测试示例应用程序
 *
 * @author pengpeng
 * @version 1.0
 */
@AutoConfigurationPackage(basePackageClasses=BasePackage.class)
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class MybatisTinyExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisTinyExampleApplication.class, args);
    }

}
