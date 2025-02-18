<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .nav-header {
        background: #f8f9fa;
        padding: 10px 20px;
        margin-bottom: 20px;
        border-bottom: 1px solid #ddd;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .user-info {
        display: flex;
        align-items: center;
        gap: 15px;
    }
    .user-name {
        color: #333;
        font-weight: bold;
    }
    .user-roles {
        color: #666;
        font-size: 0.9em;
    }
    .role-badge {
        background: #e9ecef;
        padding: 2px 8px;
        border-radius: 3px;
        margin: 0 2px;
    }
    .logout-btn {
        background: #dc3545;
        color: white;
        padding: 5px 15px;
        border-radius: 4px;
        text-decoration: none;
    }
    .logout-btn:hover {
        background: #c82333;
    }
</style>

<div class="nav-header">
    <div class="user-info">
        <c:if test="${not empty sessionScope.user}">
            <span class="user-name">${sessionScope.user}</span>
            <span class="user-roles">
                Rôles: 
                <c:forEach items="${sessionScope.roles}" var="role" varStatus="status">
                    <span class="role-badge">${role}</span>
                </c:forEach>
            </span>
        </c:if>
    </div>
    <c:if test="${not empty sessionScope.user}">
        <a href="/TestFramework/logout" class="logout-btn">Déconnexion</a>
    </c:if>
</div> 