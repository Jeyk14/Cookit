<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ page import="data.BeanUsuario"%>
    
    <%
    
    BeanUsuario myself = (BeanUsuario) session.getAttribute("myself");
    
    %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Recuperar contrase�a</title>
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/main.css">
    
    <jsp:include page="prebuilt/boostrap.jsp" />
    
</head>
<body>

	<div class="login">
	
		<div class="login-content">

            <h2 class="form-title recovery-title">Recuperar contrase�a</h2>
            <h4 class="form-subtitle">Env�a un codigo de verificaci�n a tu correo electr�nico para saber que eres tu y s�lo tu</h4>

            <div class="log-form">

                <form class="email-field" action="restorePass" method="post">
                    <label for="email-code">Email</label>
                    <input type="text" id="email" name="target-email" <%= myself.getEmail() %>required>
                    <input type="submit" class="send-email" name="submit" value="ENVIAR C�DIGO">

                        <label for="recovery-code">C�digo de recuperacion</label>
                        <input type="text" id="code" name="recovery-code" required>
                    
                    <input type="submit" class="button-login" name="submit" value="CONFIRMAR CORREO">
                </form>

            </div>

        </div>
	
	</div>

</body>
</html>