package metadata;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/3
 * 测试数据库元数据
 */
public class DataBaseMetaDataTest {
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



    }
    @After
    public void after() throws SQLException {
        if(rs!=null){
            rs.close();
        }
        connection.close();
    }

    @Test
    public void test01() throws SQLException {
        //获取数据库列表
        rs = metaData.getCatalogs();
        //3.获取数据库基本信息
        System.out.println(metaData.getUserName()); //获取用户名

        System.out.println(metaData.supportsTransactions());    //获取是否支持事务

        System.out.println(metaData.getDatabaseProductName());  //获取数据库名称

    }

    //获取数据库 列表
    @Test
    public void test02() throws SQLException {
        //获取数据库列表
        rs = metaData.getCatalogs();
        while (rs.next()){
            //获取数据库所有表名
            System.out.println(rs.getString(1));
        }
    }

    //获取指定数据库中的表的信息
    @Test
    public void test03() throws SQLException {
        //String catalog,           获取数据库的名称
        //String schemaPattern,     指定的用户名称    Oracle用户名称(大写)  mysql为空
        //String tableNamePattern,  指定的表名称      null:查询所有表 非空:查询目标表
        //String types[]            指定的表类型      types:类型(如视图View,表Table)

        rs = metaData.getTables("order",null,null,new String[]{"TABLE"});
        while (rs.next()){
            //获取数据库所有表名
            System.out.println(rs.getString("TABLE_NAME"));
        }
    }

    //获取指定数据库中的表的字段信息
    @Test
    public void test04() throws SQLException {
        //String catalog,           获取数据库的名称
        //String schemaPattern,     指定的用户名称     Oracle用户名称(大写)  mysql为空
        //String tableNamePattern,  指定的表名称       null:查询所有表 非空:查询目标表
        //String columnNamePattern  指定的字段名称     null:查询所有字段 非空指定字段

        rs = metaData.getColumns("order",null,"goods",null);
        ResultSet keyRs = metaData.getPrimaryKeys("order", null, "goods");
        int i=1;
        while (rs.next()){
            //获取数据库order表中所有字段名
            System.out.println(rs.getString("COLUMN_NAME"));
            //获取字段Type的Code值
            System.out.println(rs.getString("DATA_TYPE"));
            //获取SQL里面的数据类型(在Java里面不能用)
            System.out.println(rs.getString("TYPE_NAME"));
            System.out.println(rs.getString("IS_AUTOINCREMENT"));
        }
        while (keyRs.next()){
            //获取主键
            System.out.println("----------"+ keyRs.getString("COLUMN_NAME"));
        }
    }

    @Test
    public void demo(){
        Set<String> set = new HashSet<>();
        set.add("order");
        set.add("goods");
        System.out.println(set.toString());
    }

}
