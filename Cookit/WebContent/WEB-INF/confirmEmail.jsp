<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Confirmar email</title>
    
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/main.css">
    
    <jsp:include page="prebuilt/boostrap.jsp" />
    
</head>
<body>

	<%@ page import="data.BeanUsuario"%>
	
	<% if(session.getAttribute("myself") != null){ %>
	
	<% 
		BeanUsuario myself = (BeanUsuario) session.getAttribute("myself");
	%>
	
	<jsp:include page="prebuilt/tempMsg.jsp" />

	<div class="side-img"></div>

	<div class="login">
	
		<div class="login-content">

            <h2 class="form-title recovery-title">Confirmar correo electrónico</h2>
            <h4 class="form-subtitle">Se ha enviado un código de confirmación a <%= myself.getEmail() %></h4>

            <div class="log-form">

                <form action="confirmEmail" method="post" accept-charset="utf-8">
                    <div class="pass-field">
                        <label for="recovery-code">Teclee o pegue aquí su código</label>
                        <input type="text" id="code" name="recovery-code" required>
                    </div>
                    
                    <input type="submit" class="button-login" value="CONFIRMAR MI CORREO">
                </form>
                
                <a href="confirmEmail"><button class="button-login">Volver a enviar</button></a>
                <a href="index" style="text-decoration: underline !important;">No quiero confirmar mi correo ahora</a>
                

            </div>

        </div>
	
	</div>
	
	<% } else { %>
	
		 <jsp:forward page = "login" />
	
	<% } %>

</body>
</html>