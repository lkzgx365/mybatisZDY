package plugins.extended.javamapper;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;
import plugins.extended.javamapper.elements.MySelectByCodeMethodGenerator;
import plugins.extended.javamapper.elements.MySelectCountByConditionMethodGenerator;
import plugins.extended.javamapper.elements.MySelectListByConditionMethodGenerator;
import plugins.extended.javamapper.elements.MyUpdateByBusinessCodeMethodGenerator;

import java.util.ArrayList;
import java.util.List;



public class MyJavaMapperGenerator extends JavaMapperGenerator {

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(Messages.getString("Progress.17",
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);

        String rootInterface = introspectedTable
                .getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!StringUtility.stringHasValue(rootInterface)) {
            rootInterface = context.getJavaClientGeneratorConfiguration()
                    .getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (StringUtility.stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
                    rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }

        addDeleteByPrimaryKeyMethod(interfaze);
        addInsertSelectiveMethod(interfaze);
        addSelectByPrimaryKeyMethod(interfaze);
        addUpdateByPrimaryKeySelectiveMethod(interfaze);
        addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
        //增加selectAll
        addSelectListByConditionMethod(interfaze);
        addSelectCountByConditionMethod(interfaze);
        addSelectByBusinessCodeMethod(interfaze);
        addUpdateByBusinessCodeMethod(interfaze);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().clientGenerated(interfaze, null,
                introspectedTable)) {
            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }

    protected void addSelectListByConditionMethod(Interface interfaze) {
        if (introspectedTable.getRules()
                .generateSelectListByCondition()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new MySelectListByConditionMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addSelectCountByConditionMethod(Interface interfaze) {
        if (introspectedTable.getRules()
                .generateSelectCountByCondition()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new MySelectCountByConditionMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addSelectByBusinessCodeMethod(Interface interfaze){
        if(introspectedTable.getRules().generateSelectByCode()){
            AbstractJavaMapperMethodGenerator methodGenerator = new MySelectByCodeMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator,interfaze);
        }
    }

    protected void addUpdateByBusinessCodeMethod(Interface interfaze){
        if(introspectedTable.getRules().generateUpdateByBusinessCode()){
            AbstractJavaMapperMethodGenerator methodGenerator = new MyUpdateByBusinessCodeMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator,interfaze);
        }
    }
}
