<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Login</title>
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>

	<%if(request.getAttribute("tempMsg") != null){%>
	
		<div class="errorMsg">
			<h4>Parece que algo ha ido mal...</h4>
			<p><%= request.getAttribute("tempMsg") %></p>
		</div>
	
	<%}%>

	<div class="login">
	
		<div class="login-content">
	
	           <h2 class="form-title">Iniciar sesi�n en cookit</h2>
	
	           <form class="log-form" action="" method="post" accept-charset="utf-8">
	
	               <div class="email-field">
	                   <label for="email">Email</label>
	                   <input type="text" id="email" name="email" required>
	               </div>
	
	               <div class="pass-field">
	                   <label for="pass">Contrase�a</label>
	                   <input type="password" id="pass" name="pass" required>
	               </div>
	
	               <div class="log-option">
	                   <div class="remember-me">
	                       <input type="checkbox" name="rememberme" />
	                       <label for="remember-me" id="remember-me">Recuerdame</label>
	                   </div>
	
	                   <a class="forgot" href=""><p>He olvidado mi contrase�a</p></a>
	               </div>
	               
	               <input type="submit" class="button-login" value="INICIAR SESI�N">
	
	           </form>
	
	           <div class="goto-registro">�No tienes cuenta? Reg�strate <a href="registro.html">aqu�</a></div>
	
	       </div>

    </div>

</body>
</html>