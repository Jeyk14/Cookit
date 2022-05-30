	<%@ page import="data.BeanEspecial"%>
	
	<%
	
		if(session.getAttribute("special") != null){
			
			BeanEspecial special = (BeanEspecial) session.getAttribute("special");
			int starRate = special.getEstrellas();

	%>

	<div class="special">
            <div class="special-alert">
                <h3>¡DESTACADO!</h3>
            </div>

            <div class="special-img">
            	<a href="recipe?id=<%= special.getIdPublicacion() %>"><img src="loadImg?id=<%= special.getIdReceta() %>&target=recipe" onerror="this.onerror=null;this.src='img/noRecipeImg.jpg';this.title='Oops... Ha ocurrido un problema cargando la imagen'"/></a>
            </div>

            <div class="rating">
                <%	// Count from 0 to 4 while adding stars if rate > i
						for(int j = 0; j < 5; j++){%>
							<% if(starRate > j){ %>
								<img src="img/star.png"/>
							<% } else { %>
								<img src="img/star_0.png"/>
							<% } %>
						<%} %>
            </div>

            <div class="special-title">
                <a href="profile?id=<%= special.getIdUsuario() %>"><h2><%= special.getTitulo() %></h2></a>
                <h4>De <span class="user-name"><%= special.getAutor() %></span></h4>
                <p class="tags"><%= special.getTags() %></p>
            </div>
        </div>
	
	<%
		} // weekspecial == null -> do nothing
	%>