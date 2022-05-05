<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

	<%@ page import="data.BeanUsuario"%>

	<%
		BeanUsuario myself = new BeanUsuario();
	
		if(session.getAttribute("myself") != null){
			myself = (BeanUsuario) session.getAttribute("myself");
		}
	%>
	
<html>
<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Modificar perfil</title>

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/profile.css">
    <link rel="stylesheet" href="css/profileMod.css">
    <link rel="stylesheet" href="css/recipeList.css">
</head>
<body>

		<div id="content">

        <jsp:include page="prebuilt/header.jsp" />

        <div class="profile">

            <div class="profile-content">

                <form method="changeImg" action="POST" enctype="multipart/form-data">
                    <div class="profile-pic">
                        <img src="LoadImgProfile?id=<%= myself.getId() %>" id="preview">
                    </div>
                    <div class="img-buttons">
                        <button style="display:block;width:120px;"
                            onclick="event.preventDefault(); document.getElementById('getFile').click()">Elegir
                            imagen</button>
                        <input type='file' id="getFile" style="display:none">
                        <input type="submit" value="Guardar imagen">
                    </div>
                </form>

                <div class="profile-info">

                    <form action="modProfile" method="POST">

                        <div class="username">
                            <label for="newName">Nombre de usuario</label><br>
                            <input type="text" id="newname" name="newName" value="<%= myself.getNombre() %>"/>
                        </div>

                        <ul class="user-param">

                            <li class="mail">
                                <label for="newEmail"
                                    title="Correo electrónico&#013;Si cambias tu correo electrónico no podrás crear publicaciones hasta que confirmes el nuevo coreo electrónico">*E-mail</label>
                                <input type="text" name="newEmail" value="<%= myself.getEmail() %>"
                                    title="Correo electrónico&#013;Si cambias tu correo electrónico no podrás crear publicaciones hasta que confirmes el nuevo coreo electrónico" />
                            </li>
                            <li class="time">
                                <label for="age">Edad</label>
                                <input type="number" name="newAge" value="<%= myself.getEdad() %>"/>
                            </li>
                            <li class="joined">
                                <p>08/04/2022</p>
                            </li>
                            <li class="diet">
                                <label for="newDiet">Dieta</label>
                                <input type="text" name="newDiet" value="<%= myself.getDieta() %>" />
                            </li>
                            <li class="national">
                                <label for="newNation">Nacionalidad</label>
                                <input type="text" name="newNation" value="<%= myself.getNacionalidad() %>" />
                            </li>

                            <input type="submit" value="Guardar cambios" />

                        </ul>

                    </form>

                    <form action="changePass" method="POST">

                        <ul class="email-form">

                            <li>
                                <label for="oldPass">Contraseña antigua</label>
                                <input type="password" id="oldPass" name="oldPass" />
                            </li>
                            
                            <li>
                                <label for="newPass">Nueva contraseña</label>
                                <input type="password" id="newPass" name="newPass" />
                            </li>
        
                            <li>
                                <label for="newPass2">Repetir contraseña</label>
                                <input type="password" id="newPass2" name="newPass2" />
                            </li>
        
                            <input type="submit" value="Cambiar contraseña" />

                        </ul>
        
                    </form>

                </div>

            </div>

        </div>

</body>
</html>