<%
	boolean doExpandSearch = false;
	String search = "";
	String orderType = "";

	if(session.getAttribute("search-value") != null){
		doExpandSearch = true;
		search = (String) session.getAttribute("search-value");
	}
	
	if(session.getAttribute("order") != null){
		doExpandSearch = true;
		orderType = (String) session.getAttribute("order");
	}
	
%>

<div id="cookit-search-toggle">
	<button class="btn btn-primary rounded-circle float-right"
		type="button" data-toggle="collapse" data-target="#index-search"
		<% if(doExpandSearch){ out.print("aria-expanded='true'"); } else { out.print("aria-expanded='false'"); }%> aria-controls="index-search">
		<i class="bi bi-search"></i>
	</button>
</div>

<div id="index-search" class="search-form collapse row">
	<form action="index" method="post" class="col-6">
		<div class="mb-2 row" title="Busca por palabras el título, un tag o un ingrediente de una publicación">
			<label class="col-3" for="title-search"> Buscar: </label> <input class="col" type="text" id="cookit-search" name="title" />
		</div>

		<div class="mb-2 row">
			<select name="order" id="order">
				<option value="none">Sin ordenar</option>
				<option value="dateasc" <% if(orderType.equals("dateasc")){out.print("selected");} %>>Recetas más recientes</option>
				<option value="datedesc" <% if(orderType.equals("datedesc")){out.print("selected");} %>>Recetas más antiguas</option>
				<option value="titledesc" <% if(orderType.equals("titledesc")){out.print("selected");} %>>Ordenar por título descendente
					(A-Z)</option>
				<option value="titleasc" <% if(orderType.equals("titleasc")){out.print("selected");} %>>Ordenar por título ascedente (Z-A)</option>
				<option value="likesdesc" <% if(orderType.equals("likedesc")){out.print("selected");} %>>Mejor valorados primero</option>
				<option value="likesasc" <% if(orderType.equals("likeasc")){out.print("selected");} %>>Peor valorados primero</option>
				<option value="timedesc" <% if(orderType.equals("timedesc")){out.print("selected");} %>>Lento de preparar</option>
				<option value="timeasc" <% if(orderType.equals("timeasc")){out.print("selected");} %>>Rápido de preparar</option>
			</select>
		</div>

		<input type="submit" role="button" value="Buscar" />
	</form>

</div>

<div class="separator"></div>