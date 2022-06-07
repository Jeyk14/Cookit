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
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>
    
</head>
<body>

	<%@ page import="data.BeanUsuario"%>
	
	<% if(session.getAttribute("myself") != null){ %>
	
	<% 
		BeanUsuario myself = (BeanUsuario) session.getAttribute("myself");
	%>
	
	<% if(request.getAttribute("tempMsg") != null){ %>
	
		<div class="errorMsg">
			<h4>Parece que algo ha ido mal...</h4>
			<p><%= request.getAttribute("tempMsg") %></p>
		</div>
	
	<% } %>

	<div class="side-img"></div>

	<div class="login">
	
		<div class="login-content">

            <h2 class="form-title recovery-title">Recuperar contraseña</h2>
            <h4 class="form-subtitle">Se ha enviado un código de recuperación a <%= myself.getEmail() %></h4>

            <div class="log-form">

                <form action="confirmEmail" method="post" accept-charset="utf-8">
                    <div class="pass-field">
                        <label for="recovery-code">Teclee o pegue aquí su código</label>
                        <input type="text" id="code" name="recovery-code" required>
                    </div>
                    
                    <input type="submit" class="button-login" value="CONFIRMAR MI CORREO">
                </form>

            </div>

        </div>
	
	</div>
	
	<% } else { %>
	
		 <jsp:forward page = "login" />
	
	<% } %>

</body>
</html>