<%@ page language="java" import="java.util.*,net.jkcode.jkmvc.http.HttpRequest,net.jkcode.jkmvc.tenancy.TenantModel" pageEncoding="UTF-8"%>
<% HttpRequest req = HttpRequest.current(); %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>todolist</title>
  <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
  <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
  <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
  <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
</head>
<body>
  <div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">租户列表</div>
    <div class="panel-body">
      <p>
        <a href="<%= req.absoluteUrl("tenant/new/") %>" class="btn btn-warning">新建</a>
      </p>

       <!-- Table -->
      <table class="table">
        <thead>
          <tr>
            <th>租户id</th>
            <th>租户名</th>
            <th>租户子域名</th>
            <th>操作:</th>
          </tr>
        </thead>
        <tbody>
          <%  List<TenantModel> tenants = (List<TenantModel>)request.getAttribute("tenants");
              for (Iterator<TenantModel> it = tenants.iterator(); it.hasNext();) {
               TenantModel tenant = it.next(); %>
              <tr>
                <th scope="row"><%= tenant.getId() %></th>
                <td><%= tenant.getName() %></td>
                <td><a href="<%= tenant.buildTenantUrl("user/") %>"><%= tenant.buildTenantUrl("user/") %></a></td>
                <td>
                  <a href="<%= req.absoluteUrl("tenant/detail/" + tenant.getId()) %>" class="btn btn-default">详情</a>
                  <a href="<%= req.absoluteUrl("tenant/edit/" + tenant.getId()) %>" class="btn btn-primary">编辑</a>
                  <a href="<%= req.absoluteUrl("tenant/delete/" + tenant.getId())%>" class="btn btn-info">删除</a>
                 </td>
              </tr>
           <% } %>
        </tbody>
      </table>
    </div>
  </div>
  <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
  <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</body>
</html>