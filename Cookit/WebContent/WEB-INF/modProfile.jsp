<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

	<%@ page import="data.BeanUsuario"%>

	<%
		BeanUsuario myself = new BeanUsuario();
	
		String joinDate = "";
	
		if(session.getAttribute("myself") != null){
			myself = (BeanUsuario) session.getAttribute("myself");
			
			if(myself.getCreacion() != null){
				joinDate = myself.getCreacion().get(Calendar.DATE)+"/"+myself.getCreacion().get(Calendar.MONTH)+"/"+myself.getCreacion().get(Calendar.YEAR);
			}
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
    
    <jsp:include page="prebuilt/boostrap.jsp" />
    
</head>
<body>

		<div id="content">

        <jsp:include page="prebuilt/header.jsp" />

        <div class="profile-mod">

            <div class="profile-content">

				<div class="row">
                <form action="changeImg?target=user&id=<%= myself.getId() %>" method="POST" enctype="multipart/form-data">
                    <div class="profile-pic">
                        <img src="loadImg?target=user&id=<%= myself.getId() %>" id="preview">
                    </div>
                    
                    <div class="img-buttons d-flex flex-row justify-content-center">
                        <button class="p-6" style="display:block;width:120px;"
                            onclick="event.preventDefault(); document.getElementById('getFile').click()">Elegir
                            imagen</button>
                            <div class="p-2"></div>
                        <input type='file' id="getFile" name="image" style="display:none" required>
                        <input class="p-6" type="submit" value="Guardar imagen">
                    </div>
                    
                </form>
                </div>

                <div class="profile-info row">
                	<div class="col"></div>
                	
                	<div class="profile-config col-8 col-md-6">

                    <form action="changeProfile" method="POST" accept-charset="UTF-8">

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
                                <label for="age">Edad <%= Integer.valueOf(myself.getEdad()) %></label>
                                <input type="number" name="newAge" value="<%= Integer.valueOf(myself.getEdad()) %>"/>
                            </li>
                            <li class="joined">
                                <p>Me he unido el <%= joinDate %></p>
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
                <div class="col"></div>
				</div>
            </div>

        </div>
        
        </div>
        
        <script src="js/previewImg.js"></script>

		<jsp:include page="prebuilt/footer.jsp" />
</body>
</html>