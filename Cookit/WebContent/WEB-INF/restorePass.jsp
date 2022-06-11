<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
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

	<jsp:include page="prebuilt/tempMsg.jsp" />

	<div class="login">
	
		<div class="login-content">

            <h2 class="form-title recovery-title">Recuperar contrase�a</h2>
            <h4 class="form-subtitle">Env�a un codigo de verificaci�n a tu correo electr�nico para saber que eres tu y s�lo tu</h4>

            <div class="log-form col">
	            <div class="row">
	            	<form class="email-field" action="restorePass" method="post" accept-charset="utf-8">
	            		<label for="target-email">Email</label>
	                    <input type="email" id="email" name="target-email" required>
	                    <input type="submit" class="send-email" name="submit" value="ENVIAR CLAVE">
	            	</form>
	            </div>
	            
	            <div class="row">
		            <form class="email-field" action="restorePass" method="post" accept-charset="utf-8">
		
		                   <label for="recovery-code">C�digo de recuperacion</label>
		                   <input type="text" id="code" name="recovery-code" >
		                  
		                   <input type="submit" class="button-login" name="submit" value="RESTAURAR MI CONTRASE�A">
		               </form>
	            </div>
	        </div>

		</div>
	
	</div>

</body>
</html>