<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cookit - Inicio</title>
</head>
<body>

	<%@ page import="data.BeanUsuario"%>
	<%@ page import="data.BeanReceta"%>
	<%@ page import="data.BeanCategoria"%>

	<%
		int starCont = 0;
		int starRate = 0;
		int likes = 0;
		int dislikes = 0;
		// header.jsp -> (BeanUsuario) loggedUsr
		BeanReceta[] recipeList = (BeanReceta[]) request.getAttribute("recipeList");
		BeanUsuario[] userList = (BeanUsuario[]) request.getAttribute("userList");
		BeanCategoria[] catList = (BeanCategoria[]) request.getAttribute("catList");
		boolean addRow = false;
	%>

	<jsp:include page="prebuilt/header.jsp" />

	<div class="separator"></div>

	<jsp:include page="prebuilt/special.jsp" />

	<span class="separator"></span>


	<div class="content">
		<%
			// show all recipes in 3 columns each
			if(recipeList != null){
		%>
		
			<% for(int i = 0; i < recipeList.length ; i++){ %>
			
			<%	likes = recipeList[i].getLikes();
				dislikes = recipeList[i].getDislikes();
				
				//starRate = 5−(((dislikes×100)÷likes)÷10);
				%>
				
				<% //put a new row every 3 elements
					if( i%3 == 0 && addRow == true ){ %> <div class="row"> <% addRow = false; } %>
					
				<div class="column">
					<h3 class="col-title"><%= recipeList[i].getTitulo() %></h3>
					<div class="col-img">
						<img src="loadImg?<%= recipeList[i].getIdReceta() %>" title="<%= catList[i] %>" />
						<div class="stars">
						
							<%	// Count from 0 to 4 while adding stars if rate > i
							for(int j = 0; j < 5; j++){%>
								<% if(starCont > j){ %>
									<img src="img/star.png"/>
								<% } else { %>
									<img src="img/star0.png"/>
								<% } %>
							<%} %>
							
						</div>
					</div>
					<div class="col-author"><p><%= userList[i].getNombre() %></p></div>
					<div class="col-time"> <p><%= recipeList[i].getTiempo() %></p> </div>
				</div>
					
				<% //put a new row every 3 elements
					if( i%3 == 0 && addRow == false ){ %> </div> <% addRow = false; } %>
				
			<% } %>
		
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

</body>
</html>