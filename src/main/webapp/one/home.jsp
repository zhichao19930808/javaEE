<%@ page import="java.util.List" %>
<%@ page import="one.Students" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
    <script>
        function del() {
            return confirm('是否删除这条记录？')
        }
        function selectALL() {
            var cb = document.getElementById('cb');
            var items = document.getElementsByClassName('st');
            for (var i = 0; i < items.length; i++) {
                items[i].checked = cb.checked
            }
        }
    </script>
</head>
<body>
<c:if test="${sessionScope.nick eq null}">
    <c:redirect url="index.jsp"/>
</c:if>
<h1>${sessionScope.nick}的主页</h1>
<hr/>
<form action="student" method="post">
    <h3>添加学生</h3>
    <input type="hidden" name="action" value="add">
    <input type="text" name="name" placeholder="学生姓名"><br>
    <p>性别</p>
    <input type="radio" name="gender" value="男">男
    <input type="radio" name="gender" value="女">女 <br>
    <input type="date" name="date" ><br>
    <input type="submit" value="添加">
</form>
<hr/>
<p>${requestScope.message}</p>
<hr/>
<form action="/one/student">
    <input type="hidden" name="action" value="batchRemove">
<table border="1">
    <c:choose>
        <c:when test="${fn:length(sessionScope.students) eq 0}">
            当前没有记录
        </c:when>
        <c:otherwise>
            <tr>
                <th><input type="checkbox" id="cb" onclick="return selectALL()">序号</th>
                <th>姓名</th>
                <th>性别</th>
                <th>出生日期</th>
                <th colspan="2">操作</th>
            </tr>
        </c:otherwise>
    </c:choose>
    <c:forEach var="student" items="${sessionScope.students}" varStatus="vs" begin="0" end="30" step="1">
        <tr>
            <td><input class="st" type="checkbox" name="ids" value="${student.id}">${vs.count}</td>
            <td>${student.name}</td>
            <td>${student.gender}</td>
            <td>${student.date}</td>
            <td><a href="student?action=queryById&id=${student.id}">编辑</a></td>
            <td><a href="student?action=remove&id=${student.id}" onclick="return del()">删除</a></td>
        </tr>
    </c:forEach>

</table>
    <c:if test="${fn:length(sessionScope.students) ne 0}">
    <input type="submit" value="删除" onclick="return del()">
    </c:if>
</form>
<a href="second.jsp">下一页</a>
<a href="user?action=logout">注销</a>
</body>
</html>