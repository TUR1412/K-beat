<!DOCTYPE html>
<html lang="en">
<head>
    <title>待办事项</title>
</head>
<body>
<h1>待办事项列表</h1>
<form action="/todos" method="post">
    <input type="text" name="description" placeholder="输入待办事项" required>
    <select name="priority">
        <option value="high">高</option>
        <option value="medium">中</option>
        <option value="low">低</option>
    </select>
    <input type="date" name="due_date" required>
    <button type="submit">添加</button>
</form>
<ul>
    <#list todos as todo>
        <li>
            <span>${todo.description}</span> - 优先级: <span>${todo.priority}</span> - 截止日期: <span>${todo.dueDate}</span>
            <#if !todo.isCompleted()> <!-- 修改为调用方法 -->
                <form action="/todos/complete/${todo.id}" method="post" style="display:inline;">
                    <button type="submit">标记完成</button>
                </form>
            </#if>
            <form action="/todos/delete/${todo.id}" method="post" style="display:inline;">
                <button type="submit">删除</button>
            </form>
        </li>
    </#list>
</ul>
</body>
</html>
