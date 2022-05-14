<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

	<%@ page import="data.BeanUsuario"%>
	<%@ page import="data.BeanReceta"%>
	<%@ page import="data.BeanCategoria"%>
	<%@ page import="java.util.Calendar"%>

	<%
		int starCont = 0;
		int starRate = 0;
		int likes = 0;
		int dislikes = 0;
		int recipeCont = 0;
		// header.jsp -> (BeanUsuario) loggedUsr
		BeanReceta[] recipeList = (BeanReceta[]) request.getAttribute("recipeList");
		BeanUsuario user = (BeanUsuario) request.getAttribute("user");
		BeanUsuario myself = new BeanUsuario();
		BeanCategoria[] catList = (BeanCategoria[]) request.getAttribute("catList");
		int rowCont = 0;
		
		Calendar auxCal = user.getCreacion();
		
		if(session.getAttribute("myself") != null){
			myself = (BeanUsuario) session.getAttribute("myself");
		}
		
	%>

<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - <%= user.getNombre() %></title>

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/profile.css">
    <link rel="stylesheet" href="css/recipeList.css">
</head>
<body>

	
	
	
		<jsp:include page="prebuilt/header.jsp" />
		
		<div id="content">
		
		<div class="profile">

            <div class="profile-content">

                <div class="profile-pic">
                    <img src="loadImg?id=<%= user.getId() %>&target=user" />
                </div>
    
                <div class="profile-info">
    
                    <h3 class="username"><%= user.getNombre() %></h3>
    
                    <ul>
                        <li class="time">
                            <p><%= user.getEdad() %> años - Me he unido el <span><%= auxCal.get(Calendar.DATE)+"/"+auxCal.get(Calendar.MONTH)+"/"+auxCal.get(Calendar.YEAR) %></span></p>
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
        
        <%
        	if(myself.getId() == user.getId()){
        %>
        
        	<div class="modAcc">
        		<a href="modProfile?id=<%= myself.getId() %>"><button>Modificar mi perfil</button></a>
        	</div>
        	
        <% } %>

            <div class="separator"></div>
		
		<%
			// show all recipes in 3 columns each
			if(recipeList != null){
		%>
		
			<% while(recipeList[recipeCont] != null){ %>
			
			<%	
				starRate = recipeList[recipeCont].getStars();
				
				%>
				
				<% //put a new row every 3 elements
						if( rowCont == 0){ %> <div class="row"> <% } %>
					
				<div class="column">
					<h4 class="col-title"><%= recipeList[recipeCont].getTitulo() %></h4>
					<div class="col-img">
						<img src="LoadRecipeImg?<%= recipeList[recipeCont].getIdReceta() %>"
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
				if( rowCont == 2 || recipeList[recipeCont+1] == null ){ %> </div> <!-- end row --> <% rowCont = 0; } else { rowCont ++; } %>
				
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
	
	<jsp:include page="prebuilt/footer.jsp" />

</body>
</html>