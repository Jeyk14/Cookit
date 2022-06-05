<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

	<%@ page import="data.BeanUsuario"%>
	<%@ page import="data.BeanReceta"%>
	<%@ page import="data.BeanCategoria"%>
	<%@ page import="java.util.Calendar"%>

	<%
		int starRate = 0;
		int recipeCont = 0;
		// header.jsp -> (BeanUsuario) loggedUsr
		BeanReceta[] recipeList = (BeanReceta[]) request.getAttribute("recipeList");
		BeanUsuario user = (BeanUsuario) request.getAttribute("user");
		BeanUsuario myself = new BeanUsuario();
		BeanCategoria[] catList = (BeanCategoria[]) request.getAttribute("catList");
		int rowCont = 0;
		int pag = 1;
		
		String estado = "";
		
		Boolean isMyself = (boolean) request.getAttribute("isMyself");
		boolean isFound = (boolean) request.getAttribute("isFound");
		
		Calendar auxCal = user.getCreacion();
		
		if(session.getAttribute("myself") != null){
			myself = (BeanUsuario) session.getAttribute("myself");
		}
		
		if(request.getAttribute("pag") != null){
			pag = (int) request.getAttribute("pag");
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
		
		<% if(isFound == true ){ %>
		
		<div class="profile">

            <div class="profile-content">

                <div class="profile-pic">
                    <img src="loadImg?id=<%= user.getId() %>&target=user" onerror="this.onerror=null;this.src='img/noRecipeImg.png'"/>
                	 <% if(myself.getId() == user.getId()){ %>
			        
			        	<div class="modAcc">
			        		<a href="modProfile?id=<%= myself.getId() %>" ><button>Modificar mi perfil</button></a>
			        	</div>
			        	
			        <% } %>
                </div>
    
                <div class="profile-info">
    
                    <h3 class="username"><%= user.getNombre() %></h3>
    
                    <ul>
                        <li class="time">
                            <p><%= user.getEdad() %> a�os - Me he unido el <span><%= auxCal.get(Calendar.DATE)+"/"+auxCal.get(Calendar.MONTH)+"/"+auxCal.get(Calendar.YEAR) %></span></p>
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
		
			<% while(recipeCont < 9){
				if(recipeList[recipeCont] != null){
					
				starRate = recipeList[recipeCont].getEstrellas();

				 //put a new row every 3 elements
						if( rowCont == 0){ %> <div class="row"> <% } 
				 
						switch(recipeList[recipeCont].getEstado()){
						case "bloqueado":
							estado = "<div class='status'><img src='img/blocked.png' title='�La publicaci�n est� bloqueada!&#13Contecte con el equipo de Cookit para solucionarlo'></div>";
							break;
						case "guardado":
							estado = "<div class='status'><a href='createRecipe'><img src='img/saved.png' title='La publicaci�n no se ha publicado&#13Haga clic para continuar la edici�n de esta receta'></a></div>";
							break;
						case "ocultado":
							estado = "<div class='status'><a href='changeStatus?id="+recipeList[recipeCont].getIdPublicacion()+"&status=1'><img src='img/hidden.png' title='La publicaci�n est� oculta.&#13Pulsa para mostrarla'></a></div>";
							break;
						case "publicado":
							estado = "<div class='status'><a href='changeStatus?id="+recipeList[recipeCont].getIdPublicacion()+"status=0'><img src='img/visible.png' title='La publicaci�n est� p�blica.&#13Pulsa para ocultarla'></a></div>";
							break;
						}%>
					
				<div class="column">
					<h4 class="col-title"><%= recipeList[recipeCont].getTitulo() %></h4>
					<div class="col-img">
						<a href="recipe?id=<%= recipeList[recipeCont].getIdReceta() %>">
						<img src="loadImg?id=<%= recipeList[recipeCont].getIdReceta() %>&target=recipe"
							title="Categor�a: <%= catList[recipeCont].getNombre() %>&#013;Tags: <%= recipeList[recipeCont].getTags() %>" onerror="this.onerror=null;this.src='img/broken.jpg'" />
							</a>
							
						<% if(isMyself){out.print(estado);} %>
				
						<div class="stars">
				
							<%	// Count from 0 to 4 while adding stars if rate > i
								for(int j = 0; j < 5; j++){%>
									<% if(starRate > j){ %>
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
				
			<% } recipeCont++; } %>
		
		<% } else { 
			// error -> apology
		%>
		
			<div class="errorMsg">
				<h3>Ups... algo ha ido mal</h3>
				<h4>Parece que el servicio no est� disponible ahora mismo,<br>vuelva a intentarlo dentro de un rato</h4>
				<h4>De parte del equipo de Cookit, le ofrecemos nuestra m�s sincera disculpa</h4>
			</div>
		
		<% } %>
		
		<div class="pageSelect">
			<div class="prePage">
				<a <% if(pag > 1){ out.print("href='profile?id="+user.getId()+"&pag="+pag+"'"); } %>><button>P&aacute;gina anterior</button></a>
			</div>
			
			<div class="curPage">
				<p>P�gina <%= pag %></p>
			</div>
			
			<div class="nextPage">
				<a href="profile?id=<%= user.getId() %>&pag=<%= pag + 1 %>"><button>P&aacute;gina siguiente</button></a>
			</div>
		</div>
		
		<% } else { //User not found%>
		
			<div class="notFound">
			
				<h3>Ups... Parece que no hemos encontrado lo que buscas</h3>
				<p>El usuario que buscas no existe o no se encuentra disponible</p>
				<br>
				<p>Lamentamos mucho el inconveniente</p>
				
			</div>
		
		<% } %>
		
		</div>
	
	<jsp:include page="prebuilt/footer.jsp" />

</body>
</html>