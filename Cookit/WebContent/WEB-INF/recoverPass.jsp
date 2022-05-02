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
</head>
<body>

	<div class="login">
	
		<div class="login-content">

            <h2 class="form-title recovery-title">Recuperar contrase�a</h2>
            <h4 class="form-subtitle">Env�a un codigo de verificaci�n a tu correo electr�nico para cambiar la contrase�a</h4>

            <div class="log-form">

                <form class="email-field">
                    <label for="email-code">Email</label>
                    <input type="text" id="email" name="email-code" required>
                    <input type="submit" class="send-email" value="ENVIAR C�DIGO">
                </form>

                <form action="" method="post" accept-charset="utf-8">
                    <div class="pass-field">
                        <label for="recovery-code">C�digo de recuperacion</label>
                        <input type="text" id="code" name="recovery-code" required>
                    </div>
                    
                    <input type="submit" class="button-login" value="CONFIRMAR CORREO">
                </form>

            </div>

        </div>
	
	</div>

</body>
</html>