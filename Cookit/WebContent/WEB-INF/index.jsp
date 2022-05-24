<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Cookit - Inicio</title>
	
	<link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/recipeList.css">
    <link rel="stylesheet" href="css/index.css">

</head>
<body>

	<%@ page import="data.BeanUsuario"%>
	<%@ page import="data.BeanReceta"%>
	<%@ page import="data.BeanCategoria"%>

	<%
		int starRate = 0;
		int likes = 0;
		int dislikes = 0;
		int recipeCont = 0;
		// header.jsp -> (BeanUsuario) loggedUsr
		BeanReceta[] recipeList = (BeanReceta[]) request.getAttribute("recipeList");
		BeanUsuario[] userList = (BeanUsuario[]) request.getAttribute("userList");
		BeanCategoria[] catList = (BeanCategoria[]) request.getAttribute("catList");
		
		int rowCont = 0;
		
		int pag = 1;
		
		if(session.getAttribute("curpage") != null){
			pag = (int) session.getAttribute("pag");
		}
	%>

	<jsp:include page="prebuilt/header.jsp" />
	
	<div class="go-up" id="go-up" style="display: none;" title="Volver arriba">
        <div class="arrow"></div>
    </div>
	
	<div class="search-form">
		<form action="index" method="post">
			<div class="search">
                <label for="title-search">Buscar</label>
                <input type="text" id="title-search" name="title" title="Palabras clave que estés buscando.&#013;Sólo se buscará por el título de la publicación&#013;&#013;Ejemplo: 'puré de ' o 'sopa de '"/>
            </div>

            <div class="order">
                <select name="order" id="order">
                    <option value="none">Sin ordenar</option>
                    <option value="dateasc">Recetas más recientes</option>
                    <option value="datedesc">Recetas más antiguas</option>
                    <option value="titledesc">Ordenar por título descendente (A-Z)</option>
                    <option value="titleasc">Ordenar por título ascedente (Z-A)</option>
                    <option value="likesdesc">Mejor valorados primero</option>
                    <option value="likesasc">Peor valorados primero</option>
                    <option value="timedesc">Lento de preparar</option>
                    <option value="timeasc">Rápido de preparar</option>
                  </select>
            </div>

            <div class="tag">
                <label for="tag-search">Buscar tags</label>
                <input type="text" name="tag" title="Los tags de la receta que estás buscando&#013;Escribe todos los tags separados por coma y un espacio.&#013;Escribir cualquier otro signo que no sea un espacio o añadir más de 6 tags invalidará la búsqueda&#013;&#013;Ejemplo 'ensalada, tomate, remolacha, lechuga'"/>
            </div>
			<input type="submit" value="Buscar" />
		</form>
	</div>

<div class="separator"></div>

	<jsp:include page="prebuilt/special.jsp" />
	<jsp:include page="prebuilt/tempMsg.jsp" />


	<div id="content">
		<%
			// show all recipes in 3 columns each
			if(recipeList != null){
		%>
		
			<% while(recipeCont < 12){ 
				if(recipeList[recipeCont] != null){%>
			
			<%	
			
				starRate = recipeList[recipeCont].getStars();

				%>
				
				<% //put a new row every 3 elements
					if( rowCont == 0){ %> <div class="row"> <% } %>
					
					
					<div class="column"> <!-- start column -->
						<h4 class="col-title"><%= recipeList[recipeCont].getTitulo() %></h4>
						<div class="col-img"> <!-- start col-img -->
							<a href="recipe?id=<%= recipeList[recipeCont].getIdReceta() %>">
								<img src="loadImg?id=<%= recipeList[recipeCont].getIdReceta() %>&target=recipe" title="Categoría: <%= catList[recipeCont] %>&#013;Tags: <%= recipeList[recipeCont].getTags() %>" onerror="this.onerror=null;this.src='img/broken.jpg'" />
							</a>
					
							<div class="stars"> <!-- start stars -->
					
								<%	// Count from 0 to 4 while adding stars if rate > i
								for(int j = 0; j < 5; j++){%>
									<% if(starRate > j){ %>
										<img src="img/star.png"/>
									<% } else { %>
										<img src="img/star_0.png"/>
									<% } %>
								<%} %>
					
							</div> <!-- end stars -->
					
						</div> <!-- col-img -->
						
						<div class="col-info"> <!-- start col-info -->
							<div class="col-author">
								<a href="profile?id=<%= userList[recipeCont].getId() %>"><p><%= userList[recipeCont].getNombre() %></p></a>
							</div>
							<div class="col-time">
								<p><%= recipeList[recipeCont].getTiempo() %>m</p>
							</div>
						</div> <!-- end col-info -->
					
					</div> <!-- end column -->
					
				<% // if already 3 col OR next col is null -> put row end
					if( rowCont == 2 || recipeList[recipeCont+1] == null ){ %> </div> <!-- end row --> <% rowCont = 0; } else { rowCont ++; }  %>
				
			<% } recipeCont++; } %>
		
		<% } else { 
			// error -> apology
		%>
		
			<div class="errorMsg">
				<h3>Ups... algo ha ido mal</h3>
				<h4>Parece que el servicio no está disponible ahora mismo,<br>vuelva a intentarlo dentro de un rato</h4>
				<h4>De parte del equipo de Cookit, le ofrecemos nuestra más sincera disculpa</h4>
			</div>
		
		<% } %>
		
		</div> <!-- end #content -->
		
		<div class="pageSelect">
			<div class="prePage">
				<a <% if(pag > 1){ out.print("href='index?pag="+pag+"'"); } %>><button>P&aacute;gina anterior</button></a>
			</div>
			
			<div class="curPage">
				<p>Página <%= pag %></p>
			</div>
			
			<div class="nextPage">
				<a href='index?pag=<%= pag + 1 %>'><button>P&aacute;gina siguiente</button></a>
			</div>
		</div>
	
	<jsp:include page="prebuilt/footer.jsp" />
	
	<script src="js/goup.js"></script>

</body>
</html>