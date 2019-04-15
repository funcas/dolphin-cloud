package cn.goktech.dolphin.oss.spring.conf;

import cn.goktech.dolphin.oss.enumeration.Provider;
import cn.goktech.dolphin.oss.spring.OssProviderSpringFacade;
import cn.goktech.dolphin.oss.spring.annotation.EnableOssClient;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * @author funcas
 * @version 1.0
 * @date 2019年04月12日
 */
public class OssClientImportSelector implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment env;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
                .getAnnotationAttributes(EnableOssClient.class.getName(),
                        true));
        Assert.notNull(attributes, "No auto-configuration attributes found. Is "
                + metadata.getClassName()
                + " annotated with @EnableOssClient?");

        Provider provider = attributes.getEnum("provider");
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(OssProviderSpringFacade.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("provider", provider.getValue());
        propertyValues.addPropertyValue("endpoint", env.getProperty("oss.endpoint", ""));
        propertyValues.addPropertyValue("accessKey", env.getProperty("oss.accessKey", ""));
        propertyValues.addPropertyValue("secretKey", env.getProperty("oss.secretKey",""));
        propertyValues.addPropertyValue("urlPrefix", env.getProperty("oss.url-prefix",""));
        propertyValues.addPropertyValue("ifPrivate", env.getProperty("oss.if-private", "false"));
        propertyValues.addPropertyValue("bucketName", env.getProperty("oss.bucketName", ""));
        propertyValues.addPropertyValue("internalUrl", env.getProperty("oss.internal-url", ""));
        beanDefinition.setPropertyValues(propertyValues);
        beanDefinitionRegistry.registerBeanDefinition("ossProviderSpingFacade", beanDefinition);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }


}
