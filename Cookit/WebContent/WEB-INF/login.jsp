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
    
    <jsp:include page="prebuilt/boostrap.jsp" />
    
</head>
<body class="login-body">

	<jsp:include page="prebuilt/tempMsg.jsp" />
	
	<div class="side-img"></div>

	<div class="login d-flex justify-content-center align-items-center">
	
		<div class="login-content">
		
		<div class="exit-log"><a href="index" title="Salir de aqu&iacute;"><i class="bi bi-x-circle-fill"></i></a></div>
	
	           <h2 class="form-title">Iniciar sesión en Cookit!</h2>
	
	           <form class="log-form" action="login" method="post" accept-charset="utf-8">
	
	               <div class="email-field">
	                   <label for="email">Email</label>
	                   <input type="text" id="email" name="email" value="jeykantonio@gmail.com" required>
	               </div>
	
	               <div class="pass-field">
	                   <label for="pass">Contraseña</label>
	                   <input type="password" id="pass" name="pass" value="jeykantonio" required>
	               </div>
	
	               <div class="row">
	                   <div class=" float-left row-6 text-left">
	                       <input type="checkbox" name="rememberme" />
	                       <label for="remember-me" id="remember-me">Recuerdame</label>
	                   </div>
						<div class="float-right row-6">
	                   		<a class="link" href="restorePass"><p>He olvidado mi contraseña</p></a>
	               		</div>
	               </div>
	               
	               <input type="submit" class="button-login" value="INICIAR SESIÓN">
	
	           </form>
	
	           <div class="goto-registro">¿No tienes cuenta? Regístrate <a href="signin">aquí</a></div>
	
	       </div>

    </div>
    
    <div style=" display:none;position: fixed;right:0px;bottom:0px;background-color:#ffffff99;padding:3em;">
    	<p>jeykantonio@gmail.com - jeykantonio</p>
    	<p>sofiasaragota@ejemplo.com - sofigota</p>
    	<p>ajarana@ejemplo.com - ajarana</p>
    	<p>sandraoliveira@ejemplo.com - sandraoli</p>
    </div>

</body>
</html>