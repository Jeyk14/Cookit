	<%@ page import="data.BeanUsuario" %>
	
	<% BeanUsuario myself = (BeanUsuario) session.getAttribute("myself"); %>

	<%if(myself != null){%>
	
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
						<h4><%= myself.getNombre() %></h4>
						<h5><%= myself.getEmail() %></h5>
					</div>
	
					<div class="session-buttons">
						<div><a href="profile?id=<%= myself.getId() %>"><button>Ver mi perfil</button></a></div>
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