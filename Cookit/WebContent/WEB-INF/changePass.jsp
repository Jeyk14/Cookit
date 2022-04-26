<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Cambiar contraseña</title>
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>

	<div class="login">
		<div class="login-content">
	
            <h2 class="form-title recovery-title">Cambiar contraseña</h2>
            <h4 class="form-subtitle">Elija una nueva contraseña</h4>

            <form class="log-form" action="" method="post" accept-charset="utf-8">

                <div class="log-form">

                    <div class="pass-field">
                        <label for="new-pass">Nueva contrasñea</label>
                        <input type="text" id="new-pass" name="new-pass" required>
                    </div>
    
                    <div class="pass-field">
                        <label for="new-pass-repeat">Repetir contraseña</label>
                        <input type="password" id="new-pass-repeat" name="new-pass-repeat" required>
                    </div>
                    
                    <input type="submit" class="button-login" value="CAMBIAR CONTRASEÑA" />
    
                </div>

            </form>

        </div>
	</div>

</body>
</html>