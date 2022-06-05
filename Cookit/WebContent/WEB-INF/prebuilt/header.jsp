	<%@ page import="data.BeanUsuario" %>
	
	<% 
		BeanUsuario myself = (BeanUsuario) session.getAttribute("myself"); 
		String curPage = (String) session.getAttribute("curPage");
		
		if(curPage == null){ // <----- ???
			 session.setAttribute("curPage", "recipe");
			 curPage = "recipe";
		}
		
	%>

	<%-- The header itself --%>
	<%if(myself != null){ // logged in%>
	
		<div class="header" id="top">

			<div class="header-content">
				<div class="logo">
					<img src="img/placeholder.png" />
				</div>
	
				<div class="header-info">

					<% if(!myself.isConfirmado()){ %>
						<div class="header-create">
							<p><a href="confirmEmail">Confirma tu correo</a> para publicar una receta</p>
						</div>
					<% } else { %>

						<div class="header-create">
							<a href="createRecipe">
								<button>Crear receta</button>
							</a>
						</div>

					<% } %>
	
					<div class="user-info">
						<a href="profile?id=<%= myself.getId() %>">
							<h4><%= myself.getNombre() %></h4>
							<h5><%= myself.getEmail() %></h5>
						</a>
					</div>
	
					<div class="session-buttons">
						<% if(curPage.equals("myprofile")){ 
								
							out.print("<div><a href='profile?id="+myself.getId()+"'><button>Ver mi perfil</button></a></div>"); 
								
							} %>
						<div><a href="logoff"><button>Cerrar sesión</button></a></div>
					</div>
				</div>
			</div>
		</div>
	
	<% } else { %>

		<div class="header" id="top">

			<div class="header-content">
				<div class="logo">
					<img src="img/placeholder.png" />
				</div>
	
				<div class="header-info">

					<div class="header-create">
						<p><a href="login">Inicia sesión</a> para publicar una receta</p>
					</div>
	
					<div class="user-info">
						<h4>&nbsp;</h4>
						<h5>&nbsp;</h5>
					</div>
	
					<div class="session-buttons">
						<div><a href="login"><button>Iniciar sesión</button></a></div>
						<div><a href="signin"><button>Crear cuenta</button></a></div>
					</div>
				</div>
			</div>
		</div>
	
	<% } %>
	
	<%-- The "go to index" button only appears when not in the index --%>
	<% if(!curPage.equals("index")){
		
		out.print("<div class='goto'><a href='index'><button class='gotoIndex'>Volver a inicio</button></a></div>");
		
	} %>