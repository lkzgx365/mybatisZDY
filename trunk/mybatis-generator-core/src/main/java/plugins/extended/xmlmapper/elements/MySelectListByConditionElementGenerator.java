package plugins.extended.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Iterator;

public class MySelectListByConditionElementGenerator extends AbstractXmlElementGenerator {

    public MySelectListByConditionElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute(
                "id", introspectedTable.getSelectListByConditionStatementId()));
        answer.addAttribute(new Attribute("parameterType",
                "map"));
        answer.addAttribute(new Attribute("resultMap",
                introspectedTable.getBaseResultMapId()));
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select ");

        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());
        if (introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(",")); //$NON-NLS-1$
            answer.addElement(getBlobColumnListElement());
        }

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append(" where 1=1");
        answer.addElement(new TextElement(sb.toString()));


        for(IntrospectedColumn introspectedColumn : ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable
                .getAllColumns())) {
            sb.setLength(0);
            XmlElement valuesNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            valuesNotNullElement.addAttribute(new Attribute(
                    "test", sb.toString())); //$NON-NLS-1$

            sb.setLength(0);
            sb.append(" and ");
            sb.append(MyBatis3FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities
                     .getParameterClause(introspectedColumn));
            valuesNotNullElement.addElement(new TextElement(sb.toString()));
            answer.addElement(valuesNotNullElement);
        }

        sb.setLength(0);
        XmlElement orderParams = new XmlElement("if");
        sb.append(" orderByParams != null");
        orderParams.addAttribute(new Attribute("test",sb.toString()));
        answer.addElement(orderParams);
        XmlElement innerForEach = new XmlElement("foreach");
        innerForEach.addAttribute(new Attribute("collection", "orderByParams"));
        innerForEach.addAttribute(new Attribute("item", "item"));
        innerForEach.addAttribute(new Attribute("index", "index"));
        innerForEach.addAttribute(new Attribute("separator", ","));
        innerForEach.addElement(new TextElement(" ${item}"));
        answer.addElement(innerForEach);

        sb.setLength(0);
        XmlElement pageLimit = new XmlElement("if");
        sb.append(" pageNumber != null and pageSize != null");
        pageLimit.addAttribute(new Attribute("test",sb.toString()));
        pageLimit.addElement(new TextElement(" limit #{pageNumber,jdbcType=INTEGER} , #{pageSize,jdbcType=INTEGER}"));
        answer.addElement(pageLimit);

        if (context.getPlugins().sqlMapSelectAllElementGenerated(
                answer, introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
