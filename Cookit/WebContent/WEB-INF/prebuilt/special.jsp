	<%@ page import="data.BeanEspecial"%>
	
	<%
	
		if(session.getAttribute("special") != null){
			
			BeanEspecial special = (BeanEspecial) session.getAttribute("special");
			int starRate = special.getEstrellas();

	%>


<div class="row">
	<div class="col"></div>
		<div class="special d-flex justify-content-center col-10 col-md-8 col-xl-6">
		
			<div class="special-alert">
                <h3>¡DESTACADO!</h3>
            </div>
            
			<div class="row">

	            <div class="special-img col-lg-6">
	            	<a href="recipe?id=<%= special.getIdPublicacion() %>"><img src="loadImg?id=<%= special.getIdReceta() %>&target=recipe" class="d-block mx-auto" onerror="this.onerror=null;this.src='img/noRecipeImg.jpg';this.title='Oops... Ha ocurrido un problema cargando la imagen'"/></a>
	            </div>
            
	            <div class="col-lg-6  d-flex align-items-center">
	            
	            	<div class="container">
		            	<div class="rating mx-auto">
		                <%	// Count from 0 to 4 while adding stars if rate > i
								for(int j = 0; j < 5; j++){%>
									<% if(starRate > j){ %>
										<img src="img/star.png"/>
									<% } else { %>
										<img src="img/star_0.png"/>
									<% } %>
								<%} %>
		            	</div>
		
			            <div class="special-title row">
			                <a href="recipe?id=<%= special.getIdPublicacion() %>"><h2><%= special.getTitulo() %></h2></a>
			                <h4>De <a href="profile?id=<%= special.getIdUsuario() %>"><span class="user-name"><%= special.getAutor() %></span></a></h4>
			                <p class="tags mb-5"><%= special.getTags() %></p>
			            </div>
	            	</div>
            	</div>
        	</div>
        </div>
        <div class="col"></div>
     </div>
	
	<%
		} // weekspecial == null -> do nothing
	%>