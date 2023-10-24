    <%@ include file="common/header.jspf" %>
    <%@ include file="common/navigation.jspf" %>

    <body>
        <div class="container">
            <h1>Welcome to my website!</h1>
            <div>Your name is: ${name}</div>
            <div><a href="list-todos">Manage</a> your todos.</div>
        </div>
    </body>

    <%@ include file="common/footer.jspf" %>

</html>