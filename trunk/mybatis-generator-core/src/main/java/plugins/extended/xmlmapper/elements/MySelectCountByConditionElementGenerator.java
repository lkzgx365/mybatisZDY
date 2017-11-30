package plugins.extended.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class MySelectCountByConditionElementGenerator extends AbstractXmlElementGenerator {

    public MySelectCountByConditionElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute(
                "id", introspectedTable.getSelectCountByConditionStatementId()));
        answer.addAttribute(new Attribute("parameterType",
                "java.util.Map"));
        answer.addAttribute(new Attribute("resultType",
                "java.lang.Integer"));
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select count(1) ");

        answer.addElement(new TextElement(sb.toString()));
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

        if (context.getPlugins().sqlMapSelectAllElementGenerated(
                answer, introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
