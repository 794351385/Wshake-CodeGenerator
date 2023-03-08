package metadata;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/3
 * ORM框架常用,如Mybatis映射
 * 结果集元数据(ResultSetMetaData)
 *  通过ResultSet获取
 *  获取sql参数中的属性信息
 */
public class ResultSetMetaDataTest {
    String driver="com.mysql.cj.jdbc.Driver";
    String url="jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&useSSL=false";
    String username="root";
    String password="123456";
    Properties props=new Properties();
    Connection connection=null;
    DatabaseMetaData metaData=null;
    ResultSet rs=null;

    //获取数据库基本信息
    @Before
    public void before() throws ClassNotFoundException, SQLException {
        props.put("remarksReporting","true"); //获取数据库的备注信息
        props.put("user",username);
        props.put("password",password);

        //1.获取连接
        //注册驱动
        Class.forName(driver);
        //有可能获取不到获取数据库的备注
        //Connection connection = DriverManager.getConnection(url, username, password);
        //可以获取数据库的备注
        connection = DriverManager.getConnection(url,props);

        //2.获取元数据
        metaData = connection.getMetaData();   //根据metaData.get可以获取所需数据

        //获取数据库列表
        rs = metaData.getCatalogs();

    }
    @After
    public void after() throws SQLException {
        rs.close();
        connection.close();
    }

    @Test
    public void test01() throws SQLException {
        String sql="select * from order.goods where id=?";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1,"1");

        //发起查询
        ResultSet rs = pstmt.executeQuery();

        //获取结果集
        ResultSetMetaData rsMetaData = rs.getMetaData();

        //获取查询字段个数
        int columnCount = rsMetaData.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            //获取列名
            String columnName = rsMetaData.getColumnName(i + 1);
            //获取字段类型    sql类型
            int columnType = rsMetaData.getColumnType(i + 1);
            String columnTypeName = rsMetaData.getColumnTypeName(i + 1);
            //获取列的类型名   Java类型
            String columnClassName = rsMetaData.getColumnClassName(i + 1);
            System.out.println(columnName+"--"+columnType+"--"+columnTypeName+"--" +columnClassName);
        }
    }
}
