package plugins.extended.xmlmapper;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import plugins.extended.xmlmapper.elements.MySelectByCodeElementGenerator;
import plugins.extended.xmlmapper.elements.MySelectCountByConditionElementGenerator;
import plugins.extended.xmlmapper.elements.MySelectListByConditionElementGenerator;
import plugins.extended.xmlmapper.elements.MyUpdateByBusinessElementGenerator;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class MyXMLMapperGenerator extends XMLMapperGenerator {

    public MyXMLMapperGenerator() {
    }

    @Override
    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString(
                "Progress.12", table.toString()));
        XmlElement answer = new XmlElement("mapper");
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace",
                namespace));

        context.getCommentGenerator().addRootComment(answer);

        addResultMapWithoutBLOBsElement(answer);
        addResultMapWithBLOBsElement(answer);
        addBaseColumnListElement(answer);
        addBlobColumnListElement(answer);
        addSelectByPrimaryKeyElement(answer);
        addDeleteByPrimaryKeyElement(answer);
        addInsertSelectiveElement(answer);
        addUpdateByPrimaryKeySelectiveElement(answer);
        addUpdateByPrimaryKeyWithBLOBsElement(answer);
        addMySelectListByConditionElementGeneratorElement(answer);
        addMySelectCountByConditionElementGeneratorElement(answer);
        addSelectByCodeElementGeneratorElement(answer);
        addMyUpdateByBusinessCodeElementGeneratorElement(answer);
        return answer;
    }

    protected void addSelectByCodeElementGeneratorElement(XmlElement answer) {
        if(introspectedTable.getRules().generateSelectByCode()){
            AbstractXmlElementGenerator elementGenerator = new MySelectByCodeElementGenerator();
            initializeAndExecuteGenerator(elementGenerator,answer);
        }
    }

    protected void addMySelectListByConditionElementGeneratorElement(XmlElement parentElement){
        if (introspectedTable.getRules()
                .generateSelectListByCondition()) {
            AbstractXmlElementGenerator elementGenerator = new MySelectListByConditionElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addMySelectCountByConditionElementGeneratorElement(XmlElement parentElement){
        if (introspectedTable.getRules()
                .generateSelectCountByCondition()) {
            AbstractXmlElementGenerator elementGenerator = new MySelectCountByConditionElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addMyUpdateByBusinessCodeElementGeneratorElement(XmlElement xmlElement){
        if(introspectedTable.getRules().generateUpdateByBusinessCode()){
            AbstractXmlElementGenerator elementGenerator = new MyUpdateByBusinessElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, xmlElement);
        }
    }


}
