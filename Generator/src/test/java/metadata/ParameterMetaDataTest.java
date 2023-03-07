package metadata;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Wshake
 * @version 1.0.0
 * @date 2023/3/3
 * 测试参数元数据(ParameterMetaData)
 *  通过PreparedStatement获取
 *  获取sql参数中的属性信息
 */
public class ParameterMetaDataTest {
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
        String sql="select * from goods where id=?";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1,"1");

        //获取参数元数据
        ParameterMetaData parameterMetaData = pstmt.getParameterMetaData();
        //获取个数 还有其他方法可以尝试
        int count = parameterMetaData.getParameterCount();
        System.out.println(count);
    }
}
