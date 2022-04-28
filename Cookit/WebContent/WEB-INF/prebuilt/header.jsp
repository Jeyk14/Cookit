<div class="header">
	<div class="logo">
		<img src="img/placeholder.png" />
	</div>
	
	<%@ page import="data.BeanUsuario" %>
	
	<% BeanUsuario myself = (BeanUsuario) session.getAttribute("myself"); %>

	<%if(myself != null){%>
	
		<div class="user-info">
			<h5><%= myself.getNombre() %></h5>
			<h5><%= myself.getEmail() %></h5>
		</div>
		
		<div class="session-buttons">
			<a href="profile?id=<%= myself.getId() %>">
				<button>Ver mi perfil</button>
			</a>
			
			<% if(!myself.isConfirmado()){ %>
				<a href="confirmEmail">
					<button>Confirmar correo</button>
				</a>
			<% } %>
			
			<a href="logout">
				<button>Cerrar sesión</button>
			</a>
		</div>
	
	<% } else { %>
	
		<div class="no-acc">
			<h4>¡Bienvenido a COOKIT!</h4>
		</div>
		
		<div class="session-buttons">
			<a href="login">
				<button>Iniciar sesión</button>
			</a>
			<a href="signin">
				<button class="flashy-button">Crear cuenta</button>
			</a>
		</div>
	
	<% } %>

</div>

<div class="search-form">
	<form action="" method="GET">
		<div class="search">
			<label for="title-search"> Buscar </label> <input type="text"
				id="title-search" name="title" />
		</div>
		<div>
			<select name="order" id="order">
				<option value="none">Sin ordenar</option>
				<option value="dateasc">Má recientes</option>
				<option value="datedesc">Má antiguas</option>
				<option value="titledesc">Ordenar por tÃ­tulo descendente
					(A-Z)</option>
				<option value="titleasc">Ordenar por título ascedente (Z-A)</option>
				<option value="likesdesc">Mejor valorados primero</option>
				<option value="likesasc">Peor valorados primero</option>
				<option value="timedesc">Dificiles de preparar</option>
				<option value="timeasc">FÃ¡ciles de preparar</option>
			</select>
		</div>
		<div class="tag">
			<label for="tag-search">Buscar tags</label> <input type="text"
				name="tag" />
		</div>
	</form>
	<div class="toggle-search" id="toggle-search"><button>Ocultar</button></div>
</div>

<div class="separator"></div>