<%@ page import="data.BeanUsuario"%>

<%
	BeanUsuario myself = (BeanUsuario) session.getAttribute("myself");
	String curPage = (String) session.getAttribute("curPage");

if (curPage == null) { // <----- ???
	session.setAttribute("curPage", "recipe");
	curPage = "recipe";
}
%>

<div class="header fixed-top ">
<div class="row ">

	<div class="col-3">
		<div class="logo">
			<img src="img/placeholder.png">
		</div>
	</div>

	<div class="col"></div>

	<div class="col-7 cookit-navigation">
		<nav class=" navbar navbar-expand-sm ">
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarSupportedContent"
				aria-controls="navbarSupportedContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav ms-auto">
				<li class="nav-item active"><a class="nav-link" href="index">
							<i class="bi bi-house-door-fill"></i> Ir al inicio
					</a></li>
					<%
						if (myself != null) {
					%>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
						role="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false"> <i class="bi bi-person-circle"></i> <%= myself.getNombre() %> </a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="profile"> <i class="bi bi-person-lines-fill"></i> Ver mi perfil </a> <a
								class="dropdown-item" href="modProfile"> <i class="bi bi-wrench-adjustable"></i> Modificar mi perfil</a>
							<div class="dropdown-divider"></div>
							<% if(!myself.isConfirmado()){ %>
								<a class="dropdown-item text-danger" href="confirmEmail"> <i class="bi bi-wrench-adjustable"></i> ¡Confirmar mi correo!</a>
								<div class="dropdown-divider"></div>
							<% } %>
							<a class="dropdown-item" href="logoff"> <i class="bi bi-x-square-fill"></i> Cerrar sesión</a>
						</div></li>
					<%
						} else {
					%>
					<li class="nav-item"><a class="nav-link" href="signin"> <i class="bi bi-person-plus-fill"></i> Registrarme </a></li>
					<li class="nav-item"><a class="nav-link" href="login"> <i class="bi bi-door-open-fill"></i> Iniciar sesión </a></li>
					<%
						}
					%>
					<% if(myself != null){ %>
					<li class="nav-item"><a class="nav-link" href=createRecipe> <i class="bi bi-pencil"></i> Crear una receta </a></li>
					<% } %>
				</ul>
			</div>
		</nav>
	</div>
	<div class="col-1"></div>

</div>
</div>