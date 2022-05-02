<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

	<%@ page import="data.BeanUsuario"%>
	<%@ page import="data.BeanReceta"%>
	<%@ page import="data.BeanCategoria"%>

	<%
		int starCont = 0;
		int starRate = 0;
		int likes = 0;
		int dislikes = 0;
		int recipeCont = 0;
		// header.jsp -> (BeanUsuario) loggedUsr
		BeanReceta[] recipeList = (BeanReceta[]) request.getAttribute("recipeList");
		BeanUsuario user = (BeanUsuario) request.getAttribute("user");
		BeanCategoria[] catList = (BeanCategoria[]) request.getAttribute("catList");
		boolean addRow = false;
	%>

<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - <%= user.getNombre() %> (<%= user.getId() %>)</title>

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/profile.css">
    <link rel="stylesheet" href="css/recipeList.css">
</head>
<body>

	<div id="content">
	
		<jsp:include page="prebuilt/header.jsp" />
		
		<div class="profile">

            <div class="profile-content">

                <div class="profile-pic">
                    <img src="loadUsrImg?<%= user.getId() %>">
                </div>
    
                <div class="profile-info">
    
                    <h3 class="username"><%= user.getNombre() %></h3>
    
                    <ul>
                        <li class="time">
                            <p><%= user.getEdad() %> años - Me he unido el <span><%= user.getCreacion() %></span></p>
                        </li>
                        <li class="diet">
                            <p><%= user.getDieta() %></p>
                        </li>
                        <li class="national">
                            <p>Nacionalidad: <span><%= user.getNacionalidad() %></span></p>
                        </li>
                    </ul>
    
                </div>

            </div>

        </div>

            <div class="separator"></div>
		
		<%
			// show all recipes in 3 columns each
			if(recipeList != null){
		%>
		
			<% while(recipeList[recipeCont] != null){ %>
			
			<%	
				likes = recipeList[recipeCont].getLikes();
				dislikes = recipeList[recipeCont].getDislikes();
				
				%>
				
				<% //put a new row every 3 elements
					if( recipeCont%3 == 0 && addRow == true ){ %> <div class="row"> <% addRow = false; } %>
					
				<div class="column">
					<h4 class="col-title"><%= recipeList[recipeCont].getTitulo() %></h4>
					<div class="col-img">
						<img src="loadImg?<%= recipeList[recipeCont].getIdReceta() %>"
							title="Categoría: <%= catList[recipeCont] %>&#013;Tags: <%= recipeList[recipeCont].getTags() %>" />
				
						<div class="stars">
				
							<%	// Count from 0 to 4 while adding stars if rate > i
							for(int j = 0; j < 5; j++){%>
								<% if(starCont > j){ %>
									<img src="img/star.png"/>
								<% } else { %>
									<img src="img/star_0.png"/>
								<% } %>
							<%} %>
				
						</div>
				
					</div>
					<div class="col-info">
						<div class="col-author">
							<p><%= user.getNombre() %></p>
						</div>
						<div class="col-time">
							<p><%= recipeList[recipeCont].getTiempo() %>m</p>
						</div>
					</div>
				</div>
					
				<% //put a new row every 3 elements
					if( recipeCont%3 == 0 && addRow == false ){ %> </div> <% addRow = false; } %>
				
			<% recipeCont++; } %>
		
		<% } else { 
			// error -> apology
		%>
		
			<div class="errorMsg">
				<h3>Ups... algo ha ido mal</h3>
				<h4>Parece que el servicio no está disponible ahora mismo,<br>vuelva a intentarlo dentro de un rato</h4>
				<h4>De parte del equipo de Cookit, le ofrecemos nuestra más sincera disculpa</h4>
			</div>
		
		<% } %>
	</div>
	
	<jsp:include page="prebuilt/footer.jsp" />

</body>
</html>