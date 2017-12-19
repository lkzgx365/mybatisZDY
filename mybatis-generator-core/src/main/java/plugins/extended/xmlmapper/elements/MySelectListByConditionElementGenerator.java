package plugins.extended.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

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
                "java.util.Map"));
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
            sb.append(" != null "); //$NON-NLS-1$
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

        for(IntrospectedColumn introspectedColumn : ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable
                .getAllColumns())) {
            sb.setLength(0);
            if("VARCHAR".equals(introspectedColumn.getJdbcTypeName())) {
                XmlElement valuesNotNullElement = new XmlElement("if"); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty() + "Param");
                sb.append(" != null "); //$NON-NLS-1$
                valuesNotNullElement.addAttribute(new Attribute(
                        "test", sb.toString())); //$NON-NLS-1$

                sb.setLength(0);
                sb.append(" and ");
                sb.append(MyBatis3FormattingUtilities
                        .getAliasedEscapedColumnName(introspectedColumn));
                sb.append(" LIKE CONCAT('%',#{" + introspectedColumn.getJavaProperty() + "Param,jdbcType=VARCHAR},'%') ");
                valuesNotNullElement.addElement(new TextElement(sb.toString()));
                answer.addElement(valuesNotNullElement);
            }else if("TIMESTAMP".equals(introspectedColumn.getJdbcTypeName())){
                XmlElement valuesNotNullElementStart = new XmlElement("if"); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty() + "Start");
                sb.append(" != null "); //$NON-NLS-1$
                valuesNotNullElementStart.addAttribute(new Attribute(
                        "test", sb.toString())); //$NON-NLS-1$

                sb.setLength(0);
                sb.append(" and ");
                sb.append(MyBatis3FormattingUtilities
                        .getAliasedEscapedColumnName(introspectedColumn));
                sb.append(" &gt; #{"+introspectedColumn.getJavaProperty() + "Start,jdbcType=TIMESTAMP}");
                valuesNotNullElementStart.addElement(new TextElement(sb.toString()));
                answer.addElement(valuesNotNullElementStart);

                sb.setLength(0);
                XmlElement valuesNotNullElementEnd = new XmlElement("if"); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty() + "End");
                sb.append(" != null "); //$NON-NLS-1$
                valuesNotNullElementEnd.addAttribute(new Attribute(
                        "test", sb.toString())); //$NON-NLS-1$

                sb.setLength(0);
                sb.append(" and ");
                sb.append(MyBatis3FormattingUtilities
                        .getAliasedEscapedColumnName(introspectedColumn));
                sb.append(" &lt; #{"+introspectedColumn.getJavaProperty() + "End,jdbcType=TIMESTAMP}");
                valuesNotNullElementEnd.addElement(new TextElement(sb.toString()));
                answer.addElement(valuesNotNullElementEnd);
            }
        }

        sb.setLength(0);
        XmlElement orderParams = new XmlElement("if");
        sb.append(" orderByParams != null");
        orderParams.addAttribute(new Attribute("test",sb.toString()));
        orderParams.addElement(new TextElement(" order by "));
        XmlElement innerForEach = new XmlElement("foreach");
        innerForEach.addAttribute(new Attribute("collection", "orderByParams"));
        innerForEach.addAttribute(new Attribute("item", "item"));
        innerForEach.addAttribute(new Attribute("index", "index"));
        innerForEach.addAttribute(new Attribute("separator", ","));
        innerForEach.addElement(new TextElement(" ${item}"));
        XmlElement descParams = new XmlElement("if");
        orderParams.addElement(innerForEach);
        sb.setLength(0);
        sb.append(" descParams != null ");
        descParams.addAttribute(new Attribute("test",sb.toString()));
        descParams.addElement(new TextElement(" DESC "));
        orderParams.addElement(descParams);
        answer.addElement(orderParams);

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
