package one;

import sun.plugin2.message.Message;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */
@WebServlet(urlPatterns = "/one/student")
public class StudentAction extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String Action = req.getParameter("action");
        System.out.println("进入到student界面。。。");
        if ("add".equals(Action)) {
            add(req, resp);
            return;
        }
        if ("queryAll".equals(Action)) {
            queryAll(req, resp);
            return;
        }
        if ("queryById".equals(Action)) {
            queryById(req, resp);
            return;
        }
        if ("modify".equals(Action)) {
            modify(req, resp);
            return;
        }
        if ("remove".equals(Action)) {
            remove(req, resp);
            return;
        }
        if ("batchRemove".equals(Action)) {
            batchRemove(req, resp);
            return;
        }
        req.setAttribute("message", "出了一点问题");
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
        }
    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String date = req.getParameter("date");

        if (name.length() == 0 || gender.length() == 0 || date.length() == 0) {
            req.setAttribute("message", "添加失败：您输入的信息不规范");
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        } else {
            Connection connection = Db.getConnection();
            PreparedStatement statement = null;

            try {
                String sql = "INSERT INTO db_javaee.studens (name, gender, date) VALUES (?,?,?)";
                if (connection != null) {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, gender);
                    statement.setString(3, date);
                    statement.executeUpdate();

                    resp.sendRedirect("student?action=queryAll");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Db.close(null, statement, connection);
            }
        }
    }
    private void queryAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            Connection connection = Db.getConnection();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                String sql ="SELECT * FROM db_javaee.studens";
                if (connection != null) {
                    statement = connection.prepareStatement(sql);
                    resultSet = statement.executeQuery();
                    List<Students> students = new ArrayList<>();
                        while (resultSet.next()) {
                            Students student = new Students(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("gender"),
                                    resultSet.getString("date")
                            );
                            students.add(student);
                        }
                    req.getSession().setAttribute("students", students);
                    resp.sendRedirect("home.jsp");
                } else {
                    req.setAttribute("message", "异常：没有获取到学生信息");
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                Db.close(resultSet,statement,connection);
            }

    }
    private void queryById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

            Connection connection = Db.getConnection();
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                String sql ="SELECT * FROM db_javaee.studens WHERE id=?";
                if (connection != null) {
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1,id);
                    resultSet = statement.executeQuery();
                    resultSet.next();
                    Students student = new Students(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("gender"),
                            resultSet.getString("date")
                    );

                    req.getSession().setAttribute("student", student);
                    resp.sendRedirect("edit.jsp");
                } else {
                    req.setAttribute("message", "异常：没有获取到该学生信息");
                    req.getRequestDispatcher("home.jsp").forward(req, resp);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                Db.close(resultSet,statement,connection);
            }

    }
    private void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String date = req.getParameter("date");

        if (name.length() == 0 || gender.length() == 0 || date.length() == 0) {
            req.setAttribute("message", "编辑失败：您输入的信息不规范");
            req.getRequestDispatcher("edit.jsp").forward(req, resp);
        } else {
            Connection connection = Db.getConnection();
            PreparedStatement statement = null;

            try {
                String sql = "UPDATE db_javaee.studens SET name=?,gender=?,date=? WHERE id=?";
                if (connection != null) {
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, gender);
                    statement.setString(3, date);
                    statement.setInt(4,id);
                    statement.executeUpdate();

                    resp.sendRedirect("student?action=queryAll");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Db.close(null, statement, connection);
            }
        }
    }
    private void removeByid(int id,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = Db.getConnection();
        PreparedStatement statement = null;

        try {
            String sql ="DELETE FROM db_javaee.studens WHERE id=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1,id);
                statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Db.close(null,statement,connection);
        }
    }
    private void remove(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        Connection connection = Db.getConnection();
        PreparedStatement statement = null;
        removeByid(id, req, resp);
        resp.sendRedirect("student?action=queryAll");

    }
    private void batchRemove(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] ids = req.getParameterValues("ids");
        if (ids == null) {
            req.setAttribute("message","删除失败：没有选择任何需要删除的记录");
            req.getRequestDispatcher("home.jsp").forward(req, resp);
            return;
        }
        for (String idString : ids) {
            int id = Integer.parseInt(idString);
            removeByid(id,req,resp);
        }
        resp.sendRedirect("student?action=queryAll");
    }

}