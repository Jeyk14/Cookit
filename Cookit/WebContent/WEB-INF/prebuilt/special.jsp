	<%@ page import="data.BeanReceta"%>
	
	<%
		BeanReceta special = (BeanReceta) session.getAttribute("special");
	%>
	
	<%
		// Put a random special recipe here
		if(special != null){
			
			int likes = special.getLikes();
			int dislikes = special.getDislikes();
			int starCont = 0;
			int starRate = 0;
			
			if(likes > 0){
				//starRate = 5-(((dislikes*100)/likes)/10);
				starRate = dislikes*100;
				if(dislikes > 0){
					starRate = starRate / likes;
				}
				starRate = starRate / 10;
				starRate = 5 - starRate;
			}
			
	%>

	<div class="special">
            <div class="special-alert">
                <h3>¡DESTACADO!</h3>
            </div>

            <div class="special-img">
                <img src="img/placeholder.png"/>
            </div>

            <div class="rating">
                <%	// Count from 0 to 4 while adding stars if rate > i
								for(int j = 0; j < 5; j++){%>
									<% if(starCont > j){ %>
										<img src="img/star.png"/>
									<% } else { %>
										<img src="img/star_0.png"/>
									<% } %>
								<%} %>
            </div>

            <div class="special-title">
                <h2>Placeholder</h2>
                <h4>De <span class="user-name">Placeholder</span></h4>
                <p class="tags">ejemplo, prueba, placeholder, muestra, maqueta</p>
            </div>
        </div>
	
	<%
		} // weekspecial == null -> do nothing
	%>