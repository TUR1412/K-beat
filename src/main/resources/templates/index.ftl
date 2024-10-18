<!DOCTYPE html>
<html lang="en">
<head>
    <title>待办事项</title>
</head>
<body>
<h1>待办事项列表</h1>
<form action="/todos" method="post">
    <input type="text" name="description" placeholder="输入待办事项" required>
    <button type="submit">添加</button>
</form>
<ul>
    <#list todos as todo>
    <li>
        <span>${todo.description}</span>
        <#if !todo.completed> <!-- 确保这里使用正确的属性名 -->
            <form action="/todos/complete/${todo.id}" method="post"
                  style="display:inline;">
                <button type="submit">标记完成</button>
            </form>
        </#if>
        <form action="/todos/delete/${todo.id}" method="post"
              style="display:inline;">
            <button type="submit">删除</button>
        </form>
    </li>
    </#list>
</ul>
</body>
</html>