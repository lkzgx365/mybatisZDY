package plugins.extended;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import plugins.extended.javamapper.MyJavaMapperGenerator;
import plugins.extended.xmlmapper.MyXMLMapperGenerator;

import java.util.List;

public class MyIntrospectedTable extends MyIntrospectedTableMybatis3Impl {

    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback) {
        xmlMapperGenerator = new MyXMLMapperGenerator();

        initializeAbstractGenerator(xmlMapperGenerator, warnings,
                progressCallback);
    }

    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }
        AbstractJavaClientGenerator javaGenerator = new MyJavaMapperGenerator();

        return javaGenerator;
    }
}
