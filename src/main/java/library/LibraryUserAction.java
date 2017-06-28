package library;

import util.Db;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/6/28.
 */
@WebServlet(urlPatterns = "/library/libraryUser")
public class LibraryUserAction extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        System.out.println("进入到LibraryUser界面");
        String action = req.getParameter("action");
        if ("register".equals(action)) {
            System.out.println("进入到register方案");
            register(req, resp);
            return;
        }
        req.setAttribute("message", "错误：没有与之匹配的方法");
        resp.sendRedirect("index.jsp");
    }

    protected void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("开始执行register方法");
        String userName = req.getParameter("userName").trim();
        String password = req.getParameter("password");

        Connection connection = Db.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM library.user WHERE userName=?";
        try {
            if (connection == null) {
                req.setAttribute("message", "Connection为null");
                req.getRequestDispatcher("index.jsp").forward(req,resp);
            } else {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,userName);
                resultSet=preparedStatement.executeQuery();
                if (resultSet.next()) {
                    req.setAttribute("message","用户名已存在");
                    req.getRequestDispatcher("register.jsp").forward(req,resp);
                    return;
                }

                sql = "INSERT INTO library.user(userName, password) VALUES (?,?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,userName);
                preparedStatement.setString(2,password);
                preparedStatement.executeUpdate();
                req.setAttribute("message","您已注册成功");
                req.getRequestDispatcher("index.jsp").forward(req,resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Db.close(resultSet,preparedStatement,connection);
        }
    }
}