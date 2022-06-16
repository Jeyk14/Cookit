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

<jsp:include page="prebuilt/boostrap.jsp" />

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

	if (session.getAttribute("pag") != null) {
		pag = (int) session.getAttribute("pag");
	}
	%>

	<jsp:include page="prebuilt/header.jsp" />

	<jsp:include page="prebuilt/search.jsp" />

	<jsp:include page="prebuilt/special.jsp" />

	<jsp:include page="prebuilt/tempMsg.jsp" />

	<div class="separator"></div>

	<div id="content">
	
		<%// show all recipes in 3 columns each
		if (recipeList != null) {
		%>
		<div class="container">

			<%
				while (recipeCont < 12) {

				if (recipeList[recipeCont] != null) {
					starRate = recipeList[recipeCont].getEstrellas();
					//put a new row every 3 elements

					if (rowCont == 0) {
			%>
			<div class="cookit-row row my-0">
				<%
					}
				%>


				<div class="col-lg-4 my-4" align="center">
					<!-- start column -->

					<div class="cookit-column">
						<h4 class="cookit-col-title"><%=recipeList[recipeCont].getTitulo()%></h4>
						<div class="cookit-col-img">
							<!-- start col-img -->
							<a href="recipe?id=<%=recipeList[recipeCont].getIdReceta()%>">
								<img
								src="loadImg?id=<%=recipeList[recipeCont].getIdReceta()%>&target=recipe"
								title="Categoría: <%=catList[recipeCont].getNombre()%>&#013;Tags: <%=recipeList[recipeCont].getTags()%>"
								onerror="this.onerror=null;this.src='img/noRecipeImg.jpg'" />
							</a>

							<div class="stars">
								<!-- start stars -->

								<%
									// Count from 0 to 4 while adding stars if rate > i
								for (int j = 0; j < 5; j++) {
								%>
								<%
									if (starRate > j) {
								%>
								<img src="img/star.png" />
								<%
									} else {
								%>
								<img src="img/star_0.png" />
								<%
									}
								%>
								<%
									}
								%>

							</div>
							<!-- end stars -->

						</div>
						<!-- col-img -->

						<div class="cookit-col-info">
							<!-- start col-info -->
							<div class="cookit-col-author">
								<a href="profile?id=<%=userList[recipeCont].getId()%>"><p><%=userList[recipeCont].getNombre()%></p></a>
							</div>
							<div class="cookit-col-time">
								<p><%=recipeList[recipeCont].getTiempo()%>m
								</p>
							</div>
						</div>
						<!-- end col-info -->
					</div>

				</div>
				<!-- end column -->


				<%
					// if already 3 col OR next col is null -> put row end
				if (rowCont == 2 || recipeList[recipeCont + 1] == null) {
				%>
			</div>
			<!-- end row -->
			<%
				rowCont = 0;
			} else {
			rowCont++;
			}

			}
			recipeCont++;
			}

			} else {
			// error -> apology
			%>

			<div class="errorMsg">
				<h3>Ups... algo ha ido mal</h3>
				<h4>
					Parece que el servicio no está disponible ahora mismo,<br>vuelva
					a intentarlo dentro de un rato
				</h4>
				<h4>De parte del equipo de Cookit, le ofrecemos nuestra más
					sincera disculpa</h4>
			</div>

			<%
				}
			%>
		</div>

	</div>
	<!-- end #content -->

	<div class="pageSelect d-flex flex-row justify-content-center">
	
		<div class="prePage p-4">
			<a <%if (pag > 1) { out.print("href='index?pag=" + pag + "'"); }%>>
				<button style="height: max-content;">P&aacute;gina anterior</button>
			</a>
		</div>

		<div class="curPage p-4 text-center">
			<p>Página <%=pag%></p>
		</div>

		<div class="nextPage p-4">
			<a href='index?pag=<%=pag + 1%>'>
				<button style="height: max-content;">P&aacute;gina siguiente</button>
			</a>
		</div>
		
	</div>

	<jsp:include page="prebuilt/goup.jsp" />

	<jsp:include page="prebuilt/footer.jsp" />

</body>
</html>